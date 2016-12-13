package org.chalup.microorm.tests;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import android.database.Cursor;
import android.provider.BaseColumns;

public class ObjectWithNestedNonPublicObject {
  static class NonPublicNestedObject {
    @Column(BaseColumns._ID)
    long id;
  }

  public static NonPublicNestedObject test(MicroOrm microOrm, Cursor cursor) {
    return microOrm.fromCursor(cursor, NonPublicNestedObject.class);
  }
}
