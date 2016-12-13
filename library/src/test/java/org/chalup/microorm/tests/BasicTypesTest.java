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

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;
import org.chalup.microorm.tests.ObjectWithNestedNonPublicObject.NonPublicNestedObject;
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
public class BasicTypesTest {

  private MicroOrm testSubject;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm();
  }

  public static class ShortDao {
    static final String SHORT_COLUMN = "SHORT_COLUMN";
    static final short TEST_SHORT = 42;

    @Column(SHORT_COLUMN)
    public short mShort;
  }

  @Test
  public void shouldUnpackShortFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(ShortDao.SHORT_COLUMN)
        .addRow(ShortDao.TEST_SHORT);

    ShortDao shortDao = testSubject.fromCursor(c, ShortDao.class);
    assertThat(shortDao.mShort).isEqualTo(ShortDao.TEST_SHORT);
  }

  @Test
  public void shouldPackShortFieldsIntoContentValues() throws Exception {
    ShortDao shortDao = new ShortDao();
    shortDao.mShort = ShortDao.TEST_SHORT;

    ContentValues values = testSubject.toContentValues(shortDao);
    assertThat(values).contains(entry(ShortDao.SHORT_COLUMN, ShortDao.TEST_SHORT));
  }

  public static class IntegerDao {
    static final String INTEGER_COLUMN = "INTEGER_COLUMN";
    static final int TEST_INT = 42;

    @Column(INTEGER_COLUMN)
    public int mInt;
  }

  @Test
  public void shouldUnpackIntFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(IntegerDao.INTEGER_COLUMN)
        .addRow(IntegerDao.TEST_INT);

    IntegerDao integerDao = testSubject.fromCursor(c, IntegerDao.class);
    assertThat(integerDao.mInt).isEqualTo(IntegerDao.TEST_INT);
  }

  @Test
  public void shouldPackIntFieldsIntoContentValues() throws Exception {
    IntegerDao integerDao = new IntegerDao();
    integerDao.mInt = IntegerDao.TEST_INT;

    ContentValues values = testSubject.toContentValues(integerDao);
    assertThat(values).contains(entry(IntegerDao.INTEGER_COLUMN, IntegerDao.TEST_INT));
  }

  public static class LongDao {
    static final String LONG_COLUMN = "LONG_COLUMN";
    static final long TEST_LONG = 42;

    @Column(LONG_COLUMN)
    public long mLong;
  }

  @Test
  public void shouldUnpackLongFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(LongDao.LONG_COLUMN)
        .addRow(LongDao.TEST_LONG);

    LongDao longDao = testSubject.fromCursor(c, LongDao.class);
    assertThat(longDao.mLong).isEqualTo(LongDao.TEST_LONG);
  }

  @Test
  public void shouldPackLongFieldsIntoContentValues() throws Exception {
    LongDao longDao = new LongDao();
    longDao.mLong = LongDao.TEST_LONG;

    ContentValues values = testSubject.toContentValues(longDao);
    assertThat(values).contains(entry(LongDao.LONG_COLUMN, LongDao.TEST_LONG));
  }

  public static class FloatDao {
    static final String FLOAT_COLUMN = "FLOAT_COLUMN";
    static final float TEST_FLOAT = 42;

    @Column(FLOAT_COLUMN)
    public float mFloat;
  }

  @Test
  public void shouldUnpackFloatFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(FloatDao.FLOAT_COLUMN)
        .addRow(FloatDao.TEST_FLOAT);

    FloatDao floatDao = testSubject.fromCursor(c, FloatDao.class);
    assertThat(floatDao.mFloat).isEqualTo(FloatDao.TEST_FLOAT);
  }

  @Test
  public void shouldPackFloatFieldsIntoContentValues() throws Exception {
    FloatDao floatDao = new FloatDao();
    floatDao.mFloat = FloatDao.TEST_FLOAT;

    ContentValues values = testSubject.toContentValues(floatDao);
    assertThat(values).contains(entry(FloatDao.FLOAT_COLUMN, FloatDao.TEST_FLOAT));
  }

  public static class DoubleDao {
    static final String DOUBLE_COLUMN = "DOUBLE_COLUMN";
    static final double TEST_DOUBLE = 42;

    @Column(DOUBLE_COLUMN)
    public double mDouble;
  }

  @Test
  public void shouldUnpackDoubleFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(DoubleDao.DOUBLE_COLUMN)
        .addRow(DoubleDao.TEST_DOUBLE);

    DoubleDao doubleDao = testSubject.fromCursor(c, DoubleDao.class);
    assertThat(doubleDao.mDouble).isEqualTo(DoubleDao.TEST_DOUBLE);
  }

  @Test
  public void shouldPackDoubleFieldsIntoContentValues() throws Exception {
    DoubleDao doubleDao = new DoubleDao();
    doubleDao.mDouble = DoubleDao.TEST_DOUBLE;

    ContentValues values = testSubject.toContentValues(doubleDao);
    assertThat(values).contains(entry(DoubleDao.DOUBLE_COLUMN, DoubleDao.TEST_DOUBLE));
  }

  public static class BooleanDao {
    static final String TRUE_BOOLEAN_COLUMN = "TRUE_BOOLEAN_COLUMN";
    static final String FALSE_BOOLEAN_COLUMN = "FALSE_BOOLEAN_COLUMN";

    @Column(TRUE_BOOLEAN_COLUMN)
    public boolean mTrueBoolean;

    @Column(FALSE_BOOLEAN_COLUMN)
    public boolean mFalseBoolean;
  }

  @Test
  public void shouldUnpackBooleanFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(BooleanDao.TRUE_BOOLEAN_COLUMN, BooleanDao.FALSE_BOOLEAN_COLUMN)
        .addRow(1, 0);

    BooleanDao booleanDao = testSubject.fromCursor(c, BooleanDao.class);
    assertThat(booleanDao.mTrueBoolean).isTrue();
    assertThat(booleanDao.mFalseBoolean).isFalse();
  }

  @Test
  public void shouldPackBooleanFieldsIntoContentValues() throws Exception {
    BooleanDao booleanDao = new BooleanDao();
    booleanDao.mTrueBoolean = true;

    ContentValues values = testSubject.toContentValues(booleanDao);
    assertThat(values).contains(entry(BooleanDao.TRUE_BOOLEAN_COLUMN, true));
    assertThat(values).contains(entry(BooleanDao.FALSE_BOOLEAN_COLUMN, false));
  }

  public static class BoxedShortDao {
    static final String SHORT_COLUMN = "SHORT_COLUMN";
    static final String NULL_SHORT_COLUMN = "NULL_SHORT_COLUMN";

    static final short TEST_SHORT = 42;

    @Column(SHORT_COLUMN)
    public Short mShort;

    @Column(NULL_SHORT_COLUMN)
    public Short mNullShort;
  }

  @Test
  public void shouldUnpackBoxedShortFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(BoxedShortDao.SHORT_COLUMN, BoxedShortDao.NULL_SHORT_COLUMN)
        .addRow(BoxedShortDao.TEST_SHORT, null);

    BoxedShortDao boxedShortDao = testSubject.fromCursor(c, BoxedShortDao.class);
    assertThat(boxedShortDao.mShort).isEqualTo(BoxedShortDao.TEST_SHORT);
    assertThat(boxedShortDao.mNullShort).isNull();
  }

  @Test
  public void shouldPackBoxedShortFieldsIntoContentValues() throws Exception {
    BoxedShortDao boxedShortDao = new BoxedShortDao();
    boxedShortDao.mShort = BoxedShortDao.TEST_SHORT;

    ContentValues values = testSubject.toContentValues(boxedShortDao);
    assertThat(values).contains(entry(BoxedShortDao.SHORT_COLUMN, BoxedShortDao.TEST_SHORT));
    assertThat(values).contains(entry(BoxedShortDao.NULL_SHORT_COLUMN, null));
  }

  public static class BoxedIntegerDao {
    static final String INTEGER_COLUMN = "INTEGER_COLUMN";
    static final String NULL_INTEGER_COLUMN = "NULL_INTEGER_COLUMN";
    static final int TEST_INT = 42;

    @Column(INTEGER_COLUMN)
    public Integer mInt;

    @Column(NULL_INTEGER_COLUMN)
    public Integer mNullInteger;
  }

  @Test
  public void shouldUnpackBoxedIntFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(BoxedIntegerDao.INTEGER_COLUMN, BoxedIntegerDao.NULL_INTEGER_COLUMN)
        .addRow(BoxedIntegerDao.TEST_INT, null);

    BoxedIntegerDao boxedIntegerDao = testSubject.fromCursor(c, BoxedIntegerDao.class);
    assertThat(boxedIntegerDao.mInt).isEqualTo(BoxedIntegerDao.TEST_INT);
    assertThat(boxedIntegerDao.mNullInteger).isNull();
  }

  @Test
  public void shouldPackBoxedIntFieldsIntoContentValues() throws Exception {
    BoxedIntegerDao boxedIntegerDao = new BoxedIntegerDao();
    boxedIntegerDao.mInt = BoxedIntegerDao.TEST_INT;

    ContentValues values = testSubject.toContentValues(boxedIntegerDao);
    assertThat(values).contains(entry(BoxedIntegerDao.INTEGER_COLUMN, BoxedIntegerDao.TEST_INT));
    assertThat(values).contains(entry(BoxedIntegerDao.NULL_INTEGER_COLUMN, null));
  }

  public static class BoxedLongDao {
    static final String LONG_COLUMN = "LONG_COLUMN";
    static final String NULL_LONG_COLUMN = "NULL_LONG_COLUMN";
    static final long TEST_LONG = 42;

    @Column(LONG_COLUMN)
    public Long mLong;

    @Column(NULL_LONG_COLUMN)
    public Long mNullLong;
  }

  @Test
  public void shouldUnpackBoxedLongFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(BoxedLongDao.LONG_COLUMN, BoxedLongDao.NULL_LONG_COLUMN)
        .addRow(BoxedLongDao.TEST_LONG, null);

    BoxedLongDao boxedLongDao = testSubject.fromCursor(c, BoxedLongDao.class);
    assertThat(boxedLongDao.mLong).isEqualTo(BoxedLongDao.TEST_LONG);
    assertThat(boxedLongDao.mNullLong).isNull();
  }

  @Test
  public void shouldPackBoxedLongFieldsIntoContentValues() throws Exception {
    BoxedLongDao boxedLongDao = new BoxedLongDao();
    boxedLongDao.mLong = BoxedLongDao.TEST_LONG;

    ContentValues values = testSubject.toContentValues(boxedLongDao);
    assertThat(values).contains(entry(BoxedLongDao.LONG_COLUMN, BoxedLongDao.TEST_LONG));
    assertThat(values).contains(entry(BoxedLongDao.NULL_LONG_COLUMN, null));
  }

  public static class BoxedFloatDao {
    static final String FLOAT_COLUMN = "FLOAT_COLUMN";
    static final String NULL_FLOAT_COLUMN = "NULL_FLOAT_COLUMN";

    static final float TEST_FLOAT = 42;

    @Column(FLOAT_COLUMN)
    public Float mFloat;

    @Column(NULL_FLOAT_COLUMN)
    public Float mNullFloat;
  }

  @Test
  public void shouldUnpackBoxedFloatFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(BoxedFloatDao.FLOAT_COLUMN, BoxedFloatDao.NULL_FLOAT_COLUMN)
        .addRow(BoxedFloatDao.TEST_FLOAT, null);

    BoxedFloatDao boxedFloatDao = testSubject.fromCursor(c, BoxedFloatDao.class);
    assertThat(boxedFloatDao.mFloat).isEqualTo(BoxedFloatDao.TEST_FLOAT);
    assertThat(boxedFloatDao.mNullFloat).isNull();
  }

  @Test
  public void shouldPackBoxedFloatFieldsIntoContentValues() throws Exception {
    BoxedFloatDao boxedFloatDao = new BoxedFloatDao();
    boxedFloatDao.mFloat = BoxedFloatDao.TEST_FLOAT;

    ContentValues values = testSubject.toContentValues(boxedFloatDao);
    assertThat(values).contains(entry(BoxedFloatDao.FLOAT_COLUMN, BoxedFloatDao.TEST_FLOAT));
    assertThat(values).contains(entry(BoxedFloatDao.NULL_FLOAT_COLUMN, null));
  }

  public static class BoxedDoubleDao {
    static final String DOUBLE_COLUMN = "DOUBLE_COLUMN";
    static final String NULL_DOUBLE_COLUMN = "NULL_DOUBLE_COLUMN";
    static final double TEST_DOUBLE = 42;

    @Column(DOUBLE_COLUMN)
    public Double mDouble;

    @Column(NULL_DOUBLE_COLUMN)
    public Double mNullDouble;
  }

  @Test
  public void shouldUnpackBoxedDoubleFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(BoxedDoubleDao.DOUBLE_COLUMN, BoxedDoubleDao.NULL_DOUBLE_COLUMN)
        .addRow(BoxedDoubleDao.TEST_DOUBLE, null);

    BoxedDoubleDao boxedDoubleDao = testSubject.fromCursor(c, BoxedDoubleDao.class);
    assertThat(boxedDoubleDao.mDouble).isEqualTo(BoxedDoubleDao.TEST_DOUBLE);
    assertThat(boxedDoubleDao.mNullDouble).isNull();
  }

  @Test
  public void shouldPackBoxedDoubleFieldsIntoContentValues() throws Exception {
    BoxedDoubleDao boxedDoubleDao = new BoxedDoubleDao();
    boxedDoubleDao.mDouble = BoxedDoubleDao.TEST_DOUBLE;

    ContentValues values = testSubject.toContentValues(boxedDoubleDao);
    assertThat(values).contains(entry(BoxedDoubleDao.DOUBLE_COLUMN, BoxedDoubleDao.TEST_DOUBLE));
    assertThat(values).contains(entry(BoxedDoubleDao.NULL_DOUBLE_COLUMN, null));
  }

  public static class BoxedBooleanDao {
    static final String TRUE_BOOLEAN_COLUMN = "TRUE_BOOLEAN_COLUMN";
    static final String FALSE_BOOLEAN_COLUMN = "FALSE_BOOLEAN_COLUMN";
    static final String NULL_BOOLEAN_COLUMN = "NULL_BOOLEAN_COLUMN";

    @Column(TRUE_BOOLEAN_COLUMN)
    public Boolean mTrueBoolean;

    @Column(FALSE_BOOLEAN_COLUMN)
    public Boolean mFalseBoolean;

    @Column(NULL_BOOLEAN_COLUMN)
    public Boolean mNullBoolean;
  }

  @Test
  public void shouldUnpackBoxedBooleanFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(BoxedBooleanDao.TRUE_BOOLEAN_COLUMN, BoxedBooleanDao.FALSE_BOOLEAN_COLUMN, BoxedBooleanDao.NULL_BOOLEAN_COLUMN)
        .addRow(1, 0, null);

    BoxedBooleanDao boxedBooleanDao = testSubject.fromCursor(c, BoxedBooleanDao.class);
    assertThat(boxedBooleanDao.mTrueBoolean).isTrue();
    assertThat(boxedBooleanDao.mFalseBoolean).isFalse();
    assertThat(boxedBooleanDao.mNullBoolean).isNull();
  }

  @Test
  public void shouldPackBoxedBooleanFieldsIntoContentValues() throws Exception {
    BoxedBooleanDao boxedBooleanDao = new BoxedBooleanDao();
    boxedBooleanDao.mTrueBoolean = Boolean.TRUE;
    boxedBooleanDao.mFalseBoolean = Boolean.FALSE;

    ContentValues values = testSubject.toContentValues(boxedBooleanDao);
    assertThat(values).contains(entry(BoxedBooleanDao.TRUE_BOOLEAN_COLUMN, true));
    assertThat(values).contains(entry(BoxedBooleanDao.FALSE_BOOLEAN_COLUMN, false));
    assertThat(values).contains(entry(BoxedBooleanDao.NULL_BOOLEAN_COLUMN, null));
  }

  public static class StringDao {
    static final String STRING_COLUMN = "STRING_COLUMN";
    static final String NULL_STRING_COLUMN = "NULL_STRING_COLUMN";
    static final String TEST_STRING = "TEST_STRING";

    @Column(STRING_COLUMN)
    public String mString;

    @Column(NULL_STRING_COLUMN)
    public String mNullString;
  }

  @Test
  public void shouldUnpackStringFieldsFromCursor() throws Exception {
    Cursor c = TestCursorBuilder
        .cursor(StringDao.STRING_COLUMN, StringDao.NULL_STRING_COLUMN)
        .addRow(StringDao.TEST_STRING, null);

    StringDao stringDao = testSubject.fromCursor(c, StringDao.class);
    assertThat(stringDao.mString).isEqualTo(StringDao.TEST_STRING);
    assertThat(stringDao.mNullString).isNull();
  }

  @Test
  public void shouldPackStringFieldsIntoContentValues() throws Exception {
    StringDao stringDao = new StringDao();

    stringDao.mString = StringDao.TEST_STRING;

    ContentValues values = testSubject.toContentValues(stringDao);
    assertThat(values).contains(entry(StringDao.STRING_COLUMN, StringDao.TEST_STRING));
    assertThat(values).contains(entry(StringDao.NULL_STRING_COLUMN, null));
  }

  public static class ObjectWithNonDefaultConstructor {
    public ObjectWithNonDefaultConstructor(long id) {
      this.id = id;
    }

    @Column(BaseColumns._ID)
    long id;
  }

  @Test
  public void shouldHandleObjectWithNonDefaultConstructorsOnly() throws Exception {
    Cursor c = TestCursorBuilder.cursor(BaseColumns._ID).addRow(1500L);

    ObjectWithNonDefaultConstructor o = testSubject.fromCursor(c, ObjectWithNonDefaultConstructor.class);
    assertThat(o.id).isEqualTo(1500);
  }

  @Test
  public void shouldHandleObjectWithNonPublicDefaultConstructor() throws Exception {
    Cursor c = TestCursorBuilder.cursor(BaseColumns._ID).addRow(1500L);

    ObjectWithNotAccessibleConstructor o = testSubject.fromCursor(c, ObjectWithNotAccessibleConstructor.class);
    assertThat(o.id).isEqualTo(1500);
  }

  @Test
  public void shouldHandleNonPublicNestedClasses() throws Exception {
    Cursor c = TestCursorBuilder.cursor(BaseColumns._ID).addRow(1500L);

    NonPublicNestedObject o = ObjectWithNestedNonPublicObject.test(testSubject, c);
    assertThat(o.id).isEqualTo(1500);
  }

  @Test
  public void shouldHandleKotlinDataClass() throws Exception {
    Cursor c = TestCursorBuilder.cursor(BaseColumns._ID).addRow(1500L);

    KotlinDataClass o = testSubject.fromCursor(c, KotlinDataClass.class);
    assertThat(o.getId()).isEqualTo(1500);
  }
}
