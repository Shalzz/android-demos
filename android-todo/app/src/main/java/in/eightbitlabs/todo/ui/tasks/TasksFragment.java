package in.eightbitlabs.todo.ui.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.eightbitlabs.todo.R;
import in.eightbitlabs.todo.data.local.PreferencesHelper;
import in.eightbitlabs.todo.data.model.Data;
import in.eightbitlabs.todo.injection.ActivityContext;
import in.eightbitlabs.todo.ui.main.MainActivity;
import in.eightbitlabs.todo.util.DialogFactory;
import in.eightbitlabs.todo.util.DividerItemDecoration;

/**
 * @author shalzz
 */

public class TasksFragment extends Fragment implements TaskMvpView, TasksAdapter.Callback {

    public static final String TASKS_TYPE = "tasks_type";
    public static final int TASKS_PENDING = 0;
    public static final int TASKS_DONE = 1;

    @Inject
    TasksPresenter mTasksPresenter;
    @Inject
    TasksAdapter mTasksAdapter;
    @Inject
    PreferencesHelper mPreferenceHelper;
    @Inject
    @ActivityContext
    Context mContext;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private int mTasksType;
    private Unbinder unbinder;

    public static TasksFragment newInstance(int type) {
        TasksFragment f = new TasksFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(TASKS_TYPE, type);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksType = getArguments().getInt(TASKS_TYPE);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_tasks, container, false);
        ((MainActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, view);

        mTasksPresenter.attachView(this);
        mTasksAdapter.setCallback(this);
        mRecyclerView.setAdapter(mTasksAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mTasksPresenter.loadTasks(mTasksType);

        mSwipeRefreshLayout.setOnRefreshListener(() -> mTasksPresenter.syncTasks());

        if(mPreferenceHelper.isFirstBoot() && mTasksType == 0) {
            mTasksPresenter.syncTasks();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mTasksPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mTasksType == TASKS_PENDING)
            inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.dialog_add, null);
                EditText task = (EditText) dialogView.findViewById(R.id.edit_task);
                new AlertDialog.Builder(mContext)
                        .setView(dialogView)
                        .setCancelable(false)
                        .setPositiveButton("Add Task",
                                (dialog1, id) -> {
                                    if(task.getText().length() != 0)
                                        mTasksPresenter.addTask(task.getText().toString());
                                })
                        .setNegativeButton("Cancel",
                                (dialog2, id) -> dialog2.cancel())
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskClicked(Data task) {
        Data newTask = Data.create(task.id(),task.name(), mTasksType == 1 ? 0 : 1);
        mTasksPresenter.updateTask(newTask);
        String msg = mTasksType == 1 ? "Marked as pending" : "Marked as done";
        Snackbar.make(mRecyclerView,msg,Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> mTasksPresenter.updateTask(task))
                .show();
    }

    @Override
    public void onTaskDeleted(Data task) {
        mTasksPresenter.deleteTask(task);
    }

    /***** MVP View methods implementation *****/

    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void dismissLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void showTasks(List<Data> tasks) {
        mTasksAdapter.setTasks(tasks);
        mTasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String msg) {
        DialogFactory.createGenericErrorDialog(mContext, msg)
                .show();
    }

    @Override
    public void showTasksEmpty() {
        mTasksAdapter.setTasks(Collections.emptyList());
        mTasksAdapter.notifyDataSetChanged();
        Toast.makeText(mContext, R.string.empty_tasks, Toast.LENGTH_LONG).show();
    }
}
