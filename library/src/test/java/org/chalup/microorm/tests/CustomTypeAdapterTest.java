/*
 * Copyright (C) 2013 Jerzy Chalupski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.chalup.microorm.tests;

import static com.google.common.truth.Truth.assertThat;
import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.android.content.ContentValuesEntry.entry;
import static org.mockito.Mockito.*;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.TypeAdapter;
import org.chalup.microorm.annotations.Column;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.ContentValues;
import android.database.Cursor;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CustomTypeAdapterTest {

  private static final String TEST_COLUMN = "TEST_COLUMN";
  private static final long TEST_VALUE = 42;

  private MicroOrm testSubject;
  private Cursor cursorMock;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm.Builder()
        .registerTypeAdapter(CustomType.class, new CustomTypeAdapter())
        .build();

    cursorMock = mock(Cursor.class);
    when(cursorMock.getColumnIndex(TEST_COLUMN)).thenReturn(0);
    when(cursorMock.getColumnIndexOrThrow(TEST_COLUMN)).thenReturn(0);
    when(cursorMock.getLong(0)).thenReturn(TEST_VALUE);
  }

  @Test
  public void shouldUnpackEmbeddedValuesFromCursor() throws Exception {
    DaoWithCustomType daoWithCustomType = testSubject.fromCursor(cursorMock, DaoWithCustomType.class);
    assertThat(daoWithCustomType.mCustomType.mBackingLong).isEqualTo(TEST_VALUE);
  }

  @Test
  public void shouldPackEmbeddedFieldsIntoContentValues() throws Exception {
    DaoWithCustomType daoWithCustomType = new DaoWithCustomType();
    daoWithCustomType.mCustomType = new CustomType();
    daoWithCustomType.mCustomType.mBackingLong = TEST_VALUE;

    final ContentValues values = testSubject.toContentValues(daoWithCustomType);

    assertThat(values).contains(entry(TEST_COLUMN, TEST_VALUE));
  }

  public static class DaoWithCustomType {
    @Column(TEST_COLUMN)
    public CustomType mCustomType;
  }

  public static class CustomType {
    public long mBackingLong;
  }

  public static class CustomTypeAdapter implements TypeAdapter<CustomType> {

    @Override
    public CustomType fromCursor(Cursor c, String columnName) {
      CustomType customType = new CustomType();
      customType.mBackingLong = c.getLong(c.getColumnIndexOrThrow(columnName));
      return customType;
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, CustomType object) {
      values.put(columnName, object.mBackingLong);
    }
  }
}
