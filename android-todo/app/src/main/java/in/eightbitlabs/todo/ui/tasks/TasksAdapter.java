package in.eightbitlabs.todo.ui.tasks;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.eightbitlabs.todo.R;
import in.eightbitlabs.todo.data.model.Data;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Data> mData;
    private Callback mCallback;
    private MultiSelector mMultiSelector = new MultiSelector();
    private ModalMultiSelectorCallback mActionModeCallback;
    private Activity mActivity;

    @Inject
    public TasksAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<>();
        mActionModeCallback
                = new ModalMultiSelectorCallback(mMultiSelector) {

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                super.onCreateActionMode(actionMode, menu);
                mActivity.getMenuInflater().inflate(R.menu.menu_context, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_delete) {
                    actionMode.finish();

                    for (int i = mData.size(); i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) { // (1)
                            mCallback.onTaskDeleted(mData.get(i));
                        }
                    }

                    mMultiSelector.clearSelections(); // (2)
                    return true;

                }
                return false;
            }
        };
    }

    public void setTasks(List<Data> data) {
        mData = data;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tasks, parent, false);
        TaskViewHolder holder = new TaskViewHolder(itemView);
        holder.setSelectionModeStateListAnimator(null);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {
        Data data = mData.get(position);
        holder.taskTextView.setText(data.name());
        if(mCallback != null)
            holder.cardView.setOnClickListener( v -> {
                if (!mMultiSelector.tapSelection(holder))
                    mCallback.onTaskClicked(data);
            });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TaskViewHolder extends SwappingHolder
            implements View.OnLongClickListener {

        @BindView(R.id.text_task) TextView taskTextView;
        @BindView(R.id.task_view) FrameLayout cardView;

        TaskViewHolder(View itemView) {
            super(itemView, mMultiSelector);
            ButterKnife.bind(this, itemView);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if (!mMultiSelector.isSelectable()) {
                ((AppCompatActivity) mActivity).startSupportActionMode(mActionModeCallback);
                mMultiSelector.setSelectable(true);
                mMultiSelector.setSelected(TaskViewHolder.this, true);
                return true;
            }
            return false;
        }
    }

    interface Callback {

        void onTaskClicked(Data task);

        void onTaskDeleted(Data task);
    }
}
