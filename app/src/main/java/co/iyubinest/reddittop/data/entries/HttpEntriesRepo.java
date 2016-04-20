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

import co.iyubinest.reddittop.data.entries.entities.WebEntries;
import java.util.Collection;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HttpEntriesRepo implements EntriesRepo, Callback<WebEntries> {
  private final EntriesService service;
  private Callback callback;

  public HttpEntriesRepo(Retrofit retrofit) {
    service = retrofit.create(EntriesService.class);
  }

  @Override public void page(int number, Callback callback) {
    this.callback = callback;
    service.entries(1).enqueue(this);
  }

  @Override public void onResponse(Response<WebEntries> response) {
    if (response.isSuccess()) {
      callback.success(new MappedEntry(response.body()).value());
    } else {
      callback.failure();
    }
  }

  @Override public void onFailure(Throwable t) {
    callback.failure();
  }



  public static Retrofit retrofit(String baseUrl) {
    return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build();
  }
}
