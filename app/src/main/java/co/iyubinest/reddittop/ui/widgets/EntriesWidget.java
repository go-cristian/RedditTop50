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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.iyubinest.reddittop.R;
import co.iyubinest.reddittop.data.entries.RedEntry;
import co.iyubinest.reddittop.ui.ImageLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntriesWidget extends RecyclerView {
  private EndReachedListener endReachedistener;
  private StaggeredGridLayoutManager layoutManager;

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
    final int rows = getResources().getInteger(R.integer.rows);
    layoutManager = new StaggeredGridLayoutManager(rows, StaggeredGridLayoutManager.VERTICAL);
    setHasFixedSize(false);
    setLayoutManager(layoutManager);
    addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0) {
          if (layoutManager.findLastVisibleItemPositions(new int[rows])[0]
              == layoutManager.getItemCount() - rows) {
            if (endReachedistener != null) endReachedistener.onEndReached();
          }
        }
      }
    });
  }

  public void add(Collection<RedEntry> entries) {
    ((Adapter) getAdapter()).add(entries);
    getAdapter().notifyDataSetChanged();
  }

  public void setEndReachedListener(EntriesWidget.EndReachedListener listener) {
    this.endReachedistener = listener;
  }

  public void setEntrySelectedListener(EntrySelectedListener listener) {
    ((Adapter) getAdapter()).setEntryListener(listener);
  }

  public void clear() {
    ((Adapter) getAdapter()).clear();
  }

  public interface EndReachedListener {
    void onEndReached();
  }

  public interface EntrySelectedListener {
    void onEntrySelected(RedEntry entry, View view);
  }

  protected interface OnSelectedListener {
    void onSelected(int position, View view);
  }

  private class Adapter extends RecyclerView.Adapter<Holder> {

    private List<RedEntry> entries = new ArrayList<>();
    private EntrySelectedListener entryListener;
    private final OnSelectedListener listener = new OnSelectedListener() {
      @Override public void onSelected(int position, View view) {
        if (entryListener != null) entryListener.onEntrySelected(entries.get(position), view);
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

    public void add(Collection<RedEntry> entries) {
      this.entries.addAll(entries);
    }

    public void clear() {
      this.entries.clear();
    }
  }

  private class Holder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView subtitle;
    private final TextView comments;
    private final ImageView thumbnail;

    public Holder(View view, final OnSelectedListener listener) {
      super(view);
      title = (TextView) view.findViewById(R.id.entry_title);
      subtitle = (TextView) view.findViewById(R.id.entry_subtitle);
      comments = (TextView) view.findViewById(R.id.entry_comments);
      thumbnail = (ImageView) view.findViewById(R.id.entry_thumbnail);
      view.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          if (listener != null) listener.onSelected(getAdapterPosition(), itemView);
        }
      });
    }

    public void entry(RedEntry entry) {
      title.setText(entry.title());
      subtitle.setText(getResources().getString(R.string.entry_subtitle_format, entry.author(),
          DateUtils.getRelativeDateTimeString(getContext(), entry.date().getTime(),
              DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL)));
      comments.setText(
          getResources().getQuantityString(R.plurals.comments, entry.comments(), entry.comments()));
      if (entry.thumbnail() != null && entry.thumbnail().length() > 1) {
        ImageLoader.load(thumbnail, entry.thumbnail());
      } else {
        thumbnail.setVisibility(GONE);
      }
    }
  }
}
