package bakingrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.example.alfa.bakingreciepes.R;

import static bakingrecipes.RecipeActivity.BAKING_KEY;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
private static String mBakingName;
    private static void updateAppWidget(@NonNull Context context, @NonNull AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
        RemoteViews views = setRemoteViewListView(context);
        views.setTextViewText(R.id.ingredientsName, mBakingName);
        if(!SharedPreferencesMethods.loadSavedPreferencesString(context,BAKING_KEY).equals("")) {

            Intent configIntent = new Intent(context, RecipeActivity.class);
            configIntent.putExtra("fromWidget", "");
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

            views.setOnClickPendingIntent(R.id.see_more, configPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views
            );
        }
    }


    @Override
    public void onUpdate(@NonNull Context context, @NonNull AppWidgetManager appWidgetManager, @NonNull int[] appWidgetIds) {
        updateIngredientsWidgets(context,appWidgetManager,appWidgetIds,mBakingName);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void updateIngredientsWidgets(@NonNull Context context, @NonNull AppWidgetManager appWidgetManager, int[] appWidgetIds, String bakingName ) {
        mBakingName=bakingName;

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @NonNull
    private static RemoteViews setRemoteViewListView(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        Intent intent = new Intent(context, BakingWidgetService.class);
        Bundle bundle = new Bundle();
//        ClassLoader loader = Step.class.getClassLoader();
//        intent.setExtrasClassLoader(loader);
//        bundle.putParcelableArrayList("key", mIngredients);
        intent.putExtras(bundle);
        remoteViews.setRemoteAdapter(R.id.appwidget_listView, intent);
        // Set the ReciepeActivity intent to launch when clicked
        Intent intentLaunched = new Intent(context, RecipeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentLaunched, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.appwidget_listView, pendingIntent);
//remoteViews.setEmptyView(R.id.appwidget_listView,R.id.em);

        Intent appIntent = new Intent(context, RecipeActivity.class);
//
        intent.putExtra("fromWidget",true);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.see_more, appPendingIntent);
        return remoteViews;
    }


}