package bakingrecipes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.alfa.bakingreciepes.R;
import com.google.gson.Gson;

import bakingrecipes.Data.Example;

import static bakingrecipes.RecipeActivity.BAKING_KEY;

/**
 * Created by Alfa on 5/26/2018.
 */


public class BakingWidgetService extends RemoteViewsService {
    Example mBaking;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Gson gson = new Gson();
        String json = SharedPreferencesMethods.loadSavedPreferencesString(this.getApplicationContext(),BAKING_KEY);
       mBaking = gson.fromJson(json, Example.class);

        return new BakingRemoteFactory(this.getApplicationContext());
    }

    public class BakingRemoteFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        String mBakingName;


        public BakingRemoteFactory(Context context) {
            mContext = context;


        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mBaking.getIngredients().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {


            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item);
            remoteViews.setTextColor(R.id.quantity, Color.BLACK);
            remoteViews.setTextColor(R.id.name, Color.BLACK);

            String quantityMeasure=mBaking.getIngredients().get(position).getQuantity()+"  "+mBaking.getIngredients().get(position).getMeasure();
            remoteViews.setTextViewText(R.id.quantity, quantityMeasure);
            remoteViews.setTextViewText(R.id.name, mBaking.getIngredients().get(position).getIngredient());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
