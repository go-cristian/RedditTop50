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

import java.util.Collection;
import java.util.HashMap;

public class MemoryEntriesCache implements EntriesCache {

  private HashMap<Integer, Collection<RedEntry>> cache = new HashMap<>();

  @Override public boolean has(int number) {
    return cache.containsKey(number);
  }

  @Override public Collection<RedEntry> get(int number) {
    return cache.get(number);
  }

  @Override public void save(int number, Collection<RedEntry> entries) {
    if (entries != null) cache.put(number, entries);
  }
}
