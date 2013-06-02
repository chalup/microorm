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

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;

class EmbeddedFieldAdapter extends FieldAdapter {

  private final DaoAdapter<Object> mDaoAdapter;

  protected EmbeddedFieldAdapter(Field field, DaoAdapter<?> daoAdapter) {
    super(field);
    mDaoAdapter = ((DaoAdapter<Object>) daoAdapter);
  }

  @Override
  public void setValueFromCursor(Cursor inCursor, Object outTarget) throws IllegalArgumentException, IllegalAccessException {
    mField.set(outTarget, mDaoAdapter.fromCursor(inCursor, mDaoAdapter.createInstance()));
  }

  @Override
  public void putToContentValues(Object inObject, ContentValues outValues) throws IllegalArgumentException, IllegalAccessException {
    mDaoAdapter.toContentValues(outValues, mField.get(inObject));
  }
}
