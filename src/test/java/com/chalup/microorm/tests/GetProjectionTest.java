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

package com.chalup.microorm.tests;

import static org.fest.assertions.Assertions.assertThat;

import com.chalup.microorm.MicroOrm;
import com.chalup.microorm.annotations.Column;
import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GetProjectionTest {

  private static final String SIMPLE_ENTITY_COLUMN = "SIMPLE_ENTITY_COLUMN";

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
    assertThat(Sets.newHashSet(projection)).contains(SIMPLE_ENTITY_COLUMN);
  }
}
