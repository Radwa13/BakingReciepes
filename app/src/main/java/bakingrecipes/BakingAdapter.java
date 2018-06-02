package bakingrecipes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alfa.bakingreciepes.*;

import java.util.ArrayList;

import bakingrecipes.Data.Example;
import bakingrecipes.idilingResources.SimpleIdlingResources;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alfa on 5/9/2018.
 */

public class BakingAdapter extends RecyclerView.Adapter<BakingAdapter.BakingViewHolder> {
    private ArrayList<Example> mBakingList;
    final private ListItemClickListner mClickHandler;
    private Context mContext;


    public interface ListItemClickListner {
        void onClick(int position);
    }

    public BakingAdapter(Context context, ListItemClickListner clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }


    @Override
    public BakingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int listLayoutId = R.layout.card_view_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(listLayoutId, parent, false);
        return new BakingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BakingViewHolder holder, int position) {
        holder.mTextView.setText(mBakingList.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return mBakingList.size();
    }

    public class BakingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.bakingName)
        TextView mTextView;

        public BakingViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);

        }

        @Override
        public void onClick(View view) {
            int postion = getAdapterPosition();
            mClickHandler.onClick(postion);
        }

    }

    public void loadData(ArrayList<Example> bakingList) {
        this.mBakingList = bakingList;

    }
}
