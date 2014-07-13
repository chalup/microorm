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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;
import org.chalup.microorm.annotations.Embedded;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.database.Cursor;
import android.provider.BaseColumns;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class OverridingColumnsTest {

  private MicroOrm testSubject;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm();
  }

  public static class InvalidObject {
    @Column(BaseColumns._ID)
    int id;

    @Column(BaseColumns._ID)
    int _id;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowObjectWithDuplicateColumnAnnotation() throws Exception {
    testSubject.toContentValues(new InvalidObject());
  }

  private static class BaseObject {
    @Column(BaseColumns._ID)
    int id;
  }

  public static class InvalidDerivedObject extends BaseObject {
    @Column(BaseColumns._ID)
    int id;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowObjectWhichOverridesColumnAnnotationFromBaseClass() throws Exception {
    testSubject.toContentValues(new InvalidDerivedObject());
  }

  public static class EmbeddedObject {
    @Column(BaseColumns._ID)
    int id;
  }

  public static class InvalidCompoundObject {
    @Column(BaseColumns._ID)
    int id;

    @Embedded
    EmbeddedObject mEmbeddedObject;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowObjectWithEmbeddedObjectWhichOverridesColumnAnnotationFromCompoundObject() throws Exception {
    testSubject.toContentValues(new InvalidCompoundObject());
  }

  @Test
  public void shouldAllowReadingOneColumnIntoMultipleFields() throws Exception {
    Cursor cursor = mock(Cursor.class);
    when(cursor.getColumnIndexOrThrow(BaseColumns._ID)).thenReturn(0);
    when(cursor.getColumnIndex(BaseColumns._ID)).thenReturn(0);
    when(cursor.getInt(0)).thenReturn(5);

    InvalidObject simpleObject = testSubject.fromCursor(cursor, InvalidObject.class);
    assertThat(simpleObject.id).isEqualTo(5);
    assertThat(simpleObject._id).isEqualTo(5);

    InvalidDerivedObject derivedObject = testSubject.fromCursor(cursor, InvalidDerivedObject.class);
    assertThat(derivedObject.id).isEqualTo(5);

    InvalidCompoundObject compoundObject = testSubject.fromCursor(cursor, InvalidCompoundObject.class);
    assertThat(compoundObject.id).isEqualTo(5);
    assertThat(compoundObject.mEmbeddedObject.id).isEqualTo(5);
  }
}
