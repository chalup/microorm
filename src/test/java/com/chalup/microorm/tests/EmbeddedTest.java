package com.chalup.microorm.tests;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chalup.microorm.MicroOrm;
import com.chalup.microorm.annotations.Column;
import com.chalup.microorm.annotations.Embedded;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentValues;

import android.content.ContentValues;
import android.database.Cursor;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EmbeddedTest {

  private static final String FIRST_NAME_COLUMN = "first_name";
  private static final String LAST_NAME_COLUMN = "last_name";
  private static final String AGE_COLUMN = "age";

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
    final ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsInteger(AGE_COLUMN)).isEqualTo(TEST_AGE);
    assertThat(shadowValues.getAsString(FIRST_NAME_COLUMN)).isEqualTo(TEST_FIRST_NAME);
    assertThat(shadowValues.getAsString(LAST_NAME_COLUMN)).isEqualTo(TEST_LAST_NAME);
  }

  @Test
  public void shouldUnpackEmbeddedValuesFromCursor() throws Exception {
    Parent parent = testSubject.fromCursor(cursorMock, Parent.class);
    assertThat(parent.getFirstName()).isEqualTo(TEST_FIRST_NAME);
    assertThat(parent.getLastName()).isEqualTo(TEST_LAST_NAME);
    assertThat(parent.getAge()).isEqualTo(TEST_AGE);
  }

  public static class Parent {

    @Embedded
    private Person person;

    public Parent() {

    }

    private Parent(Person person) {
      this.person = person;
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
    private Name name;
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
