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
import org.chalup.microorm.annotations.Embedded;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.ContentValues;
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

  public static class ReadonlyObject {
    @Column(BaseColumns._ID)
    int id;

    @Column(BaseColumns._ID)
    int _id;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowGettingContentValuesFromObjectWithDuplicateColumnAnnotation() throws Exception {
    testSubject.toContentValues(new ReadonlyObject());
  }

  private static class BaseObject {
    @Column(BaseColumns._ID)
    int id;
  }

  public static class ReadonlyDerivedObject extends BaseObject {
    @Column(BaseColumns._ID)
    int id;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowGettingContentValuesFromObjectWhichOverridesColumnAnnotationFromBaseClass() throws Exception {
    testSubject.toContentValues(new ReadonlyDerivedObject());
  }

  public static class EmbeddedObject {
    @Column(BaseColumns._ID)
    int id;
  }

  public static class ReadonlyCompoundObject {
    @Column(BaseColumns._ID)
    int id;

    @Embedded
    EmbeddedObject mEmbeddedObject;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotAllowGettingContentValuesFromObjectWithEmbeddedObjectWhichOverridesColumnAnnotationFromCompoundObject() throws Exception {
    testSubject.toContentValues(new ReadonlyCompoundObject());
  }

  @Test
  public void shouldAllowReadingOneColumnIntoMultipleFields() throws Exception {
    Cursor cursor = mock(Cursor.class);
    when(cursor.getColumnIndexOrThrow(BaseColumns._ID)).thenReturn(0);
    when(cursor.getColumnIndex(BaseColumns._ID)).thenReturn(0);
    when(cursor.getInt(0)).thenReturn(5);

    ReadonlyObject simpleObject = testSubject.fromCursor(cursor, ReadonlyObject.class);
    assertThat(simpleObject.id).isEqualTo(5);
    assertThat(simpleObject._id).isEqualTo(5);

    ReadonlyDerivedObject derivedObject = testSubject.fromCursor(cursor, ReadonlyDerivedObject.class);
    assertThat(derivedObject.id).isEqualTo(5);

    ReadonlyCompoundObject compoundObject = testSubject.fromCursor(cursor, ReadonlyCompoundObject.class);
    assertThat(compoundObject.id).isEqualTo(5);
    assertThat(compoundObject.mEmbeddedObject.id).isEqualTo(5);
  }

  public static class WritableObjectWithDuplicateColumns {
    @Column(BaseColumns._ID)
    int id;

    @Column(value = BaseColumns._ID, readonly = true)
    int _id;
  }

  @Test
  public void shouldAllowGettingContentValuesFromObjectWithDuplicateColumnAnnotationIfOnlyOneColumnIsNotReadonly() throws Exception {
    WritableObjectWithDuplicateColumns object = new WritableObjectWithDuplicateColumns();
    object.id = 2;
    object._id = 1;

    ContentValues values = testSubject.toContentValues(object);
    assertThat(values.containsKey(BaseColumns._ID)).isTrue();
    assertThat(values.getAsInteger(BaseColumns._ID)).isEqualTo(2);
  }

  public static class WritableDerivedObjectWithDuplicateColumns extends BaseObject {
    @Column(value = BaseColumns._ID, readonly = true)
    int _id;
  }

  @Test
  public void shouldAllowGettingContentValuesFromObjectWhichOverridesColumnAnnotationFromBaseClassIfOnlyOneColumnIsNotReadonly() throws Exception {
    WritableDerivedObjectWithDuplicateColumns object = new WritableDerivedObjectWithDuplicateColumns();
    object.id = 2;
    object._id = 1;

    ContentValues values = testSubject.toContentValues(object);
    assertThat(values.containsKey(BaseColumns._ID)).isTrue();
    assertThat(values.getAsInteger(BaseColumns._ID)).isEqualTo(2);
  }

  public static class WritableCompoundObject {
    @Column(value = BaseColumns._ID, readonly = true)
    int id;

    @Embedded
    EmbeddedObject mEmbeddedObject;
  }

  @Test
  public void shouldAllowGettingContentValuesFromObjectWithEmbeddedObjectWhichOverridesColumnAnnotationFromCompoundObjectIfOnlyOneColumnIsNotReadonly() throws Exception {
    WritableCompoundObject object = new WritableCompoundObject();
    object.id = 2;
    object.mEmbeddedObject = new EmbeddedObject();
    object.mEmbeddedObject.id = 1;

    ContentValues values = testSubject.toContentValues(object);
    assertThat(values.containsKey(BaseColumns._ID)).isTrue();
    assertThat(values.getAsInteger(BaseColumns._ID)).isEqualTo(1);
  }

  public static class InvalidObjectWithDuplicateTreatNullAsDefaultColumns {
    @Column(value = BaseColumns._ID, treatNullAsDefault = true)
    Long id;

    @Column(value = BaseColumns._ID, treatNullAsDefault = true)
    Long _id;
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldIgnoreTreatNullAsDefaultWhenGettingContentValuesFromObjectWithDuplicateColumnAnnotation() throws Exception {
    testSubject.toContentValues(new InvalidObjectWithDuplicateTreatNullAsDefaultColumns());

    // One might argue that in this case toContentValues() should work, because
    // both Columns containing null are ignored, but it would complicate the
    // implementation and mental usage model.
    //
    // The annotated class is either readonly or read-write, regardless of data it holds.
  }
}
