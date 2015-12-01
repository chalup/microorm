package org.chalup.microorm.tests;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import android.database.Cursor;
import android.provider.BaseColumns;

public class ObjectWithNestedNonPulicObject {
  static class NonPublicNestedObject {
    @Column(BaseColumns._ID)
    long id;
  }

  public static void test(MicroOrm microOrm, Cursor cursor) {
    microOrm.fromCursor(cursor, NonPublicNestedObject.class);
  }
}
