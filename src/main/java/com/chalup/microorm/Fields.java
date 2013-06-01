package com.chalup.microorm;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

class Fields {

  static List<Field> allFieldsIncludingPrivateAndSuper(Class<?> klass) {
    List<Field> fields = Lists.newArrayList();
    while (!klass.equals(Object.class)) {
      Collections.addAll(fields, klass.getDeclaredFields());
      klass = klass.getSuperclass();
    }
    return fields;
  }

}
