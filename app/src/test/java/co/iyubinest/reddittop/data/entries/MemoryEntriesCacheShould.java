/**
 * Copyright (C) 2015 Cristian Gómez Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.iyubinest.reddittop.data.entries;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyListOf;

public class MemoryEntriesCacheShould {

  private EntriesCache cache;

  @Before public void before() {
    cache = new MemoryEntriesCache();
  }

  @Test public void no_have_data_on_start() {
    assertThat(cache.has(0), is(false));
    assertThat(cache.has(1), is(false));
    assertThat(cache.has(2), is(false));
  }

  @Test public void save_data() throws Exception {
    cache.save(0, null);
    assertThat(cache.has(0), is(false));

    cache.save(0, anyListOf(RedEntry.class));
    assertThat(cache.has(0), is(true));

    cache.save(1, null);
    assertThat(cache.has(1), is(false));

    cache.save(1, anyListOf(RedEntry.class));
    assertThat(cache.has(1), is(true));
  }
}
