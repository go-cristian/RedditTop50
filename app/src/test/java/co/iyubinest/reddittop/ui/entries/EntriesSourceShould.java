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
package co.iyubinest.reddittop.ui.entries;

import co.iyubinest.reddittop.data.entries.EntriesRepo;
import co.iyubinest.reddittop.data.entries.RedEntry;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EntriesSourceShould {

  @Mock EntriesView view;
  @Mock EntriesRepo repo;
  @Mock List<RedEntry> entries;
  @Captor ArgumentCaptor<EntriesRepo.Callback> captor;
  EntriesSource source;

  @Before public void before() {
    initMocks(this);
    when(entries.size()).thenReturn(EntriesRepo.SIZE);
    source = new EntriesSource(view, repo);
  }

  @Test public void show_loading_when_request() throws Exception {
    source.request();
    verify(view, times(1)).showLoading();
  }

  @Test public void show_retry_when_source_fails() throws Exception {
    source.request();
    verify(repo, times(1)).page(eq(0), captor.capture());
    captor.getValue().failure();
    verify(view, times(1)).showRetry();
  }

  @Test public void show_retry_when_source_is_empty() throws Exception {
    source.request();
    verify(repo, times(1)).page(eq(0), captor.capture());
    captor.getValue().success(new ArrayList<RedEntry>());
    verify(view, times(1)).showRetry();
  }

  @Test public void show_ui_when_source_success() throws Exception {
    source.request();
    verify(repo, times(1)).page(eq(0), captor.capture());
    captor.getValue().success(entries);
    verify(view, times(1)).render(anyListOf(RedEntry.class));
    verify(view, times(0)).clearEntries();
  }

  @Test public void show_error_cell_when_request_next_page_with_source_error() throws Exception {
    //first call success
    source.request();
    verify(repo, times(1)).page(eq(0), captor.capture());
    captor.getValue().success(entries);
    //second call fails
    source.request();
    verify(repo, times(1)).page(eq(1), captor.capture());
    captor.getValue().failure();

    verify(view, times(1)).showRetryCell();
    verify(view, times(1)).showLoading();
    verify(view, times(1)).showLoadingCell();
    verify(view, times(0)).clearEntries();
  }

  @Test public void show_more_elements_when_request_next_page_success() throws Exception {
    //first call success
    source.request();
    verify(repo, times(1)).page(eq(0), captor.capture());
    captor.getValue().success(entries);
    //second call is a success too
    source.request();
    verify(repo, times(1)).page(eq(1), captor.capture());
    captor.getValue().success(entries);

    verify(view, times(2)).render(anyListOf(RedEntry.class));
    verify(view, times(1)).showLoading();
    verify(view, times(1)).showLoadingCell();
    verify(view, times(0)).clearEntries();
  }

  @Test public void show_only_one_page_when_refreshing() throws Exception {
    //first call success
    source.request();
    verify(repo, times(1)).page(eq(0), captor.capture());
    captor.getValue().success(entries);
    //second call is a success too
    source.request();
    verify(repo, times(1)).page(eq(1), captor.capture());
    captor.getValue().success(entries);

    //update triggers page 0
    source.update();
    captor.getValue().success(entries);

    verify(view, times(3)).render(anyListOf(RedEntry.class));
    verify(view, times(1)).showLoading();
    verify(view, times(1)).clearEntries();
    verify(view, times(1)).showUpdating();
    verify(view, times(1)).showLoadingCell();
  }
}