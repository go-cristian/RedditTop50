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

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.iyubinest.reddittop.R;
import co.iyubinest.reddittop.data.entries.RedEntry;
import co.iyubinest.reddittop.ui.BaseActivity;
import co.iyubinest.reddittop.ui.ImageLoader;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;

public class PreviewActivity extends BaseActivity implements PreviewView {

  public static final String ORIENTATION = "orientation";
  public static final String LEFT = "left";
  public static final String TOP = "top";
  public static final String WIDTH = "width";
  public static final String HEIGHT = "height";
  private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
  private static final int ANIM_DURATION = 500;
  private static final String ENTRY = "entry";
  static float sAnimatorScale = 1;
  @Bind(R.id.preview_root) View rootView;
  @Bind(R.id.preview_image) ImageView previewView;
  @Bind(R.id.toolbar) Toolbar toolbarView;
  @Inject PreviewRepo repo;
  private PreviewSource source;
  private int originalOrientation;
  private int leftDelta;
  private int topDelta;
  private float widthScale;
  private float heightScale;

  public static Intent getIntent(Context context, RedEntry entry, int[] screenLocation, int width,
      int height) {
    assert entry == null;
    int orientation = context.getResources().getConfiguration().orientation;

    Intent intent = new Intent(context, PreviewActivity.class);
    Bundle bundle = new Bundle();
    bundle.putParcelable(ENTRY, entry);

    intent.
        putExtra(ORIENTATION, orientation).
        putExtra(LEFT, screenLocation[0]).
        putExtra(TOP, screenLocation[1]).
        putExtra(WIDTH, width).
        putExtra(HEIGHT, height).putExtras(bundle);

    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preview);
    ButterKnife.bind(this);
    appinjector().inject(this);
    source = new PreviewSource(this, repo);
    source.init((RedEntry) getIntent().getParcelableExtra(ENTRY));
    configureActionBar();
  }

  public void runEnterAnimation() {
    final long duration = (long) (ANIM_DURATION * sAnimatorScale);

    previewView.setPivotX(0);
    previewView.setPivotY(0);
    previewView.setScaleX(widthScale);
    previewView.setScaleY(heightScale);
    previewView.setTranslationX(leftDelta);
    previewView.setTranslationY(topDelta);

    previewView.animate().setDuration(duration).
        scaleX(1).scaleY(1).
        translationX(0).translationY(0).
        setInterpolator(sDecelerator);

    ObjectAnimator bgAnim = ObjectAnimator.ofInt(rootView, "alpha", 0, 255);
    bgAnim.setDuration(duration);
    bgAnim.start();
  }

  public void runExitAnimation(final Runnable endAction) {
    final long duration = (long) (ANIM_DURATION * sAnimatorScale);

    final boolean fadeOut;
    if (getResources().getConfiguration().orientation != originalOrientation) {
      previewView.setPivotX(previewView.getWidth() / 2);
      previewView.setPivotY(previewView.getHeight() / 2);
      leftDelta = 0;
      topDelta = 0;
      fadeOut = true;
    } else {
      fadeOut = false;
    }

    previewView.animate().setDuration(duration).
        scaleX(widthScale).scaleY(heightScale).
        translationX(leftDelta).translationY(topDelta);

    if (fadeOut) {
      previewView.animate().alpha(0);
    }
    ObjectAnimator bgAnim = ObjectAnimator.ofInt(rootView, "alpha", 0);
    bgAnim.setDuration(duration);
    bgAnim.start();

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override public void run() {
        endAction.run();
      }
    }, duration);
  }

  @Override public void onBackPressed() {
    runExitAnimation(new Runnable() {
      public void run() {
        finish();
      }
    });
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.preview, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.preview_save) {
      source.saveImage();
    }
    return false;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }

  @Override public void showImage(String imageUrl) {
    ImageLoader.load(previewView, imageUrl, new ImageLoader.Callback() {
      @Override public void ready() {
        prepareanimation();
      }
    });
  }

  private void prepareanimation() {
    Bundle bundle = getIntent().getExtras();
    final int thumbnailTop = bundle.getInt(TOP);
    final int thumbnailLeft = bundle.getInt(LEFT);
    final int thumbnailWidth = bundle.getInt(WIDTH);
    final int thumbnailHeight = bundle.getInt(HEIGHT);
    originalOrientation = bundle.getInt(ORIENTATION);

    ViewTreeObserver observer = previewView.getViewTreeObserver();
    observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

      @Override public boolean onPreDraw() {
        previewView.getViewTreeObserver().removeOnPreDrawListener(this);

        int[] screenLocation = new int[2];
        previewView.getLocationOnScreen(screenLocation);
        leftDelta = thumbnailLeft - screenLocation[0];
        topDelta = thumbnailTop - screenLocation[1];

        widthScale = (float) thumbnailWidth / previewView.getWidth();
        heightScale = (float) thumbnailHeight / previewView.getHeight();

        runEnterAnimation();

        return true;
      }
    });
  }

  @Override public void showSaveImageError() {
    Snackbar.make(previewView, R.string.error, Snackbar.LENGTH_SHORT).show();
  }

  @Override public void showSaveImageGreeting() {
    Snackbar.make(previewView, R.string.preview_saved, Snackbar.LENGTH_SHORT).show();
  }

  private void configureActionBar() {
    setSupportActionBar(toolbarView);
    setTitle(null);
    toolbarView.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
    toolbarView.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
  }
}
