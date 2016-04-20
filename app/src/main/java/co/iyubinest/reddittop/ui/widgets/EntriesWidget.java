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
package co.iyubinest.reddittop.ui.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.iyubinest.reddittop.R;
import co.iyubinest.reddittop.data.entries.RedditEntry;
import co.iyubinest.reddittop.ui.ImageLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class EntriesWidget extends RecyclerView {
  private EndReachedListener endReachedistener;
  private LinearLayoutManager layoutManager;

  public EntriesWidget(Context context) {
    super(context);
    init();
  }

  public EntriesWidget(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public EntriesWidget(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    setAdapter(new Adapter());
    layoutManager = new LinearLayoutManager(getContext());
    setLayoutManager(layoutManager);
    addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) {
          if (layoutManager.findLastCompletelyVisibleItemPosition()
              == layoutManager.getItemCount() - 1) {
            if (endReachedistener != null) endReachedistener.onEndReached();
          }
        }
      }
    });
  }

  public void add(Collection<RedditEntry> entries) {
    ((Adapter) getAdapter()).add(entries);
    getAdapter().notifyDataSetChanged();
  }

  public void setEndReachedListener(EntriesWidget.EndReachedListener listener) {
    this.endReachedistener = listener;
  }

  public void setEntrySelectedListener(EntrySelectedListener listener) {
    ((Adapter) getAdapter()).setEntryListener(listener);
  }

  public interface EndReachedListener {
    void onEndReached();
  }

  public interface EntrySelectedListener {
    void onEntrySelected(RedditEntry entry);
  }

  protected interface OnSelectedListener {
    void onSelected(int position);
  }

  private class Adapter extends RecyclerView.Adapter<Holder> {

    private List<RedditEntry> entries = new ArrayList<>();
    private EntrySelectedListener entryListener;
    private final OnSelectedListener listener = new OnSelectedListener() {
      @Override public void onSelected(int position) {
        if (entryListener != null) entryListener.onEntrySelected(entries.get(position));
      }
    };

    public void setEntryListener(EntrySelectedListener entryListener) {
      this.entryListener = entryListener;
    }

    @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new Holder(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.entry, parent, false),
          listener);
    }

    @Override public void onBindViewHolder(Holder holder, int position) {
      holder.entry(entries.get(position));
    }

    @Override public int getItemCount() {
      return entries.size();
    }

    public void add(Collection<RedditEntry> entries) {
      this.entries.addAll(entries);
    }
  }

  private class Holder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView subtitle;
    private final ImageView thumbnail;

    public Holder(View view, final OnSelectedListener listener) {
      super(view);
      title = (TextView) view.findViewById(R.id.entry_title);
      subtitle = (TextView) view.findViewById(R.id.entry_subtitle);
      thumbnail = (ImageView) view.findViewById(R.id.entry_thumbnail);
      view.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          if (listener != null) listener.onSelected(getAdapterPosition());
        }
      });
    }

    public void entry(RedditEntry entry) {
      title.setText(entry.title());
      subtitle.setText(String.format("%s - %s", entry.author(),
          DateUtils.getRelativeDateTimeString(getContext(),
              new Date().getTime() - entry.date().getTime(), DateUtils.SECOND_IN_MILLIS,
              DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL)));
      ImageLoader.load(thumbnail, entry.thumbnail());
    }
  }
}
