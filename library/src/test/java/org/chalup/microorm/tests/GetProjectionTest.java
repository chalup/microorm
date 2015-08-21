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

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;
import org.chalup.microorm.annotations.Embedded;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GetProjectionTest {

  private static final String SIMPLE_ENTITY_COLUMN = "SIMPLE_ENTITY_COLUMN";
  private static final String DERIVED_ENTITY_COLUMN = "DERIVED_ENTITY_COLUMN";
  private static final String EMBEDDED_ENTITY_COLUMN = "EMBEDDED_ENTITY_COLUMN";
  private static final String ANOTHER_EMBEDDED_ENTITY_COLUMN = "ANOTHER_EMBEDDED_ENTITY_COLUMN";
  private static final String CONTAINER_ENTITY_COLUMN = "CONTAINER_ENTITY_COLUMN";

  private MicroOrm testSubject;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm();
  }

  public static class SimpleEntity {
    @Column(SIMPLE_ENTITY_COLUMN)
    String simpleColumn;
  }

  @Test
  public void shouldGetProjectionForSimpleEntity() throws Exception {
    String[] projection = testSubject.getProjection(SimpleEntity.class);

    assertThat(projection).isNotNull();
    assertThat(projection).asList().contains(SIMPLE_ENTITY_COLUMN);
  }

  public static class DerivedEntity extends SimpleEntity {
    @Column(DERIVED_ENTITY_COLUMN)
    String derivedColumn;
  }

  @Test
  public void shouldGetColumnNamesFromSuperClass() throws Exception {
    String[] projection = testSubject.getProjection(DerivedEntity.class);

    assertThat(projection).isNotNull();

    assertThat(projection).asList().contains(DERIVED_ENTITY_COLUMN);
    assertThat(projection).asList().contains(SIMPLE_ENTITY_COLUMN);
  }

  public static class EmbeddedEntity {
    @Column(EMBEDDED_ENTITY_COLUMN)
    String embeddedColumn;

    @Column(ANOTHER_EMBEDDED_ENTITY_COLUMN)
    String anotherEmbeddedColumn;
  }

  public static class ContainerEntity {
    @Column(CONTAINER_ENTITY_COLUMN)
    String containerColumn;

    @Embedded
    EmbeddedEntity embedded;
  }

  @Test
  public void shouldGetColumnNamesFromEmbeddedEntities() throws Exception {
    String[] projection = testSubject.getProjection(ContainerEntity.class);

    assertThat(projection).isNotNull();

    assertThat(projection).asList().contains(CONTAINER_ENTITY_COLUMN);
    assertThat(projection).asList().contains(EMBEDDED_ENTITY_COLUMN);
    assertThat(projection).asList().contains(ANOTHER_EMBEDDED_ENTITY_COLUMN);
  }
}
