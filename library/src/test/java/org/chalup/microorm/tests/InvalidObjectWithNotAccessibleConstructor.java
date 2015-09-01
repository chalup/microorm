package org.chalup.microorm.tests;

import org.chalup.microorm.annotations.Column;

import android.provider.BaseColumns;

public class InvalidObjectWithNotAccessibleConstructor {
  private InvalidObjectWithNotAccessibleConstructor() {
  }

  @Column(BaseColumns._ID)
  long id;
}
