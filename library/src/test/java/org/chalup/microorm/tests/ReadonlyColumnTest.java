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
import static org.mockito.Mockito.*;

import org.chalup.microorm.MicroOrm;
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
public class ReadonlyColumnTest {

  private MicroOrm testSubject;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm();
  }

  private static class InvalidObject {
    @Column(value = "readonly_column_with_default_flag", treatNullAsDefault = true, readonly = true)
    Integer readonlyMember;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowSettingTreadNullAsDefaultAndReadonlyBothToTrue() throws Exception {
    testSubject.toContentValues(new InvalidObject());
  }

  public static class ObjectWithReadonlyColumn {
    @Column(value = "readonly_column", readonly = true)
    int readonlyInt;

    @Column("regular_column")
    String regularColumn;
  }

  @Test
  public void shouldNotPutReadonlyColumnsIntoContentValues() throws Exception {
    ObjectWithReadonlyColumn o = new ObjectWithReadonlyColumn();

    ContentValues contentValues = testSubject.toContentValues(o);
    assertThat(contentValues.containsKey("readonly_column")).isFalse();
    assertThat(contentValues.containsKey("regular_column")).isTrue();
  }

  @Test
  public void shouldIncludeReadonlyColumnInProjection() throws Exception {
    String[] projection = testSubject.getProjection(ObjectWithReadonlyColumn.class);

    assertThat(projection).asList().contains("readonly_column");
    assertThat(projection).asList().contains("regular_column");
  }

  @Test
  public void shouldFillReadonlyColumnFromCursor() throws Exception {
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex("readonly_column")).thenReturn(0);
    when(c.getColumnIndexOrThrow("readonly_column")).thenReturn(0);
    when(c.isNull(0)).thenReturn(Boolean.FALSE);
    when(c.getInt(0)).thenReturn(42);

    when(c.getColumnIndex("regular_column")).thenReturn(1);
    when(c.getColumnIndexOrThrow("regular_column")).thenReturn(1);
    when(c.isNull(1)).thenReturn(Boolean.TRUE);

    ObjectWithReadonlyColumn o = testSubject.fromCursor(c, ObjectWithReadonlyColumn.class);
    assertThat(o.readonlyInt).isEqualTo(42);
  }
}
