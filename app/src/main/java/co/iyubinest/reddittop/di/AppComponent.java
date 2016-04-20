/*************************************************************************
 * CONFIDENTIAL
 * __________________
 *
 * [2016] All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of {The Company} and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to {The Company}
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from {The Company}.
 */
package co.iyubinest.reddittop.di;

import co.iyubinest.reddittop.ui.entries.EntriesActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = AppModule.class) public interface AppComponent {
  void inject(EntriesActivity activity);
}
