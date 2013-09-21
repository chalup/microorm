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
import static org.mockito.Mockito.*;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(ShortDao.SHORT_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(ShortDao.SHORT_COLUMN)).thenReturn(0);
    when(c.getShort(0)).thenReturn(ShortDao.TEST_SHORT);

    ShortDao shortDao = testSubject.fromCursor(c, ShortDao.class);
    assertThat(shortDao.mShort).isEqualTo(ShortDao.TEST_SHORT);
  }

  @Test
  public void shouldPackShortFieldsIntoContentValues() throws Exception {
    ShortDao shortDao = new ShortDao();
    shortDao.mShort = ShortDao.TEST_SHORT;

    ContentValues values = testSubject.toContentValues(shortDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsShort(ShortDao.SHORT_COLUMN)).isEqualTo(ShortDao.TEST_SHORT);
  }

  public static class IntegerDao {
    static final String INTEGER_COLUMN = "INTEGER_COLUMN";
    static final int TEST_INT = 42;

    @Column(INTEGER_COLUMN)
    public int mInt;
  }

  @Test
  public void shouldUnpackIntFieldsFromCursor() throws Exception {
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(IntegerDao.INTEGER_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(IntegerDao.INTEGER_COLUMN)).thenReturn(0);
    when(c.getInt(0)).thenReturn(IntegerDao.TEST_INT);

    IntegerDao integerDao = testSubject.fromCursor(c, IntegerDao.class);
    assertThat(integerDao.mInt).isEqualTo(IntegerDao.TEST_INT);
  }

  @Test
  public void shouldPackIntFieldsIntoContentValues() throws Exception {
    IntegerDao integerDao = new IntegerDao();
    integerDao.mInt = IntegerDao.TEST_INT;

    ContentValues values = testSubject.toContentValues(integerDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsInteger(IntegerDao.INTEGER_COLUMN)).isEqualTo(IntegerDao.TEST_INT);
  }

  public static class LongDao {
    static final String LONG_COLUMN = "LONG_COLUMN";
    static final long TEST_LONG = 42;

    @Column(LONG_COLUMN)
    public long mLong;
  }

  @Test
  public void shouldUnpackLongFieldsFromCursor() throws Exception {
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(LongDao.LONG_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(LongDao.LONG_COLUMN)).thenReturn(0);
    when(c.getLong(0)).thenReturn(LongDao.TEST_LONG);

    LongDao longDao = testSubject.fromCursor(c, LongDao.class);
    assertThat(longDao.mLong).isEqualTo(LongDao.TEST_LONG);
  }

  @Test
  public void shouldPackLongFieldsIntoContentValues() throws Exception {
    LongDao longDao = new LongDao();
    longDao.mLong = LongDao.TEST_LONG;

    ContentValues values = testSubject.toContentValues(longDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsLong(LongDao.LONG_COLUMN)).isEqualTo(LongDao.TEST_LONG);
  }

  public static class FloatDao {
    static final String FLOAT_COLUMN = "FLOAT_COLUMN";
    static final float TEST_FLOAT = 42;

    @Column(FLOAT_COLUMN)
    public float mFloat;
  }

  @Test
  public void shouldUnpackFloatFieldsFromCursor() throws Exception {
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(FloatDao.FLOAT_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(FloatDao.FLOAT_COLUMN)).thenReturn(0);
    when(c.getFloat(0)).thenReturn(FloatDao.TEST_FLOAT);

    FloatDao floatDao = testSubject.fromCursor(c, FloatDao.class);
    assertThat(floatDao.mFloat).isEqualTo(FloatDao.TEST_FLOAT);
  }

  @Test
  public void shouldPackFloatFieldsIntoContentValues() throws Exception {
    FloatDao floatDao = new FloatDao();
    floatDao.mFloat = FloatDao.TEST_FLOAT;

    ContentValues values = testSubject.toContentValues(floatDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsFloat(FloatDao.FLOAT_COLUMN)).isEqualTo(FloatDao.TEST_FLOAT);
  }

  public static class DoubleDao {
    static final String DOUBLE_COLUMN = "DOUBLE_COLUMN";
    static final double TEST_DOUBLE = 42;

    @Column(DOUBLE_COLUMN)
    public double mDouble;
  }

  @Test
  public void shouldUnpackDoubleFieldsFromCursor() throws Exception {
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(DoubleDao.DOUBLE_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(DoubleDao.DOUBLE_COLUMN)).thenReturn(0);
    when(c.getDouble(0)).thenReturn(DoubleDao.TEST_DOUBLE);

    DoubleDao doubleDao = testSubject.fromCursor(c, DoubleDao.class);
    assertThat(doubleDao.mDouble).isEqualTo(DoubleDao.TEST_DOUBLE);
  }

  @Test
  public void shouldPackDoubleFieldsIntoContentValues() throws Exception {
    DoubleDao doubleDao = new DoubleDao();
    doubleDao.mDouble = DoubleDao.TEST_DOUBLE;

    ContentValues values = testSubject.toContentValues(doubleDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsDouble(DoubleDao.DOUBLE_COLUMN)).isEqualTo(DoubleDao.TEST_DOUBLE);
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(BooleanDao.TRUE_BOOLEAN_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(BooleanDao.TRUE_BOOLEAN_COLUMN)).thenReturn(0);
    when(c.getInt(0)).thenReturn(1);

    when(c.getColumnIndex(BooleanDao.FALSE_BOOLEAN_COLUMN)).thenReturn(1);
    when(c.getColumnIndexOrThrow(BooleanDao.FALSE_BOOLEAN_COLUMN)).thenReturn(1);
    when(c.getInt(1)).thenReturn(0);

    BooleanDao booleanDao = testSubject.fromCursor(c, BooleanDao.class);
    assertThat(booleanDao.mTrueBoolean).isTrue();
    assertThat(booleanDao.mFalseBoolean).isFalse();
  }

  @Test
  public void shouldPackBooleanFieldsIntoContentValues() throws Exception {
    BooleanDao booleanDao = new BooleanDao();
    booleanDao.mTrueBoolean = true;

    ContentValues values = testSubject.toContentValues(booleanDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsBoolean(BooleanDao.TRUE_BOOLEAN_COLUMN)).isTrue();
    assertThat(shadowValues.getAsBoolean(BooleanDao.FALSE_BOOLEAN_COLUMN)).isFalse();
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(BoxedShortDao.SHORT_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(BoxedShortDao.SHORT_COLUMN)).thenReturn(0);
    when(c.isNull(0)).thenReturn(Boolean.FALSE);
    when(c.getShort(0)).thenReturn(BoxedShortDao.TEST_SHORT);

    when(c.getColumnIndex(BoxedShortDao.NULL_SHORT_COLUMN)).thenReturn(1);
    when(c.getColumnIndexOrThrow(BoxedShortDao.NULL_SHORT_COLUMN)).thenReturn(1);
    when(c.isNull(1)).thenReturn(Boolean.TRUE);

    BoxedShortDao boxedShortDao = testSubject.fromCursor(c, BoxedShortDao.class);
    assertThat(boxedShortDao.mShort).isEqualTo(BoxedShortDao.TEST_SHORT);
    assertThat(boxedShortDao.mNullShort).isNull();
  }

  @Test
  public void shouldPackBoxedShortFieldsIntoContentValues() throws Exception {
    BoxedShortDao boxedShortDao = new BoxedShortDao();
    boxedShortDao.mShort = BoxedShortDao.TEST_SHORT;

    ContentValues values = testSubject.toContentValues(boxedShortDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsShort(BoxedShortDao.SHORT_COLUMN)).isEqualTo(BoxedShortDao.TEST_SHORT);
    assertThat(shadowValues.getAsShort(BoxedShortDao.NULL_SHORT_COLUMN)).isNull();
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(BoxedIntegerDao.INTEGER_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(BoxedIntegerDao.INTEGER_COLUMN)).thenReturn(0);
    when(c.isNull(0)).thenReturn(Boolean.FALSE);
    when(c.getInt(0)).thenReturn(BoxedIntegerDao.TEST_INT);

    when(c.getColumnIndex(BoxedIntegerDao.NULL_INTEGER_COLUMN)).thenReturn(1);
    when(c.getColumnIndexOrThrow(BoxedIntegerDao.NULL_INTEGER_COLUMN)).thenReturn(1);
    when(c.isNull(1)).thenReturn(Boolean.TRUE);

    BoxedIntegerDao boxedIntegerDao = testSubject.fromCursor(c, BoxedIntegerDao.class);
    assertThat(boxedIntegerDao.mInt).isEqualTo(BoxedIntegerDao.TEST_INT);
    assertThat(boxedIntegerDao.mNullInteger).isNull();
  }

  @Test
  public void shouldPackBoxedIntFieldsIntoContentValues() throws Exception {
    BoxedIntegerDao boxedIntegerDao = new BoxedIntegerDao();
    boxedIntegerDao.mInt = BoxedIntegerDao.TEST_INT;

    ContentValues values = testSubject.toContentValues(boxedIntegerDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsInteger(BoxedIntegerDao.INTEGER_COLUMN)).isEqualTo(BoxedIntegerDao.TEST_INT);
    assertThat(shadowValues.getAsInteger(BoxedIntegerDao.NULL_INTEGER_COLUMN)).isNull();
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(BoxedLongDao.LONG_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(BoxedLongDao.LONG_COLUMN)).thenReturn(0);
    when(c.isNull(0)).thenReturn(Boolean.FALSE);
    when(c.getLong(0)).thenReturn(BoxedLongDao.TEST_LONG);

    when(c.getColumnIndex(BoxedLongDao.NULL_LONG_COLUMN)).thenReturn(1);
    when(c.getColumnIndexOrThrow(BoxedLongDao.NULL_LONG_COLUMN)).thenReturn(1);
    when(c.isNull(1)).thenReturn(Boolean.TRUE);

    BoxedLongDao boxedLongDao = testSubject.fromCursor(c, BoxedLongDao.class);
    assertThat(boxedLongDao.mLong).isEqualTo(BoxedLongDao.TEST_LONG);
    assertThat(boxedLongDao.mNullLong).isNull();
  }

  @Test
  public void shouldPackBoxedLongFieldsIntoContentValues() throws Exception {
    BoxedLongDao boxedLongDao = new BoxedLongDao();
    boxedLongDao.mLong = BoxedLongDao.TEST_LONG;

    ContentValues values = testSubject.toContentValues(boxedLongDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsLong(BoxedLongDao.LONG_COLUMN)).isEqualTo(BoxedLongDao.TEST_LONG);
    assertThat(shadowValues.getAsLong(BoxedLongDao.NULL_LONG_COLUMN)).isNull();
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(BoxedFloatDao.FLOAT_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(BoxedFloatDao.FLOAT_COLUMN)).thenReturn(0);
    when(c.isNull(0)).thenReturn(Boolean.FALSE);
    when(c.getFloat(0)).thenReturn(BoxedFloatDao.TEST_FLOAT);

    when(c.getColumnIndex(BoxedFloatDao.NULL_FLOAT_COLUMN)).thenReturn(1);
    when(c.getColumnIndexOrThrow(BoxedFloatDao.NULL_FLOAT_COLUMN)).thenReturn(1);
    when(c.isNull(1)).thenReturn(Boolean.TRUE);

    BoxedFloatDao boxedFloatDao = testSubject.fromCursor(c, BoxedFloatDao.class);
    assertThat(boxedFloatDao.mFloat).isEqualTo(BoxedFloatDao.TEST_FLOAT);
    assertThat(boxedFloatDao.mNullFloat).isNull();
  }

  @Test
  public void shouldPackBoxedFloatFieldsIntoContentValues() throws Exception {
    BoxedFloatDao boxedFloatDao = new BoxedFloatDao();
    boxedFloatDao.mFloat = BoxedFloatDao.TEST_FLOAT;

    ContentValues values = testSubject.toContentValues(boxedFloatDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsFloat(BoxedFloatDao.FLOAT_COLUMN)).isEqualTo(BoxedFloatDao.TEST_FLOAT);
    assertThat(shadowValues.getAsFloat(BoxedFloatDao.NULL_FLOAT_COLUMN)).isNull();
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(BoxedDoubleDao.DOUBLE_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(BoxedDoubleDao.DOUBLE_COLUMN)).thenReturn(0);
    when(c.isNull(0)).thenReturn(Boolean.FALSE);
    when(c.getDouble(0)).thenReturn(BoxedDoubleDao.TEST_DOUBLE);

    when(c.getColumnIndex(BoxedDoubleDao.NULL_DOUBLE_COLUMN)).thenReturn(1);
    when(c.getColumnIndexOrThrow(BoxedDoubleDao.NULL_DOUBLE_COLUMN)).thenReturn(1);
    when(c.isNull(1)).thenReturn(Boolean.TRUE);

    BoxedDoubleDao boxedDoubleDao = testSubject.fromCursor(c, BoxedDoubleDao.class);
    assertThat(boxedDoubleDao.mDouble).isEqualTo(BoxedDoubleDao.TEST_DOUBLE);
    assertThat(boxedDoubleDao.mNullDouble).isNull();
  }

  @Test
  public void shouldPackBoxedDoubleFieldsIntoContentValues() throws Exception {
    BoxedDoubleDao boxedDoubleDao = new BoxedDoubleDao();
    boxedDoubleDao.mDouble = BoxedDoubleDao.TEST_DOUBLE;

    ContentValues values = testSubject.toContentValues(boxedDoubleDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsDouble(BoxedDoubleDao.DOUBLE_COLUMN)).isEqualTo(BoxedDoubleDao.TEST_DOUBLE);
    assertThat(shadowValues.getAsDouble(BoxedDoubleDao.NULL_DOUBLE_COLUMN)).isNull();
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(BoxedBooleanDao.TRUE_BOOLEAN_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(BoxedBooleanDao.TRUE_BOOLEAN_COLUMN)).thenReturn(0);
    when(c.isNull(0)).thenReturn(Boolean.FALSE);
    when(c.getInt(0)).thenReturn(1);

    when(c.getColumnIndex(BoxedBooleanDao.FALSE_BOOLEAN_COLUMN)).thenReturn(1);
    when(c.getColumnIndexOrThrow(BoxedBooleanDao.FALSE_BOOLEAN_COLUMN)).thenReturn(1);
    when(c.isNull(1)).thenReturn(Boolean.FALSE);
    when(c.getInt(1)).thenReturn(0);

    when(c.getColumnIndex(BoxedBooleanDao.NULL_BOOLEAN_COLUMN)).thenReturn(2);
    when(c.getColumnIndexOrThrow(BoxedBooleanDao.NULL_BOOLEAN_COLUMN)).thenReturn(2);
    when(c.isNull(2)).thenReturn(Boolean.TRUE);

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
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsBoolean(BoxedBooleanDao.TRUE_BOOLEAN_COLUMN)).isTrue();
    assertThat(shadowValues.getAsBoolean(BoxedBooleanDao.FALSE_BOOLEAN_COLUMN)).isFalse();
    assertThat(shadowValues.getAsBoolean(BoxedBooleanDao.NULL_BOOLEAN_COLUMN)).isNull();
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
    Cursor c = mock(Cursor.class);

    when(c.getColumnIndex(StringDao.STRING_COLUMN)).thenReturn(0);
    when(c.getColumnIndexOrThrow(StringDao.STRING_COLUMN)).thenReturn(0);
    when(c.isNull(0)).thenReturn(Boolean.FALSE);
    when(c.getString(0)).thenReturn(StringDao.TEST_STRING);

    when(c.getColumnIndex(StringDao.NULL_STRING_COLUMN)).thenReturn(1);
    when(c.getColumnIndexOrThrow(StringDao.NULL_STRING_COLUMN)).thenReturn(1);
    when(c.isNull(1)).thenReturn(Boolean.TRUE);

    StringDao stringDao = testSubject.fromCursor(c, StringDao.class);
    assertThat(stringDao.mString).isEqualTo(StringDao.TEST_STRING);
    assertThat(stringDao.mNullString).isNull();
  }

  @Test
  public void shouldPackStringFieldsIntoContentValues() throws Exception {
    StringDao stringDao = new StringDao();

    stringDao.mString = StringDao.TEST_STRING;

    ContentValues values = testSubject.toContentValues(stringDao);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsString(StringDao.STRING_COLUMN)).isEqualTo(StringDao.TEST_STRING);
    assertThat(shadowValues.getAsString(StringDao.NULL_STRING_COLUMN)).isNull();
  }
}
