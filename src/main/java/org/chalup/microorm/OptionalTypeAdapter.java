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

/**
 * Wrapper for {@link TypeAdapter}. Returns null from
 * {@link #fromCursor(android.database.Cursor, String)} if the data in the
 * database column is null, otherwise calls the wrapped adapter.
 */
public class OptionalTypeAdapter<T> implements TypeAdapter<T> {

  private final TypeAdapter<T> mWrappedAdapter;

  public OptionalTypeAdapter(TypeAdapter<T> wrappedAdapter) {
    mWrappedAdapter = wrappedAdapter;
  }

  @Override
  public T fromCursor(Cursor c, String columnName) {
    return c.isNull(c.getColumnIndexOrThrow(columnName))
        ? null
        : mWrappedAdapter.fromCursor(c, columnName);
  }

  @Override
  public void toContentValues(ContentValues values, String columnName, T object) {
    if (object != null) {
      mWrappedAdapter.toContentValues(values, columnName, object);
    } else {
      values.putNull(columnName);
    }
  }
}
