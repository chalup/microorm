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

import org.chalup.microorm.annotations.Column;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;

class ColumnFieldAdapter extends FieldAdapter {

  private final String mColumnName;
  private final TypeAdapter<?> mTypeAdapter;
  private final boolean mTreatNullAsDefault;

  ColumnFieldAdapter(Field field, TypeAdapter<?> typeAdapter) {
    super(field);
    mTypeAdapter = typeAdapter;

    Column columnAnnotation = field.getAnnotation(Column.class);
    mColumnName = columnAnnotation.value();
    mTreatNullAsDefault = columnAnnotation.treatNullAsDefault();
  }

  @Override
  public void setValueFromCursor(Cursor inCursor, Object outTarget) throws IllegalArgumentException, IllegalAccessException {
    mField.set(outTarget, mTypeAdapter.fromCursor(inCursor, mColumnName));
  }

  @SuppressWarnings("unchecked")
  @Override
  public void putToContentValues(Object inObject, ContentValues outValues) throws IllegalArgumentException, IllegalAccessException {
    Object fieldValue = mField.get(inObject);
    boolean skipColumn = mTreatNullAsDefault && fieldValue == null;
    if (!skipColumn) {
      ((TypeAdapter<Object>) mTypeAdapter).toContentValues(outValues, mColumnName, fieldValue);
    }
  }

  @Override
  public String[] getColumnNames() {
    return new String[] { mColumnName };
  }
}
