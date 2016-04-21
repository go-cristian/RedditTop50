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
package co.iyubinest.reddittop.data.preview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import co.iyubinest.reddittop.ui.BaseActivity;
import co.iyubinest.reddittop.ui.preview.PreviewRepo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.util.Date;

public class GlideMediaStoreRepo implements PreviewRepo {

  private final BaseActivity activity;

  public GlideMediaStoreRepo(BaseActivity activity) {
    this.activity = activity;
  }

  @Override public void save(String imageUrl, Callback callback) {
    Glide.with(activity).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          if (!checkPermission()) {
            requestPermission();
          } else {
            saveResource(bitmap);
          }
        } else {
          saveResource(bitmap);
        }
      }
    });
  }

  private void saveResource(Bitmap bitmap) {
    MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap,
        new Date().toString(), "");
  }

  private void requestPermission() {
    ActivityCompat.requestPermissions(activity,
        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
  }

  @SuppressLint("NewApi") private boolean checkPermission() {
    return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED;
  }
}
