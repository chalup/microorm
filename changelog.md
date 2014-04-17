Change Log
==========

Version 0.4.1 *(2014-04-17)*
----------------------------

 * Fixed crash on converting @Embedded object with null fields to ContentValues

Version 0.4 *(2014-02-25)*
----------------------------

 * Fixed converting null objects to ContentValues in OptionalTypeAdapter.
 * minSdkVersion = 10

Version 0.3 *(2013-09-21)*
----------------------------

 * Added `getFunctionFor` method returning deserialization `Function<Cursor, T>`.

Version 0.2 *(2013-08-22)*
----------------------------

 * Changed `collectionFromCursor` into `listFromCursor` returning `List<T>` instead of `Collection<T>`.
 * Do not allow multiple @Column annotations with the same column name in scope of a single entity.
 * Added `readonly` argument for @Column annotation to mark that the field should be read from `Cursor`, but not included in `ContentValues`.
 * Added `treatNullAsDefault` argument for @Column annotation to mark that the column should not be included in `ContentValues`, so the backing database can use the default value for this column.


Version 0.1 *(2013-08-14)*
----------------------------

 * Initial release.
