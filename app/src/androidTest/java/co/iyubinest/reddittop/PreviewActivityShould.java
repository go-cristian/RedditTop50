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

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import co.iyubinest.reddittop.data.entries.RedEntry;
import co.iyubinest.reddittop.ui.preview.PreviewActivity;
import com.squareup.spoon.Spoon;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.IdlingPolicies.setIdlingResourceTimeout;
import static android.support.test.espresso.IdlingPolicies.setMasterPolicyTimeout;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.util.concurrent.TimeUnit.MINUTES;

public class PreviewActivityShould {
  @Rule public ActivityTestRule<PreviewActivity> rule;

  public PreviewActivityShould() {
    setMasterPolicyTimeout(10, MINUTES);
    setIdlingResourceTimeout(10, MINUTES);
    rule = new ActivityTestRule<>(PreviewActivity.class, false, false);
  }

  @Before public void before() {
    String imageUrl =
        "https://pixabay.com/static/uploads/photo/2015/10/01/21/39/background-image-967820_960_720.jpg";
    RedEntry entry = RedEntry.create("title", "author", new Date(), 5, imageUrl, imageUrl);
    View view = new View(getTargetContext());
    rule.launchActivity(PreviewActivity.getIntent(getTargetContext(), entry, view));
  }

  @Test public void show_screen() {
    onView(withId(R.id.toolbar)).check(matches(withEffectiveVisibility(VISIBLE)));
    Spoon.screenshot(rule.getActivity(), "initial_state");
    onView(withId(R.id.preview_root)).check(matches(withEffectiveVisibility(VISIBLE)));
    onView(withId(R.id.preview_image)).check(matches(withEffectiveVisibility(VISIBLE)));
  }

  @Test public void save_image() {
    Spoon.screenshot(rule.getActivity(), "initial_state");
    onView(withId(R.id.preview_save)).perform(ViewActions.click());
    //TODO: Check for image being saved
  }
}
