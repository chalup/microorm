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
   * @param c a valid {@link Cursor}; the provided {@link Cursor} will not be
   *          closed
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
    Collection<FieldAdapter> fieldAdapters = Lists.newArrayList();
    Collection<EmbeddedFieldInitializer> fieldInitializers = Lists.newArrayList();

    for (Field field : Fields.allFieldsIncludingPrivateAndSuper(klass)) {
      field.setAccessible(true);

      Column columnAnnotation = field.getAnnotation(Column.class);
      if (columnAnnotation != null) {
        fieldAdapters.add(new ColumnFieldAdapter(field, mTypeAdapters.get(field.getType())));
      }

      Embedded embeddedAnnotation = field.getAnnotation(Embedded.class);
      if (embeddedAnnotation != null) {
        DaoAdapter<?> daoAdapter = getAdapter(field.getType());
        fieldAdapters.add(new EmbeddedFieldAdapter(field, daoAdapter));
        fieldInitializers.add(new EmbeddedFieldInitializer(field, daoAdapter));
      }
    }

    return new ReflectiveDaoAdapter<T>(klass, fieldAdapters, fieldInitializers);
  }

  public MicroOrm() {
    this(TYPE_ADAPTERS);
  }

  private MicroOrm(ImmutableMap<Class<?>, TypeAdapter<?>> typeAdapters) {
    mTypeAdapters = typeAdapters;
  }

  public static class Builder {
    private final Map<Class<?>, TypeAdapter<?>> mTypeAdapters;

    public Builder() {
      mTypeAdapters = Maps.newHashMap(TYPE_ADAPTERS);
    }

    public <T> Builder registerTypeAdapter(Class<T> klass, TypeAdapter<T> typeAdapter) {
      mTypeAdapters.put(klass, typeAdapter);
      return this;
    }

    public MicroOrm build() {
      return new MicroOrm(ImmutableMap.copyOf(mTypeAdapters));
    }
  }

  private static final ImmutableMap<Class<?>, TypeAdapter<?>> TYPE_ADAPTERS;

  static {

    Map<Class<?>, TypeAdapter<?>> typeAdapters = Maps.newHashMap();

    typeAdapters.put(short.class, new TypeAdapters.ShortAdapter());
    typeAdapters.put(int.class, new TypeAdapters.IntegerAdapter());
    typeAdapters.put(long.class, new TypeAdapters.LongAdapter());
    typeAdapters.put(boolean.class, new TypeAdapters.BooleanAdapter());
    typeAdapters.put(float.class, new TypeAdapters.FloatAdapter());
    typeAdapters.put(double.class, new TypeAdapters.DoubleAdapter());

    typeAdapters.put(Short.class, new OptionalTypeAdapter(new TypeAdapters.ShortAdapter()));
    typeAdapters.put(Integer.class, new OptionalTypeAdapter(new TypeAdapters.IntegerAdapter()));
    typeAdapters.put(Long.class, new OptionalTypeAdapter(new TypeAdapters.LongAdapter()));
    typeAdapters.put(Boolean.class, new OptionalTypeAdapter(new TypeAdapters.BooleanAdapter()));
    typeAdapters.put(Float.class, new OptionalTypeAdapter(new TypeAdapters.FloatAdapter()));
    typeAdapters.put(Double.class, new OptionalTypeAdapter(new TypeAdapters.DoubleAdapter()));

    typeAdapters.put(String.class, new OptionalTypeAdapter(new TypeAdapters.StringAdapter()));

    TYPE_ADAPTERS = ImmutableMap.copyOf(typeAdapters);
  }

  private final ImmutableMap<Class<?>, TypeAdapter<?>> mTypeAdapters;
  private final Map<Class<?>, DaoAdapter<?>> mDaoAdapterCache = Maps.newHashMap();
}
