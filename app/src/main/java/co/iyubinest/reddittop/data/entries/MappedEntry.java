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

import android.support.annotation.NonNull;
import co.iyubinest.reddittop.data.entries.entities.Child;
import co.iyubinest.reddittop.data.entries.entities.WebEntries;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public final class MappedEntry {
  private final WebEntries entries;

  public MappedEntry(WebEntries entries) {
    this.entries = entries;
  }

  public Collection<RedEntry> value() {
    List<RedEntry> mapEntries = new ArrayList<>(entries == null ? 0 : entries.data.children.size());
    if (entries != null) {
      for (Child child : entries.data.children) {
        mapEntries.add(map(child));
      }
    }
    return mapEntries;
  }

  @NonNull private RedEntry map(Child child) {
    String preview = null;
    if (child.data.preview != null
        && child.data.preview.images != null
        && child.data.preview.images.size() > 0) {
      preview = child.data.preview.images.get(0).source.url;
    }
    return RedEntry.create(child.data.title, child.data.author,
        new Date(Long.valueOf(child.data.created) * 1000L), child.data.numComments,
        child.data.thumbnail, preview);
  }
}
