package in.eightbitlabs.guidesdemo.ui.main;

import android.os.Bundle;

import in.eightbitlabs.guidesdemo.R;
import in.eightbitlabs.guidesdemo.ui.base.BaseActivity;
import in.eightbitlabs.guidesdemo.ui.guides.GuidesFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, GuidesFragment.newInstance(GuidesFragment.GUIDES_ALL))
                    .commit();
        }
    }
}
