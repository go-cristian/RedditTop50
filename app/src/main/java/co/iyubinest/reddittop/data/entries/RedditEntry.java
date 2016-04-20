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

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import java.util.Date;

@AutoValue public abstract class RedditEntry {
  public static RedditEntry create(String title, String author, Date date, int comments,
      @Nullable String thumbnail, @Nullable String preview) {
    return new AutoValue_RedditEntry(title, author, date, comments, thumbnail, preview);
  }

  public abstract String title();

  public abstract String author();

  public abstract Date date();

  public abstract int comments();

  public abstract @Nullable String thumbnail();

  public abstract @Nullable String preview();
}
