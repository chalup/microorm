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
import org.chalup.microorm.annotations.Column;
import org.chalup.microorm.annotations.Embedded;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.ContentValues;
import android.database.Cursor;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EmbeddedTest {

  static final String FIRST_NAME_COLUMN = "first_name";
  static final String LAST_NAME_COLUMN = "last_name";
  static final String AGE_COLUMN = "age";

  private static final String TEST_FIRST_NAME = "Jan";
  private static final String TEST_LAST_NAME = "Kowalski";
  private static final int TEST_AGE = 12;

  private MicroOrm testSubject;
  private Cursor cursorMock;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm();
    cursorMock = mock(Cursor.class);
    when(cursorMock.getColumnIndex(FIRST_NAME_COLUMN)).thenReturn(0);
    when(cursorMock.getColumnIndexOrThrow(FIRST_NAME_COLUMN)).thenReturn(0);
    when(cursorMock.getString(0)).thenReturn("Jan");

    when(cursorMock.getColumnIndex(LAST_NAME_COLUMN)).thenReturn(1);
    when(cursorMock.getColumnIndexOrThrow(LAST_NAME_COLUMN)).thenReturn(1);
    when(cursorMock.getString(1)).thenReturn("Kowalski");

    when(cursorMock.getColumnIndex(AGE_COLUMN)).thenReturn(2);
    when(cursorMock.getColumnIndexOrThrow(AGE_COLUMN)).thenReturn(2);
    when(cursorMock.getInt(2)).thenReturn(TEST_AGE);
  }

  @Test
  public void shouldPackEmbeddedFieldsIntoContentValues() throws Exception {
    Parent parent = new Parent();
    parent.person = new Person();
    parent.person.age = TEST_AGE;
    parent.person.name = new Name(TEST_FIRST_NAME, TEST_LAST_NAME);

    final ContentValues values = testSubject.toContentValues(parent);

    assertThat(values).contains(
        entry(AGE_COLUMN, TEST_AGE),
        entry(FIRST_NAME_COLUMN, TEST_FIRST_NAME),
        entry(LAST_NAME_COLUMN, TEST_LAST_NAME)
    );
  }

  @Test
  public void shouldUnpackEmbeddedValuesFromCursor() throws Exception {
    Parent parent = testSubject.fromCursor(cursorMock, Parent.class);
    assertThat(parent.getFirstName()).isEqualTo(TEST_FIRST_NAME);
    assertThat(parent.getLastName()).isEqualTo(TEST_LAST_NAME);
    assertThat(parent.getAge()).isEqualTo(TEST_AGE);
  }

  @Test
  public void shouldInitializeAllEmbeddedFieldsWhenCreatingObjectFromCursor() throws Exception {
    Cursor cursorMock = mock(Cursor.class);
    when(cursorMock.getColumnIndex(FIRST_NAME_COLUMN)).thenReturn(0);
    when(cursorMock.getColumnIndexOrThrow(FIRST_NAME_COLUMN)).thenReturn(0);
    when(cursorMock.isNull(0)).thenReturn(Boolean.TRUE);

    when(cursorMock.getColumnIndex(LAST_NAME_COLUMN)).thenReturn(1);
    when(cursorMock.getColumnIndexOrThrow(LAST_NAME_COLUMN)).thenReturn(1);
    when(cursorMock.isNull(1)).thenReturn(Boolean.TRUE);

    when(cursorMock.getColumnIndex(AGE_COLUMN)).thenReturn(2);
    when(cursorMock.getColumnIndexOrThrow(AGE_COLUMN)).thenReturn(2);
    when(cursorMock.getInt(2)).thenReturn(TEST_AGE);

    Parent parent = testSubject.fromCursor(cursorMock, Parent.class);

    assertThat(parent).isNotNull();
    assertThat(parent.person).isNotNull();
    assertThat(parent.person.name).isNotNull();
  }

  @Test
  public void shouldHandleNulledEmbeddedField() throws Exception {
    final Parent parent = new Parent();
    final ContentValues contentValues = testSubject.toContentValues(parent);
    assertValuesAreNull(contentValues, FIRST_NAME_COLUMN, LAST_NAME_COLUMN, AGE_COLUMN);
  }

  @Test
  public void shouldReturnCorrectProjectionForJavaClassWithEmbeddedFields() throws Exception {
    assertThat(testSubject.getProjection(Parent.class))
        .asList()
        .containsExactly(
            AGE_COLUMN,
            FIRST_NAME_COLUMN,
            LAST_NAME_COLUMN
        );
  }

  @Test
  public void shouldReturnCorrectProjectionForKotlinDataClassWithEmbeddedFields() throws Exception {
    assertThat(testSubject.getProjection(KotlinParentDataClass.class))
        .asList()
        .containsExactly(
            AGE_COLUMN,
            FIRST_NAME_COLUMN,
            LAST_NAME_COLUMN
        );
  }

  private void assertValuesAreNull(ContentValues values, String... columnNames) {
    for (String columnName : columnNames) {
      assertThat(values.containsKey(columnName)).isTrue();
      assertThat(values.get(columnName)).isNull();
    }
  }

  public static class Parent {

    @Embedded
    public Person person;

    public Parent() {

    }

    public int getAge() {
      return person.age;
    }

    public String getFirstName() {
      return person.name.firstname;
    }

    public String getLastName() {
      return person.name.lastname;
    }
  }

  public static class Person {

    @Column(AGE_COLUMN)
    private int age;

    @Embedded
    public Name name;
  }

  public static class Name {

    @Column(FIRST_NAME_COLUMN)
    private String firstname;

    @Column(LAST_NAME_COLUMN)
    private String lastname;

    public Name() {
    }

    private Name(String firstname, String lastname) {
      this.firstname = firstname;
      this.lastname = lastname;
    }
  }
}
