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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HttpEntriesShould {
  @Mock EntriesRepo.Callback callback;
  private MockWebServer server;
  private HttpEntriesRepo repo;

  @Before public void before() throws Exception {
    initMocks(this);
    server = new MockWebServer();
    server.start();
    repo = new HttpEntriesRepo(HttpEntriesRepo.retrofit(server.url("/").toString()));
  }

  @Test public void throw_error_on_failure() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500));
    repo.get(0, callback);
    Thread.sleep(200);
    verify(callback, times(1)).failure();
  }

  @Test public void parse_items_on_success() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(200).setBody(fromFile("top.json")));
    repo.get(0, callback);
    Thread.sleep(200);
    verify(callback, times(1)).success(anyListOf(RedEntry.class));
  }

  private String fromFile(String path) throws IOException {
    InputStream stream = getClass().getResourceAsStream("/" + path);
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder builder = new StringBuilder("");
    String line;
    while ((line = reader.readLine()) != null) {
      builder.append(line);
    }
    return builder.toString();
  }
}
