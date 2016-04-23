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
import co.iyubinest.reddittop.data.preview.PreviewRepo;

public final class PreviewSource {
  private final PreviewView view;
  private final PreviewRepo repo;
  private RedEntry entry;

  public PreviewSource(PreviewView view, PreviewRepo repo) {
    this.view = view;
    this.repo = repo;
  }

  public void init(RedEntry entry) {
    this.entry = entry;
    view.showImage(entry.preview());
  }

  public void saveImage() {
    repo.save(entry.preview(), new PreviewRepo.Callback() {
      @Override public void onFailure() {
        view.showSaveImageError();
      }

      @Override public void onSuccess() {
        view.showSaveImageGreeting();
      }
    });
  }
}
