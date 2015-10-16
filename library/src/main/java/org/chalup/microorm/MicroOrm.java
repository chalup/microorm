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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.chalup.microorm.annotations.Column;
import org.chalup.microorm.annotations.Embedded;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * This is the main class for using MicroOrm. MicroOrm is typically used by
 * first constructing a MicroOrm instance and then invoking
 * {@link #fromCursor(android.database.Cursor, Class)} or
 * {@link #toContentValues(Object)} methods on it.
 *
 * You can create a MicroOrm instance by invoking {@code new MicroOrm()} if the
 * default configuration is all you need. You can also use
 * {@link MicroOrm.Builder} to build a MicroOrm instance
 * with support for custom fields' types.
 */
public class MicroOrm {
  /**
   * Creates an object of the specified type from the current row in
   * {@link Cursor}.
   *
   * @param <T> the type of the desired object
   * @param c an open {@link Cursor} with position set to valid row
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
   * @param c an open {@link Cursor} with position set to valid row
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
    DaoAdapter<T> adapter = (DaoAdapter<T>) getAdapter(object.getClass());
    return adapter.toContentValues(adapter.createContentValues(), object);
  }

  /**
   * Convenience method for converting the whole {@link Cursor} into
   * {@link List} of objects of specified type.
   *
   * @param <T> the type of the provided object
   * @param c a valid {@link Cursor}; the provided {@link Cursor} will not be
   * closed
   * @param klass The {@link Class} of the desired object
   * @return the {@link List} of object of type T created from the entire
   * {@link Cursor}
   */
  public <T> List<T> listFromCursor(Cursor c, Class<T> klass) {
    List<T> result = Lists.newArrayList();

    if (c != null && c.moveToFirst()) {
      DaoAdapter<T> adapter = getAdapter(klass);
      do {
        result.add(adapter.fromCursor(c, adapter.createInstance()));
      } while (c.moveToNext());
    }

    return result;
  }

  /**
   * Method for acquiring the {@link Function} converting the {@link Cursor}
   * row into object of specified type.
   *
   * @param <T> the type of the provided object
   * @param klass The {@link Class} of the function output type
   * @return the {@link Function} converting {@link Cursor} row into object
   * of type T.
   */
  public <T> Function<Cursor, T> getFunctionFor(final Class<T> klass) {
    return new Function<Cursor, T>() {
      private final DaoAdapter<T> mAdapter = getAdapter(klass);

      @Override
      public T apply(Cursor c) {
        return mAdapter.fromCursor(c, mAdapter.createInstance());
      }
    };
  }

  /**
   * Constructs {@link Function} converting single column in {@link Cursor}
   * row into object of given type. You can get builder instances with
   * {@link org.chalup.microorm.MicroOrm#getColumn(String)}.
   */
  public interface ColumnFunctionBuilder {
    /**
     * @param <T> the type of the requested object
     * @param klass The {@link Class} of the function output type
     * @return the {@link Function} converting {@link Cursor} row into object
     * of type T using {@link TypeAdapter}s registered in current
     */
    <T> Function<Cursor, T> as(Class<T> klass);
  }

  /**
   * Constructs new {@link ColumnFunctionBuilder} for specified
   * {@code columnName}.
   */
  public ColumnFunctionBuilder getColumn(final String columnName) {
    Preconditions.checkNotNull(columnName);

    return new ColumnFunctionBuilder() {
      @SuppressWarnings("unchecked")
      @Override
      public <T> Function<Cursor, T> as(Class<T> klass) {
        Preconditions.checkArgument(mTypeAdapters.containsKey(klass));

        final TypeAdapter<T> adapter = (TypeAdapter<T>) mTypeAdapters.get(klass);

        return new Function<Cursor, T>() {
          @Override
          public T apply(Cursor c) {
            return adapter.fromCursor(c, columnName);
          }
        };
      }
    };
  }

  /**
   * Returns an array containing column names needed by {@link MicroOrm} to
   * successfully create an object of the specified type from {@link Cursor}.
   *
   * @param klass The {@link Class} of the object, for which the projection
   * should be generated
   * @return the {@link String[]} containing column names
   */
  public <T> String[] getProjection(Class<T> klass) {
    return getAdapter(klass).getProjection();
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
    ImmutableList.Builder<FieldAdapter> fieldAdapters = ImmutableList.builder();
    ImmutableList.Builder<EmbeddedFieldInitializer> fieldInitializers = ImmutableList.builder();

    for (Field field : Fields.allFieldsIncludingPrivateAndSuper(klass)) {
      field.setAccessible(true);

      Column columnAnnotation = field.getAnnotation(Column.class);
      if (columnAnnotation != null) {
        if (field.getType().isPrimitive() && columnAnnotation.treatNullAsDefault()) {
          throw new IllegalArgumentException("Cannot set treatNullAsDefault on primitive members");
        }
        if (columnAnnotation.treatNullAsDefault() && columnAnnotation.readonly()) {
          throw new IllegalArgumentException("It doesn't make sense to set treatNullAsDefault on readonly column");
        }
        ColumnFieldAdapter fieldAdapter = new ColumnFieldAdapter(field, mTypeAdapters.get(field.getType()));

        fieldAdapters.add(fieldAdapter);
      }

      Embedded embeddedAnnotation = field.getAnnotation(Embedded.class);
      if (embeddedAnnotation != null) {
        DaoAdapter<?> daoAdapter = getAdapter(field.getType());
        EmbeddedFieldAdapter fieldAdapter = new EmbeddedFieldAdapter(field, daoAdapter);

        fieldAdapters.add(fieldAdapter);
        fieldInitializers.add(new EmbeddedFieldInitializer(field, daoAdapter));
      }
    }

    return new ReflectiveDaoAdapter<>(klass, fieldAdapters.build(), fieldInitializers.build());
  }

  /**
   * Constructs a MicroOrm object with default configuration, i.e. with support
   * only for primitives, boxed primitives and String fields.
   */
  public MicroOrm() {
    this(TYPE_ADAPTERS);
  }

  private MicroOrm(ImmutableMap<Class<?>, TypeAdapter<?>> typeAdapters) {
    mTypeAdapters = typeAdapters;
  }

  /**
   * Use this builder to construct a {@link MicroOrm} instance when you need
   * support for custom fields' types. For {@link MicroOrm} with default
   * configuration, it is simpler to use {@code new MicroOrm()}. {@link Builder}
   * is best used by creating it, and then invoking
   * {@link #registerTypeAdapter(Class, TypeAdapter)} for each custom type, and
   * finally calling {@link #build()}.</p>
   */
  public static class Builder {
    private final Map<Class<?>, TypeAdapter<?>> mTypeAdapters;

    public Builder() {
      mTypeAdapters = Maps.newHashMap(TYPE_ADAPTERS);
    }

    /**
     * Configures MicroOrm for custom conversion of fields of given types.
     *
     * @param klass the {@link Class} of the type the {@link TypeAdapter} being
     * registered
     * @param typeAdapter implementation defining how the custom type should be
     * converted to {@link ContentValues} and from {@link Cursor}.
     * @return a reference to this {@link Builder} object to fulfill the
     * "Builder" pattern
     */
    public <T> Builder registerTypeAdapter(Class<T> klass, TypeAdapter<T> typeAdapter) {
      mTypeAdapters.put(klass, typeAdapter);
      return this;
    }

    /**
     * Creates a {@link MicroOrm} instance with support for custom types that
     * were registered with this {@link Builder}. This method is free of
     * side-effects to this {@code Builder} instance and hence can be called
     * multiple times.
     *
     * @return an instance of MicroOrm with support for custom types that were
     * registered with this this builder
     */
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

    typeAdapters.put(Short.class, new OptionalTypeAdapter<>(new TypeAdapters.ShortAdapter()));
    typeAdapters.put(Integer.class, new OptionalTypeAdapter<>(new TypeAdapters.IntegerAdapter()));
    typeAdapters.put(Long.class, new OptionalTypeAdapter<>(new TypeAdapters.LongAdapter()));
    typeAdapters.put(Boolean.class, new OptionalTypeAdapter<>(new TypeAdapters.BooleanAdapter()));
    typeAdapters.put(Float.class, new OptionalTypeAdapter<>(new TypeAdapters.FloatAdapter()));
    typeAdapters.put(Double.class, new OptionalTypeAdapter<>(new TypeAdapters.DoubleAdapter()));

    typeAdapters.put(String.class, new OptionalTypeAdapter<>(new TypeAdapters.StringAdapter()));

    TYPE_ADAPTERS = ImmutableMap.copyOf(typeAdapters);
  }

  private final ImmutableMap<Class<?>, TypeAdapter<?>> mTypeAdapters;
  private final Map<Class<?>, DaoAdapter<?>> mDaoAdapterCache = Maps.newHashMap();
}
