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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import co.iyubinest.reddittop.App;
import co.iyubinest.reddittop.di.ActivityComponent;
import co.iyubinest.reddittop.di.ActivityModule;
import co.iyubinest.reddittop.di.AppComponent;
import co.iyubinest.reddittop.di.DaggerActivityComponent;

public class BaseActivity extends AppCompatActivity {

  private ActivityComponent injector;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injector = DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build();
  }

  public App getApp() {
    return (App) getApplication();
  }

  public AppComponent injector() {
    return getApp().injector();
  }

  public ActivityComponent appInjector() {
    return injector;
  }
}
