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
package co.iyubinest.reddittop.di;

import co.iyubinest.reddittop.data.entries.DefaultEntriesRepo;
import co.iyubinest.reddittop.data.entries.EntriesCache;
import co.iyubinest.reddittop.data.entries.EntriesRepo;
import co.iyubinest.reddittop.data.entries.EntriesService;
import co.iyubinest.reddittop.data.entries.HttpEntriesRepo;
import co.iyubinest.reddittop.data.entries.MemoryEntriesCache;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public final class AppModule {

  private final String baseurl;

  public AppModule(String baseUrl) {
    this.baseurl = baseUrl;
  }

  @Singleton @Provides public EntriesService entriesService() {
    return new HttpEntriesRepo(HttpEntriesRepo.retrofit(baseurl));
  }

  @Singleton @Provides public EntriesCache entriesCache() {
    return new MemoryEntriesCache();
  }

  @Singleton @Provides public EntriesRepo entriesRepo(EntriesCache cache, EntriesService service) {
    return new DefaultEntriesRepo(cache, service);
  }
}
