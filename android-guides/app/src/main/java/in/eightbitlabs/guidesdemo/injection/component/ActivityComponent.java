package in.eightbitlabs.guidesdemo.injection.component;

import dagger.Subcomponent;
import in.eightbitlabs.guidesdemo.injection.PerActivity;
import in.eightbitlabs.guidesdemo.injection.module.ActivityModule;
import in.eightbitlabs.guidesdemo.ui.cart.CartActivity;
import in.eightbitlabs.guidesdemo.ui.guides.GuidesFragment;
import in.eightbitlabs.guidesdemo.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(CartActivity cartActivity);

    void inject(GuidesFragment guidesFragment);
}
