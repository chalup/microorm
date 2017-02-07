Change Log
==========

Version 0.8.0 *(2017-02-07)*
----------------------------

 * Add missing `@Target(FIELD)` to `@Embedded` annotation. This solves the issue with incorrect projection for Kotlin data classes with `@Embedded` fields.

Version 0.7.0 *(2016-12-13)*
----------------------------

 * Better handling of object creation. MicroOrm now can create instances of Kotlin data classes, classes without public constructor or classes without a default constructor.

Version 0.6.2 *(2015-10-19)*
----------------------------

 * Add Proguard consumer rules

Version 0.6.1 *(2015-09-25)*
----------------------------

 * Reduce number of allocations

Version 0.6.0 *(2015-08-21)*
----------------------------

 * Added `getColumn(String).as(Class<T>)` method returning deserialization `Function<Cursor, T>` which uses custom type adapters registered in MicroOrm instance.
 * Migrate to Gradle

Version 0.5.0 *(2014-09-19)*
----------------------------

 * Allow duplicate `@Column` annotation, with following restriction: you can get `ContentValues` from object with duplicate `@Column`s only if at most one `@Column` is `readonly=false`.

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
