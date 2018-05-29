package bakingrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.alfa.bakingreciepes.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import bakingrecipes.Data.Ingredient;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
static String mBakingName;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = setRemoteViewListView(context);
        views.setTextViewText(R.id.ingredientsName, mBakingName);

        appWidgetManager.updateAppWidget(appWidgetId, views
        );

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,String bakingName ) {
        mBakingName=bakingName;

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


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
        return remoteViews;
    }


}