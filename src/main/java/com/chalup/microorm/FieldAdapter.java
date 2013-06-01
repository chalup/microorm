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
import com.google.common.collect.ImmutableList;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.List;

abstract class FieldAdapter {

  protected final List<Field> mFields;
  protected final Field mField;
  protected final String mColumnName;

  protected FieldAdapter(Field field, List<Field> parentFields) {
    mFields = ImmutableList.copyOf(parentFields);
    mField = field;
    mColumnName = mField.getAnnotation(Column.class).value();
  }

  protected Object getObjectToManipulateWith(Object rootObject) {
    try {
      for (Field field : mFields) {
        rootObject = field.get(rootObject);
      }
      return rootObject;
    } catch (IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }

  public abstract void setValueFromCursor(Cursor inCursor, Object outTarget)
      throws IllegalArgumentException, IllegalAccessException;

  public abstract void putToContentValues(Object inObject, ContentValues outValues)
      throws IllegalArgumentException, IllegalAccessException;
}
