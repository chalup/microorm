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

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

final class Fields {
  private Fields() {
  }

  static List<Field> allFieldsIncludingPrivateAndSuper(Class<?> klass) {
    List<Field> fields = Lists.newArrayList();
    while (!klass.equals(Object.class)) {
      Collections.addAll(fields, klass.getDeclaredFields());
      klass = klass.getSuperclass();
    }
    return fields;
  }
}
