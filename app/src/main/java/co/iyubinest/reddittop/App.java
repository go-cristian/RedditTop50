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
package co.iyubinest.reddittop;

import android.app.Application;
import android.support.annotation.VisibleForTesting;
import co.iyubinest.reddittop.di.AppComponent;
import co.iyubinest.reddittop.di.AppModule;
import co.iyubinest.reddittop.di.DaggerAppComponent;

public class App extends Application {

  private AppComponent injector;

  @Override public void onCreate() {
    super.onCreate();
    injector = DaggerAppComponent.builder().appModule(new AppModule(BuildConfig.BASE_URL)).build();
  }

  public AppComponent injector() {
    return injector;
  }

  @VisibleForTesting public void baseUrl(String baseUrl) {
    //This is workaround for creating the UI Tests with MockWebServer, so the reason for accepting
    // this at this point is close related to the fact of get a connection of MockWebServer
    injector = DaggerAppComponent.builder().appModule(new AppModule(baseUrl)).build();
  }
}
