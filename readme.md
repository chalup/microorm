MicroOrm
========

A library for creating POJOs from Android Cursors and converting
them back into ContentValues.

The following pattern is quite common:

```java
private static import MyContract.SomeObjectColumns.*;

private static class SomeObject {

  private final String mSomeField;
  private final long mSomeOtherField;
  private final Long mSomeOptionalField;
  private final boolean mBooleanField;

  private SomeObject (String someValue, long someOtherValue, Long someOptionalValue, boolean booleanValue) {
    mSomeField = someValue;
    mSomeOtherField = someOtherValue;
    mSomeOptionalField = someOptionalValue;
    mBooleanField = booleanValue;
  }

  public ContentValues getContentValues() {
    ContentValues values =  new ContentValues();

    values.put(SOME_FIELD, mSomeField);
    values.put(SOME_OTHER_FIELD, mSomeOtherField);
    values.put(SOME_OPTIONAL_FIELD, mSomeOptionalField);
    values.put(SOME_BOOLEAN_FIELD, mBooleanField);

    return values;
  }

  public static SomeObject fromCursor(Cursor c) {
    String someValue = c.getString(c.getColumnIndexOrThrow(SOME_FIELD));
    long someOtherValue = c.getLong(c.getColumnIndexOrThrow(SOME_OTHER_FIELD));

    int optionalValueColumnIndex = c.getColumnIndexOrThrow(SOME_OPTIONAL_FIELD);
    someOptionalValue = c.isNull(optionalValueColumnIndex)
      ? null
      : c.getLong(optionalValueColumnIndex);

    booleanValue = c.getInt(c.getColumnIndexOrThrow(SOME_BOOLEAN_FIELD)) == 1;

    return new SomeObject(someValue, someOtherValue, someOptionalValue, booleanValue);
  }

  // getters and setters
}
```

Kind of meh, especially if you repeat it several times. With MicroOrm you can
reduce the boilerplate to this:

```java
private static import MyContract.SomeObjectColumns.*;

private static class SomeObject {
  @Column(SOME_FIELD)
  private String mSomeField;

  @Column(SOME_OTHER_FIELD)
  private long mSomeOtherField;

  @Column(SOME_OPTIONAL_FIELD)
  private Long mSomeOptionalField;

  @Column(SOME_BOOLEAN_FIELD)
  private boolean mBooleanField;

  // getters and setters
}
```

And then use MicroOrm:

```java
MicroOrm uOrm = new MicroOrm();
SomeObject o = uOrm.fromCursor(c, SomeObject.class);
ContentValues values = uOrm.toContentValues(o);

// in case you'll iterate over the whole cursor
SomeObject o = new SomeObject();
do {
  uORM.fromCursor(c, o);
} while (c.moveToNext());

// if you need to dump the whole cursor to list
List<SomeObject> someObjects = uOrm.listFromCursor(c, SomeObject.class);
```

Caveats
-------

* Generic entities or fields are not supported.
* Unlike gson, MicroOrm works only on explicitly annotated fields.
* Current implementation is roughly 2-2.5 times slower than handrolled methods.


Usage
-----
Just add the dependency to your `build.gradle`:

```groovy
dependencies {
    compile 'org.chalup.microorm:microorm:0.8.0'
}
```

minSdkVersion = 10
------------------
MicroOrm is compatibile with Android 2.3 and newer.

Download
--------
Download [jar](http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=org.chalup.microorm&a=microorm&v=LATEST) or add the dependency to your pom.xml:

```xml
<dependency>
  <groupId>org.chalup.microorm</groupId>
  <artifactId>microorm</artifactId>
  <version>0.2</version>
</dependency>
```

License
-------

    Copyright (C) 2013 Jerzy Chalupski

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
