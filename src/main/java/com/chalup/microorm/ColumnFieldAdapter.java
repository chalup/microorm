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

package com.chalup.microorm;

import com.chalup.microorm.annotations.Column;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;

class ColumnFieldAdapter extends FieldAdapter {

  private final String mColumnName;
  private final TypeAdapter<?> mTypeAdapter;

  ColumnFieldAdapter(Field field, TypeAdapter<?> typeAdapter) {
    super(field);
    mTypeAdapter = typeAdapter;
    mColumnName = field.getAnnotation(Column.class).value();
  }

  @Override
  public void setValueFromCursor(Cursor inCursor, Object outTarget) throws IllegalArgumentException, IllegalAccessException {
    mField.set(outTarget, mTypeAdapter.fromCursor(inCursor, mColumnName));
  }

  @SuppressWarnings("unchecked")
  @Override
  public void putToContentValues(Object inObject, ContentValues outValues) throws IllegalArgumentException, IllegalAccessException {
    ((TypeAdapter<Object>) mTypeAdapter).toContentValues(outValues, mColumnName, mField.get(inObject));
  }

  @Override
  public String[] getColumnNames() {
    return new String[] { mColumnName };
  }
}
