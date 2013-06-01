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

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.List;

class FieldAdapters {

  public static final FieldAdapterFactory SHORT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(getObjectToManipulateWith(outTarget), inCursor.getShort(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getShort(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory INT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(getObjectToManipulateWith(outTarget), inCursor.getInt(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getInt(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory LONG_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(getObjectToManipulateWith(outTarget), inCursor.getLong(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getLong(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory FLOAT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(getObjectToManipulateWith(outTarget), inCursor.getFloat(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getFloat(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory DOUBLE_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(getObjectToManipulateWith(outTarget), inCursor.getDouble(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getDouble(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOOLEAN_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(getObjectToManipulateWith(outTarget), inCursor.getInt(inCursor.getColumnIndexOrThrow(mColumnName)) == 1);
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getBoolean(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_SHORT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(getObjectToManipulateWith(outTarget), inCursor.isNull(columnIndex) ? null : inCursor.getShort(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Short) mField.get(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_INT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(getObjectToManipulateWith(outTarget), inCursor.isNull(columnIndex) ? null : inCursor.getInt(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Integer) mField.get(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_LONG_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(getObjectToManipulateWith(outTarget), inCursor.isNull(columnIndex) ? null : inCursor.getLong(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Long) mField.get(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_FLOAT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(getObjectToManipulateWith(outTarget), inCursor.isNull(columnIndex) ? null : inCursor.getFloat(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Float) mField.get(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_DOUBLE_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(getObjectToManipulateWith(outTarget), inCursor.isNull(columnIndex) ? null : inCursor.getDouble(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Double) mField.get(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_BOOLEAN_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(getObjectToManipulateWith(outTarget), inCursor.isNull(columnIndex) ? null : inCursor.getInt(columnIndex) == 1);
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Boolean) mField.get(getObjectToManipulateWith(inObject)));
        }
      };
    }
  };

  public static final FieldAdapterFactory STRING_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field, List<Field> parentFields) {
      return new FieldAdapter(field, parentFields) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.setAccessible(true);
          mField.set(getObjectToManipulateWith(outTarget), inCursor.getString(inCursor.getColumnIndexOrThrow(mColumnName)));
          mField.setAccessible(false);
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          mField.setAccessible(true);
          outValues.put(mColumnName, (String) mField.get(getObjectToManipulateWith(inObject)));
          mField.setAccessible(false);
        }
      };
    }
  };

}
