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
package co.iyubinest.reddittop.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class ImageLoader {

  public static void background(final View view, String imageUrl) {
    Glide.with(view.getContext()).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        Drawable drawable = new BitmapDrawable(resource);
        view.setBackgroundDrawable(drawable);
      }
    });
  }
}
