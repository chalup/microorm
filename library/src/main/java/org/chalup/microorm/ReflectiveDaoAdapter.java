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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Sets;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Set;

class ReflectiveDaoAdapter<T> implements DaoAdapter<T> {

  private final ClassFactory<T> mClassFactory;
  private final ImmutableList<FieldAdapter> mFieldAdapters;
  private final ImmutableList<EmbeddedFieldInitializer> mFieldInitializers;
  private final String[] mProjection;
  private final String[] mWritableColumns;
  private final ImmutableSet<String> mWritableDuplicates;

  ReflectiveDaoAdapter(Class<T> klass, ImmutableList<FieldAdapter> fieldAdapters, ImmutableList<EmbeddedFieldInitializer> fieldInitializers) {
    mClassFactory = ClassFactory.get(klass);
    mFieldAdapters = fieldAdapters;
    mFieldInitializers = fieldInitializers;

    ImmutableList.Builder<String> projectionBuilder = ImmutableList.builder();
    ImmutableList.Builder<String> writableColumnsBuilder = ImmutableList.builder();

    for (FieldAdapter fieldAdapter : fieldAdapters) {
      projectionBuilder.add(fieldAdapter.getColumnNames());
      writableColumnsBuilder.add(fieldAdapter.getWritableColumnNames());
    }
    mProjection = array(projectionBuilder.build());
    mWritableColumns = array(writableColumnsBuilder.build());
    mWritableDuplicates = findDuplicates(mWritableColumns);
  }

  private static String[] array(Collection<String> collection) {
    return collection.toArray(new String[collection.size()]);
  }

  private static <T> ImmutableSet<T> findDuplicates(T[] array) {
    final Builder<T> result = ImmutableSet.builder();
    final Set<T> uniques = Sets.newHashSet();

    for (T element : array) {
      if (!uniques.add(element)) {
        result.add(element);
      }
    }

    return result.build();
  }

  @Override
  public T createInstance() {
    try {
      T instance = mClassFactory.newInstance();
      for (EmbeddedFieldInitializer fieldInitializer : mFieldInitializers) {
        fieldInitializer.initEmbeddedField(instance);
      }
      return instance;
    } catch (InvocationTargetException e) {
      throw new AssertionError(e);
    } catch (IllegalAccessException e) {
      throw new AssertionError(e);
    } catch (InstantiationException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public T fromCursor(Cursor c, T object) {
    try {
      for (FieldAdapter fieldAdapter : mFieldAdapters) {
        fieldAdapter.setValueFromCursor(c, object);
      }
      return object;
    } catch (IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public ContentValues toContentValues(ContentValues values, T object) {
    if (!mWritableDuplicates.isEmpty()) {
      throw new IllegalArgumentException("Duplicate columns definitions: " + Joiner.on(", ").join(mWritableDuplicates));
    }
    try {
      for (FieldAdapter fieldAdapter : mFieldAdapters) {
        fieldAdapter.putToContentValues(object, values);
      }
    } catch (IllegalArgumentException e) {
      throw new AssertionError(e);
    } catch (IllegalAccessException e) {
      throw new AssertionError(e);
    }

    return values;
  }

  @Override
  public ContentValues createContentValues() {
    return new ContentValues(mWritableColumns.length);
  }

  @Override
  public String[] getProjection() {
    return mProjection.clone();
  }

  @Override
  public String[] getWritableColumns() {
    return mWritableColumns.clone();
  }
}
