/*************************************************************************
 * CONFIDENTIAL
 * __________________
 *
 * [2016] All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of {The Company} and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to {The Company}
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from {The Company}.
 */
package co.iyubinest.reddittop.data.entries;

import android.support.annotation.NonNull;
import co.iyubinest.reddittop.data.entries.entities.Child;
import co.iyubinest.reddittop.data.entries.entities.WebEntries;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MappedEntry {
  private final WebEntries entries;

  public MappedEntry(WebEntries entries) {
    this.entries = entries;
  }

  public Collection<RedditEntry> value() {
    List<RedditEntry> mapEntries =
        new ArrayList<>(entries == null ? 0 : entries.data.children.size());
    if (entries != null) {
      for (Child child : entries.data.children) {
        mapEntries.add(map(child));
      }
    }
    return mapEntries;
  }

  @NonNull private RedditEntry map(Child child) {
    String preview = null;
    if (child.data.preview != null
        && child.data.preview.images != null
        && child.data.preview.images.size() > 0) {
      preview = child.data.preview.images.get(0).source.url;
    }
    return RedditEntry.create(child.data.title, child.data.author,
        new Date(Long.valueOf(child.data.created) * 1000L), child.data.numComments,
        child.data.thumbnail, preview);
  }
}
