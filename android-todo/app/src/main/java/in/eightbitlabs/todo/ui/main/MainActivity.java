package in.eightbitlabs.todo.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.eightbitlabs.todo.R;
import in.eightbitlabs.todo.ui.base.BaseActivity;
import in.eightbitlabs.todo.ui.tasks.TasksFragment;

import static in.eightbitlabs.todo.ui.tasks.TasksFragment.TASKS_DONE;
import static in.eightbitlabs.todo.ui.tasks.TasksFragment.TASKS_PENDING;
import static in.eightbitlabs.todo.ui.tasks.TasksFragment.TASKS_TYPE;

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject MainPresenter mMainPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mMainPresenter.attachView(this);
        setupViewPager(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(TasksFragment.newInstance(TASKS_PENDING), "Pending");
        adapter.addFragment(TasksFragment.newInstance(TASKS_DONE), "Done");

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMainPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

}
