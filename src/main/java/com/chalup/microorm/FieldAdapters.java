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

class FieldAdapters {

  public static final FieldAdapterFactory SHORT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(outTarget, inCursor.getShort(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getShort(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory INT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(outTarget, inCursor.getInt(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getInt(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory LONG_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(outTarget, inCursor.getLong(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getLong(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory FLOAT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(outTarget, inCursor.getFloat(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getFloat(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory DOUBLE_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(outTarget, inCursor.getDouble(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getDouble(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOOLEAN_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(outTarget, inCursor.getInt(inCursor.getColumnIndexOrThrow(mColumnName)) == 1);
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, mField.getBoolean(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_SHORT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(outTarget, inCursor.isNull(columnIndex) ? null : inCursor.getShort(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Short) mField.get(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_INT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(outTarget, inCursor.isNull(columnIndex) ? null : inCursor.getInt(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Integer) mField.get(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_LONG_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(outTarget, inCursor.isNull(columnIndex) ? null : inCursor.getLong(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Long) mField.get(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_FLOAT_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(outTarget, inCursor.isNull(columnIndex) ? null : inCursor.getFloat(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Float) mField.get(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_DOUBLE_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(outTarget, inCursor.isNull(columnIndex) ? null : inCursor.getDouble(columnIndex));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Double) mField.get(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory BOXED_BOOLEAN_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          int columnIndex = inCursor.getColumnIndexOrThrow(mColumnName);
          mField.set(outTarget, inCursor.isNull(columnIndex) ? null : inCursor.getInt(columnIndex) == 1);
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (Boolean) mField.get(inObject));
        }
      };
    }
  };

  public static final FieldAdapterFactory STRING_FACTORY = new FieldAdapterFactory() {
    @Override
    public FieldAdapter buildAdapter(Field field) {
      return new FieldAdapter(field) {
        @Override
        public void setValueFromCursor(Cursor inCursor, Object outTarget)
            throws IllegalArgumentException, IllegalAccessException {
          mField.set(outTarget, inCursor.getString(inCursor.getColumnIndexOrThrow(mColumnName)));
        }

        @Override
        public void putToContentValues(Object inObject, ContentValues outValues)
            throws IllegalArgumentException, IllegalAccessException {
          outValues.put(mColumnName, (String) mField.get(inObject));
        }
      };
    }
  };

}
