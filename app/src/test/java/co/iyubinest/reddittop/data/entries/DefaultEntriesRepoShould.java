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

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultEntriesRepoShould {

  @Mock EntriesCache cache;
  @Mock EntriesService service;
  @Mock EntriesRepo.Callback callback;
  @Captor ArgumentCaptor<EntriesRepo.Callback> captor;
  private DefaultEntriesRepo repo;

  @Before public void before() throws Exception {
    initMocks(this);
    repo = new DefaultEntriesRepo(cache, service);
  }

  @Test public void save_on_cache_when_getting_data_from_service() throws Exception {
    when(cache.has(eq(0))).thenReturn(false);
    repo.page(0, callback);

    verify(service, times(1)).get(eq(0), captor.capture());
    captor.getValue().success(mock(List.class));

    verify(cache, times(1)).save(eq(0), anyListOf(RedEntry.class));
  }

  @Test public void use_chache_when_has_prevoius() throws Exception {
    when(cache.has(eq(0))).thenReturn(true);
    repo.page(0, callback);
    verify(cache, times(1)).get(eq(0));
  }
}
