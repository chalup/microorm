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

import com.google.common.collect.ImmutableList;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Collection;

class ReflectiveDaoAdapter<T> implements DaoAdapter<T> {

  private final Class<T> mKlass;
  private final ImmutableList<FieldAdapter> mFieldAdapters;
  private final Collection<EmbeddedFieldInitializer> mFieldInitializers;

  ReflectiveDaoAdapter(Class<T> klass, Collection<FieldAdapter> fieldAdapters, Collection<EmbeddedFieldInitializer> fieldInitializers) {
    mKlass = klass;
    mFieldAdapters = ImmutableList.copyOf(fieldAdapters);
    mFieldInitializers = fieldInitializers;
  }

  @Override
  public T createInstance() {
    return createInstance(mKlass);
  }

  private <T> T createInstance(Class<T> klass) {
    try {
      T instance = klass.newInstance();
      for (EmbeddedFieldInitializer fieldInitializer : mFieldInitializers) {
        fieldInitializer.initEmbeddedField(instance);
      }
      return instance;
    } catch (InstantiationException e) {
      throw new AssertionError(e);
    } catch (IllegalAccessException e) {
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
    for (FieldAdapter fieldAdapter : mFieldAdapters) {
      try {
        fieldAdapter.putToContentValues(object, values);
      } catch (IllegalArgumentException e) {
        throw new AssertionError(e);
      } catch (IllegalAccessException e) {
        throw new AssertionError(e);
      }
    }

    return values;
  }
}
