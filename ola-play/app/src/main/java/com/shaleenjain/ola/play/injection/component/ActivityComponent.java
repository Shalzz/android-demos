package com.shaleenjain.ola.play.injection.component;


import com.shaleenjain.ola.play.injection.PerActivity;
import com.shaleenjain.ola.play.injection.module.ActivityModule;
import com.shaleenjain.ola.play.ui.FullScreenPlayerActivity;
import com.shaleenjain.ola.play.ui.MediaBrowserFragment;
import com.shaleenjain.ola.play.ui.PlaybackControlsFragment;
import com.shaleenjain.ola.play.ui.SearchResultsActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(PlaybackControlsFragment playbackControlsFragment);

    void inject(FullScreenPlayerActivity fullScreenPlayerActivity);

    void inject(SearchResultsActivity searchResultsActivity);

    void inject(MediaBrowserFragment mediaBrowserFragment);
}
