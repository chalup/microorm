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

package org.chalup.microorm.annotations;

import org.chalup.microorm.MicroOrm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates this member should be added to ContentValues
 * returned by {@link MicroOrm#toContentValues(Object)} and filled by
 * {@link MicroOrm#fromCursor(android.database.Cursor, Class)} and
 * {@link MicroOrm#fromCursor(android.database.Cursor, Object)}.
 *
 * <p>
 * NOTE: The value you specify in this annotation must be a valid column name.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

  /**
   * @return the desired name of the column representing the field
   */
  String value();
  boolean treatNullAsDefault() default false;
  boolean readonly() default false;
}
