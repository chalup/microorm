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

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.ContentValues;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class NullableColumnsTest {

  private MicroOrm testSubject;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm();
  }

  private static class InvalidObject {
    @Column(value = "omg_nullable_primitive", treatNullAsDefault = true)
    int primitiveMember;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowSettingTreadNullAsDefaultOnPrimitiveColumns() throws Exception {
    testSubject.toContentValues(new InvalidObject());
  }

  private static class ObjectWithDefaultColumn {
    @Column(value = "column_with_default", treatNullAsDefault = true)
    Integer defaultInt;

    @Column("regular_column")
    String regularColumn;
  }

  @Test
  public void shouldNotIncludeInContentValuesNullsFromColumnsWithTreatNullAsDefaultEnabled() throws Exception {
    ObjectWithDefaultColumn o = new ObjectWithDefaultColumn();

    ContentValues contentValues = testSubject.toContentValues(o);
    assertThat(contentValues.containsKey("column_with_default")).isFalse();
    assertThat(contentValues.containsKey("regular_column")).isTrue();
  }

  @Test
  public void shouldIncludeInContentValuesNotNullsFromColumnsWithTreatNullAsDefaultEnabled() throws Exception {
    ObjectWithDefaultColumn o = new ObjectWithDefaultColumn();
    o.defaultInt = 42;

    ContentValues contentValues = testSubject.toContentValues(o);
    assertThat(contentValues.containsKey("column_with_default")).isTrue();
    assertThat(contentValues.containsKey("regular_column")).isTrue();
  }

  @Test
  public void shouldIncludeInContentValuesNullsFromColumnsWithTreatNullAsDefaultDisabled() throws Exception {
    ObjectWithDefaultColumn o = new ObjectWithDefaultColumn();

    ContentValues contentValues = testSubject.toContentValues(o);
    assertThat(contentValues.containsKey("regular_column")).isTrue();
    assertThat(contentValues.get("regular_column")).isNull();
  }
}
