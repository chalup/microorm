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
 * Converts Java objects to {@link android.content.ContentValues} and from
 * {@link android.database.Cursor}.
 */
public interface TypeAdapter<T> {

  /**
   * Reads a column from cursor and converts it to a Java object. Returns the
   * converted object.
   *
   * @param c cursor containing the column
   * @param columnName name of the column containing data representing the Java
   * object
   * @return the converted Java object. May be null.
   */
  public T fromCursor(Cursor c, String columnName);

  /**
   * Converts Java object into type that can be put into {@link ContentValues}
   * and puts it into supplied {@code values} instance under {@code columnName}
   * key.
   *
   * @param values {@link ContentValues} to be filled with data from
   * {@code object}
   * @param columnName name of the target column for converted {@code object}
   * @param object an object to be converted
   */
  public void toContentValues(ContentValues values, String columnName, T object);
}
