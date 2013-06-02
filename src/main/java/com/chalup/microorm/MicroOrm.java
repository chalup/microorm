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
import com.chalup.microorm.annotations.Embedded;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MicroOrm {
  /**
   * Creates an object of the specified type from the current row in
   * {@link Cursor}.
   *
   * @param <T> the type of the desired object
   * @param c a valid {@link Cursor}
   * @param klass The {@link Class} of the desired object
   * @return an object of type T created from the current row in {@link Cursor}
   */
  public <T> T fromCursor(Cursor c, Class<T> klass) {
    DaoAdapter<T> adapter = getAdapter(klass);
    return adapter.fromCursor(c, adapter.createInstance());
  }

  /**
   * Fills the field in the provided object with data from the current row in
   * {@link Cursor}.
   *
   * @param <T> the type of the provided object
   * @param c a valid {@link Cursor}
   * @param object the instance to be filled with data
   * @return the same object for easy chaining
   */
  @SuppressWarnings("unchecked")
  public <T> T fromCursor(Cursor c, T object) {
    return ((DaoAdapter<T>) getAdapter(object.getClass())).fromCursor(c, object);
  }

  /**
   * Creates the {@link ContentValues} from the provided object.
   *
   * @param <T> the type of the provided object
   * @param object the object to be converted into {@link ContentValues}
   * @return the {@link ContentValues} created from the provided object
   */
  @SuppressWarnings("unchecked")
  public <T> ContentValues toContentValues(T object) {
    return ((DaoAdapter<T>) getAdapter(object.getClass())).toContentValues(new ContentValues(), object);
  }

  /**
   * Convenience method for converting the whole {@link Cursor} into
   * {@link Collection} of objects of specified type.
   *
   * @param <T> the type of the provided object
   * @param c a valid {@link Cursor}; the provided {@link Cursor} will not be closed
   * @param klass The {@link Class} of the desired object
   * @return the {@link Collection} of object of type T created from the entire
   *         {@link Cursor}
   */
  public <T> Collection<T> collectionFromCursor(Cursor c, Class<T> klass) {
    Collection<T> result = Lists.newArrayList();

    if (c != null && c.moveToFirst()) {
      DaoAdapter<T> adapter = getAdapter(klass);
      do {
        result.add(adapter.fromCursor(c, adapter.createInstance()));
      } while (c.moveToNext());
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private <T> DaoAdapter<T> getAdapter(Class<T> klass) {
    DaoAdapter<?> cached = mDaoAdapterCache.get(klass);
    if (cached != null) {
      return (DaoAdapter<T>) cached;
    }

    DaoAdapter<T> adapter = buildDaoAdapter(klass);
    mDaoAdapterCache.put(klass, adapter);
    return adapter;
  }

  private <T> DaoAdapter<T> buildDaoAdapter(Class<T> klass) {
    return new ReflectiveDaoAdapter<T>(klass, getFieldAdaptersForClass(klass, Lists.<Field> newArrayList()));
  }

  private List<FieldAdapter> getFieldAdaptersForClass(Class<?> klass, List<Field> parentFields) {
    List<FieldAdapter> fieldAdapters = Lists.newArrayList();

    for (Field field : Fields.allFieldsIncludingPrivateAndSuper(klass)) {
      field.setAccessible(true);
      Column columnAnnotation = field.getAnnotation(Column.class);
      if (columnAnnotation != null) {
        fieldAdapters.add(FIELD_ADAPTER_FACTORIES.get(field.getType()).buildAdapter(field, parentFields));
      }
      Embedded embeddedAnnotation = field.getAnnotation(Embedded.class);
      if (embeddedAnnotation != null) {
        List<Field> newParentFields = Lists.newArrayList(parentFields);
        newParentFields.add(field);
        fieldAdapters.addAll(getFieldAdaptersForClass(field.getType(), newParentFields));
      }
    }

    return fieldAdapters;
  }

  private static Map<Class<?>, FieldAdapterFactory> FIELD_ADAPTER_FACTORIES;

  static {
    Map<Class<?>, FieldAdapterFactory> factories = Maps.newHashMap();

    factories.put(short.class, FieldAdapters.SHORT_FACTORY);
    factories.put(int.class, FieldAdapters.INT_FACTORY);
    factories.put(long.class, FieldAdapters.LONG_FACTORY);
    factories.put(boolean.class, FieldAdapters.BOOLEAN_FACTORY);
    factories.put(float.class, FieldAdapters.FLOAT_FACTORY);
    factories.put(double.class, FieldAdapters.DOUBLE_FACTORY);

    factories.put(Short.class, FieldAdapters.BOXED_SHORT_FACTORY);
    factories.put(Integer.class, FieldAdapters.BOXED_INT_FACTORY);
    factories.put(Long.class, FieldAdapters.BOXED_LONG_FACTORY);
    factories.put(Boolean.class, FieldAdapters.BOXED_BOOLEAN_FACTORY);
    factories.put(Float.class, FieldAdapters.BOXED_FLOAT_FACTORY);
    factories.put(Double.class, FieldAdapters.BOXED_DOUBLE_FACTORY);

    factories.put(String.class, FieldAdapters.STRING_FACTORY);

    FIELD_ADAPTER_FACTORIES = ImmutableMap.copyOf(factories);
  }

  private Map<Class<?>, DaoAdapter<?>> mDaoAdapterCache = Maps.newHashMap();
}
