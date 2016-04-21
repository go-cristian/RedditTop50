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

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.iyubinest.reddittop.R;
import co.iyubinest.reddittop.data.entries.EntriesRepo;
import co.iyubinest.reddittop.data.entries.RedEntry;
import co.iyubinest.reddittop.ui.BaseActivity;
import co.iyubinest.reddittop.ui.preview.PreviewActivity;
import co.iyubinest.reddittop.ui.widgets.EntriesWidget;
import java.util.Collection;
import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class EntriesActivity extends BaseActivity
    implements EntriesView, EntriesWidget.EndReachedListener, EntriesWidget.EntrySelectedListener {

  @Bind(R.id.loading) View loadingView;
  @Bind(R.id.retry) View retryView;
  @Bind(R.id.entries_wrapper) View entriesWrapper;
  @Bind(R.id.entries) EntriesWidget entriesView;
  @Bind(R.id.toolbar_title) TextView titleView;
  @Bind(R.id.refresh) SwipeRefreshLayout refreshView;
  @Inject EntriesRepo repo;
  private EntriesSource source;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_entries);
    ButterKnife.bind(this);
    injector().inject(this);
    source = new EntriesSource(this, repo);
    source.request();
    titleView.setText(R.string.app_name);
    refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        source.update();
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }

  @Override public void showLoading() {
    show(loadingView);
  }

  @Override public void showRetry() {
    show(retryView);
  }

  @Override public void render(Collection<RedEntry> entries) {
    show(entriesWrapper);
    entriesView.add(entries);
    entriesView.setEndReachedListener(this);
    entriesView.setEntrySelectedListener(this);
    refreshView.setRefreshing(false);
  }

  @Override public void showRetryCell() {
    Snackbar.make(entriesWrapper, R.string.error, Snackbar.LENGTH_SHORT)
        .setAction(R.string.retry_button, new View.OnClickListener() {
          @Override public void onClick(View v) {
            source.request();
          }
        })
        .show();
  }

  @Override public void showLoadingCell() {
    Snackbar.make(entriesWrapper, R.string.loading, Snackbar.LENGTH_SHORT).show();
  }

  @Override public void showUpdating() {
    refreshView.setRefreshing(true);
  }

  @Override public void clearEntries() {
    entriesView.clear();
  }

  @Override public void onEndReached() {
    source.request();
  }

  private void show(View view) {
    loadingView.setVisibility(GONE);
    retryView.setVisibility(GONE);
    entriesWrapper.setVisibility(GONE);
    view.setVisibility(VISIBLE);
  }

  @Override public void onEntrySelected(RedEntry entry, View view) {
    if (entry.preview() != null) {
      View imageToAnimate = view.findViewById(R.id.entry_thumbnail);
      int[] screenLocation = new int[2];
      imageToAnimate.getLocationOnScreen(screenLocation);
      startActivity(
          PreviewActivity.getIntent(this, entry, screenLocation, imageToAnimate.getWidth(),
              imageToAnimate.getHeight()));
      overridePendingTransition(0, 0);
    }
  }
}
