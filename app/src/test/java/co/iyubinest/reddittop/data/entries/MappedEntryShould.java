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

import co.iyubinest.reddittop.data.entries.entities.Child;
import co.iyubinest.reddittop.data.entries.entities.Data;
import co.iyubinest.reddittop.data.entries.entities.Data_;
import co.iyubinest.reddittop.data.entries.entities.Images;
import co.iyubinest.reddittop.data.entries.entities.Preview;
import co.iyubinest.reddittop.data.entries.entities.Source;
import co.iyubinest.reddittop.data.entries.entities.WebEntries;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class MappedEntryShould {

  private static final WebEntries CURRENT = new WebEntries();
  private static final List<RedEntry> EXPECTED = new ArrayList<>(1);

  static {
    Child child = new Child();
    child.data = new Data_();
    child.data.title = "Title";
    child.data.author = "Author";
    child.data.created = 1461110121;
    child.data.numComments = 15;
    child.data.thumbnail = "some url";
    Preview preview = new Preview();
    preview.images = new ArrayList<>(1);
    Images image = new Images();
    Source source = new Source();
    source.url = "another url";
    image.source = source;
    preview.images.add(image);
    child.data.preview = preview;
    CURRENT.data = new Data();

    CURRENT.data.children.add(child);

    EXPECTED.add(RedEntry.create("Title", "Author", new Date(1461110121L * 1000L), 15, "some url",
        "another url"));
  }

  @Test public void return_empty_when_invalid() throws Exception {
    assertThat(new MappedEntry(null).value(), is(notNullValue()));
    assertThat(new MappedEntry(null).value().size(), is(equalTo(0)));
  }

  @Test public void return_right_conversion() throws Exception {
    assertArrayEquals(new MappedEntry(CURRENT).value().toArray(), EXPECTED.toArray());
  }
}
