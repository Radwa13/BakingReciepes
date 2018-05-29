package bakingrecipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alfa.bakingreciepes.*;

import java.util.ArrayList;

import bakingrecipes.Data.Step;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alfa on 5/9/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private ArrayList<Step> mStepList;
    final private ListItemClickListner mClickHandler;
    private Context mContext;
    ListItemClickListner mListItemClickListner;
    public interface ListItemClickListner {
        void onClick(int position);

    }

    public StepsAdapter(Context context, ListItemClickListner clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mListItemClickListner=(ListItemClickListner)context;
    }


    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int listLayoutId = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(listLayoutId, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.mTextView.setText(mStepList.get(position).getShortDescription());

    }


    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.shortDesc)
        TextView mTextView;



        public StepsViewHolder(View itemView) {
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

    public void loadData(ArrayList<Step> stepList) {
        this.mStepList = stepList;

    }
}
