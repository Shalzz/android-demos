package in.eightbitlabs.guidesdemo.ui.cart;

import android.os.Bundle;

import in.eightbitlabs.guidesdemo.R;
import in.eightbitlabs.guidesdemo.ui.base.BaseActivity;
import in.eightbitlabs.guidesdemo.ui.guides.GuidesFragment;

/**
 * @author shalzz
 */

public class CartActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, GuidesFragment.newInstance(GuidesFragment.GUIDES_CART))
                    .commit();
        }
    }
}
