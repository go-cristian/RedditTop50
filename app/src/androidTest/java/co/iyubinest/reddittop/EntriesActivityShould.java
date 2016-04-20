package co.iyubinest.reddittop;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import co.iyubinest.reddittop.ui.entries.EntriesActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.IdlingPolicies.setIdlingResourceTimeout;
import static android.support.test.espresso.IdlingPolicies.setMasterPolicyTimeout;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.util.concurrent.TimeUnit.MINUTES;

@RunWith(AndroidJUnit4.class) @LargeTest public class EntriesActivityShould {
  @Rule public final ActivityTestRule<EntriesActivity> rule;

  private MockWebServer server;

  public EntriesActivityShould() {
    setMasterPolicyTimeout(10, MINUTES);
    setIdlingResourceTimeout(10, MINUTES);
    rule = new ActivityTestRule<>(EntriesActivity.class, false, false);
  }

  @Before public void before() throws Exception {
    server = new MockWebServer();
    server.start();
    ((App) getInstrumentation().getTargetContext().getApplicationContext()).baseUrl(
        server.url("/").toString());
  }

  @After public void after() throws Exception {
    server.shutdown();
  }

  @Test public void show_retry_on_failure() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500));
    rule.launchActivity(null);
    onView(withId(R.id.retry)).check(matches(withEffectiveVisibility(VISIBLE)));
    onView(withId(R.id.entries_wrapper)).check(matches(withEffectiveVisibility(GONE)));
  }

  @Test public void show_ui_on_success() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(200).setBody(fromAsset("top.json")));
    rule.launchActivity(null);
    onView(withId(R.id.entries_wrapper)).check(matches(withEffectiveVisibility(VISIBLE)));
    onView(withId(R.id.retry)).check(matches(withEffectiveVisibility(GONE)));
    onView(withId(R.id.entries)).perform(actionOnItemAtPosition(0, click()));
  }

  private String fromAsset(String assetName) throws Exception {
    InputStream stream = getContext().getResources().getAssets().open(assetName);
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder builder = new StringBuilder("");
    String line;
    while ((line = reader.readLine()) != null) {
      builder.append(line);
    }
    return builder.toString();
  }
}