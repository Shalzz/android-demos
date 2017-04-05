package in.eightbitlabs.guidesdemo.ui.guides;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.eightbitlabs.guidesdemo.R;
import in.eightbitlabs.guidesdemo.data.model.Data;
import in.eightbitlabs.guidesdemo.injection.ApplicationContext;

public class GuidesAdapter extends RecyclerView.Adapter<GuidesAdapter.DataViewHolder> {

    private List<Data> mData;
    private Context mContext;
    private Callback mCallback;
    private int mGuidesType;

    @Inject
    public GuidesAdapter(@ApplicationContext Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setGuidesType(int guidesType) {
        mGuidesType = guidesType;
    }

    public void setData(List<Data> data) {
        mData = data;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, int position) {
        Data data = mData.get(position);

        Glide.with(mContext)
                .load(data.icon())
                .into(holder.icon);

        holder.nameTextView.setText(data.name());
        holder.dateTextView.setText("Ending on: "+data.endDate());

        if(mGuidesType == GuidesFragment.GUIDES_CART) {
            holder.cartButton.setImageResource(R.drawable.ic_delete_black_24dp);
        } else {
            holder.cartButton.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
        }

        if(mCallback != null) {
            holder.cartButton.setOnClickListener(v -> mCallback.onCartClicked(data));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_icon) ImageView icon;
        @BindView(R.id.text_name) TextView nameTextView;
        @BindView(R.id.text_date) TextView dateTextView;
        @BindView(R.id.button_cart) ImageButton cartButton;

        public DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Callback {

        void onCartClicked(Data data);
    }
}
