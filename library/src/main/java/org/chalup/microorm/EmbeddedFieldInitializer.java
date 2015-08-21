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

import java.lang.reflect.Field;

class EmbeddedFieldInitializer {
  private final Field mField;
  private final DaoAdapter<Object> mDaoAdapter;

  @SuppressWarnings("unchecked")
  public EmbeddedFieldInitializer(Field field, DaoAdapter<?> daoAdapter) {
    mField = field;
    mDaoAdapter = ((DaoAdapter<Object>) daoAdapter);
  }

  public void initEmbeddedField(Object obj) throws IllegalAccessException {
    mField.set(obj, mDaoAdapter.createInstance());
  }
}
