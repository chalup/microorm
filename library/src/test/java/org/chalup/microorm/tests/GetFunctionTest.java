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

package org.chalup.microorm.tests;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.database.Cursor;
import android.database.MatrixCursor;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GetFunctionTest {

  private static final String SIMPLE_ENTITY_COLUMN = "SIMPLE_ENTITY_COLUMN";

  private MicroOrm testSubject;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm();
  }

  public static class SimpleEntity {
    @Column(SIMPLE_ENTITY_COLUMN)
    String simpleColumn;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      SimpleEntity that = (SimpleEntity) o;
      return Objects.equal(simpleColumn, that.simpleColumn);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(simpleColumn);
    }
  }

  @Test
  public void functionShouldReturnTheSameObjectsAsListFromCursor() throws Exception {
    MatrixCursor cursor = new MatrixCursor(new String[] { SIMPLE_ENTITY_COLUMN });
    for (int i = 0; i != 5; i++) {
      cursor.addRow(new Object[] { "row" + i });
    }

    List<SimpleEntity> reference = testSubject.listFromCursor(cursor, SimpleEntity.class);

    List<SimpleEntity> fromFunction = Lists.newArrayList();
    Function<Cursor, SimpleEntity> function = testSubject.getFunctionFor(SimpleEntity.class);
    cursor.moveToFirst();
    do {
      fromFunction.add(function.apply(cursor));
    } while (cursor.moveToNext());

    assertThat(fromFunction).containsSequence(reference);
  }
}
