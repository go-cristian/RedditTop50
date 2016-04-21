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
package co.iyubinest.reddittop.ui.preview;

import co.iyubinest.reddittop.data.entries.RedEntry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PreviewSourceShould {

  @Mock PreviewView view;
  @Mock PreviewRepo repo;
  @Mock RedEntry entry;
  @Captor ArgumentCaptor<PreviewRepo.Callback> captor;
  private PreviewSource source;

  @Before public void before() throws Exception {
    MockitoAnnotations.initMocks(this);
    source = new PreviewSource(view, repo);
    source.init(entry);
  }

  @Test public void show_image_on_init() throws Exception {
    verify(view, times(1)).showImage(entry.thumbnail());
  }

  @Test public void show_error_on_save_image_failure() throws Exception {
    source.saveImage();
    when(entry.thumbnail()).thenReturn("");
    verify(repo, times(1)).save(anyString(), captor.capture());
    captor.getValue().onFailure();
    verify(view, times(1)).showSaveImageError();
  }

  @Test public void show_success_on_save_image() throws Exception {
    source.saveImage();
    when(entry.thumbnail()).thenReturn("");
    verify(repo, times(1)).save(anyString(), captor.capture());
    captor.getValue().onSuccess();
    verify(view, times(1)).showSaveImageGreeting();
  }
}
