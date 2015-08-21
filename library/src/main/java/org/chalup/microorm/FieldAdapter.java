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

import java.lang.reflect.Field;

abstract class FieldAdapter {

  final Field mField;

  FieldAdapter(Field field) {
    mField = field;
  }

  public abstract void setValueFromCursor(Cursor inCursor, Object outTarget)
      throws IllegalArgumentException, IllegalAccessException;

  public void putToContentValues(Object inObject, ContentValues outValues) throws IllegalAccessException {
    Object value = inObject != null ? mField.get(inObject) : null;
    putValueToContentValues(value, outValues);
  }

  protected abstract void putValueToContentValues(Object value, ContentValues outValues);

  public abstract String[] getColumnNames();

  public abstract String[] getWritableColumnNames();
}
