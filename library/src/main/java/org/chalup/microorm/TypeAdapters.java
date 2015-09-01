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

package org.chalup.microorm;

import android.content.ContentValues;
import android.database.Cursor;

final class TypeAdapters {
  private TypeAdapters() {
  }

  public static class StringAdapter implements TypeAdapter<String> {
    @Override
    public String fromCursor(Cursor c, String columnName) {
      return c.getString(c.getColumnIndexOrThrow(columnName));
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, String object) {
      values.put(columnName, object);
    }
  }

  public static class ShortAdapter implements TypeAdapter<Short> {
    @Override
    public Short fromCursor(Cursor c, String columnName) {
      return c.getShort(c.getColumnIndexOrThrow(columnName));
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, Short object) {
      values.put(columnName, object);
    }
  }

  public static class IntegerAdapter implements TypeAdapter<Integer> {
    @Override
    public Integer fromCursor(Cursor c, String columnName) {
      return c.getInt(c.getColumnIndexOrThrow(columnName));
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, Integer object) {
      values.put(columnName, object);
    }
  }

  public static class LongAdapter implements TypeAdapter<Long> {
    @Override
    public Long fromCursor(Cursor c, String columnName) {
      return c.getLong(c.getColumnIndexOrThrow(columnName));
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, Long object) {
      values.put(columnName, object);
    }
  }

  public static class FloatAdapter implements TypeAdapter<Float> {
    @Override
    public Float fromCursor(Cursor c, String columnName) {
      return c.getFloat(c.getColumnIndexOrThrow(columnName));
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, Float object) {
      values.put(columnName, object);
    }
  }

  public static class DoubleAdapter implements TypeAdapter<Double> {
    @Override
    public Double fromCursor(Cursor c, String columnName) {
      return c.getDouble(c.getColumnIndexOrThrow(columnName));
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, Double object) {
      values.put(columnName, object);
    }
  }

  public static class BooleanAdapter implements TypeAdapter<Boolean> {
    @Override
    public Boolean fromCursor(Cursor c, String columnName) {
      return c.getInt(c.getColumnIndexOrThrow(columnName)) == 1;
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, Boolean object) {
      values.put(columnName, object);
    }
  }
}
