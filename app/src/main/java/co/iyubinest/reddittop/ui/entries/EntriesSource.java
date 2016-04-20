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

import java.util.Collection;

public class EntriesSource implements EntriesRepo.Callback {
  private final EntriesView view;
  private final EntriesRepo repo;
  private int currentPage = 0;

  public EntriesSource(EntriesView view, EntriesRepo repo) {
    this.view = view;
    this.repo = repo;
  }

  public void request() {
    view.showLoading();
    repo.page(currentPage, this);
  }

  @Override public void failure() {
    if (currentPage == 0) {
      view.showRetry();
    } else {
      view.showRetryCell();
    }
  }

  @Override public void success(Collection<RedditEntry> entries) {
    if (entries.size() == 0) {
      failure();
    } else {
      currentPage++;
      view.render(entries);
    }
  }
}
