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
import org.chalup.microorm.tests.CustomTypeAdapterTest.CustomType;
import org.chalup.microorm.tests.CustomTypeAdapterTest.CustomTypeAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.database.Cursor;

import java.math.BigDecimal;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GetColumnBuilderTest {

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
  public void shouldFetchCustomTypeColumnFromCursor() throws Exception {
    final CustomType customType = testSubject.getColumn(TEST_COLUMN).as(CustomType.class).apply(cursorMock);
    assertThat(customType.mBackingLong).isEqualTo(TEST_VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailIfCustomTypeIsNotRegistered() throws Exception {
    testSubject.getColumn(TEST_COLUMN).as(BigDecimal.class);
  }

  @Test(expected = NullPointerException.class)
  public void shouldFailIfColumnIsNull() throws Exception {
    testSubject.getColumn(null);
  }
}
