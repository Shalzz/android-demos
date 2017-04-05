package in.eightbitlabs.guidesdemo.ui.guides;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.eightbitlabs.guidesdemo.R;
import in.eightbitlabs.guidesdemo.data.local.PreferencesHelper;
import in.eightbitlabs.guidesdemo.data.model.Data;
import in.eightbitlabs.guidesdemo.injection.ActivityContext;
import in.eightbitlabs.guidesdemo.ui.base.BaseActivity;
import in.eightbitlabs.guidesdemo.ui.cart.CartActivity;
import in.eightbitlabs.guidesdemo.util.DialogFactory;

/**
 * @author shalzz
 */

public class GuidesFragment extends Fragment implements GuidesMvpView, GuidesAdapter.Callback{

    public static final String GUIDES_TYPE = "tasks_type";
    public static final int GUIDES_ALL = 0;
    public static final int GUIDES_CART = 1;

    @Inject
    GuidesPresenter mGuidesPresenter;
    @Inject
    GuidesAdapter mGuidesAdapter;
    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    @ActivityContext
    Context mContext;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int mGuidesType;
    private Unbinder unbinder;

    public static GuidesFragment newInstance(int type) {
        GuidesFragment f = new GuidesFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(GUIDES_TYPE, type);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGuidesType = getArguments().getInt(GUIDES_TYPE);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_guides, container, false);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);

        mGuidesAdapter.setCallback(this);
        mGuidesAdapter.setGuidesType(mGuidesType);
        mRecyclerView.setAdapter(mGuidesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mGuidesPresenter.attachView(this);

        mSwipeRefreshLayout.setOnRefreshListener(() -> mGuidesPresenter.syncGuides());

        if(mPreferencesHelper.isFirstBoot()) {
            mGuidesPresenter.syncGuides();
        } else if (mGuidesType == GUIDES_ALL) {
            mGuidesPresenter.loadGuides();
        } else {
            mGuidesPresenter.loadGuidesinCart();
            mSwipeRefreshLayout.setEnabled(false);
        }

        return view;
    }

    @Override
    public void onCartClicked(Data data) {
        Data newData = Data.create(data, mGuidesType == GUIDES_ALL);
        mGuidesPresenter.updateData(newData);
        String msg = mGuidesType == GUIDES_CART ? "Removed from cart" : "Added to cart";
        Snackbar.make(mRecyclerView,msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mGuidesType == GUIDES_ALL)
            inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                startActivity(new Intent(mContext, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mGuidesPresenter.detachView();
        unbinder.unbind();
    }

    /***** MVP View methods implementation *****/

    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void dismissLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void showGuides(List<Data> data) {
        mGuidesAdapter.setData(data);
        mGuidesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(mContext, getString(R.string.error_loading_guides))
                .show();
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mRecyclerView, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showGuidesEmpty() {
        mGuidesAdapter.setData(Collections.emptyList());
        mGuidesAdapter.notifyDataSetChanged();
        Toast.makeText(mContext, R.string.empty_ribots, Toast.LENGTH_LONG).show();
    }
}
