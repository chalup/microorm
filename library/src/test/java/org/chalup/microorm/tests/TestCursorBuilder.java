package org.chalup.microorm.tests;

import android.database.CursorWrapper;
import android.database.MatrixCursor;

public class TestCursorBuilder extends CursorWrapper {
  private final MatrixCursor mCursor;

  private TestCursorBuilder(MatrixCursor cursor) {
    super(cursor);
    mCursor = cursor;
  }

  public TestCursorBuilder addRow(Object... columnValues) {
    mCursor.addRow(columnValues);

    if (mCursor.getCount() == 1) {
      mCursor.moveToFirst();
    }

    return this;
  }

  public static TestCursorBuilder cursor(String... columns) {
    return new TestCursorBuilder(new MatrixCursor(columns));
  }
}
