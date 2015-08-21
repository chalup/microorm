package org.chalup.microorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation to easily combine multiple columns into one object.
 * Fields annotated with @Embedded are represented via 0..n entries in ContentValues or Cursor.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Embedded {

}
