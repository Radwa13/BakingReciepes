package bakingrecipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesMethods {
	
	public static void savePreferences(Context context, String key, int value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	
	public static int loadSavedPreferences(Context context, String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		int result = sharedPreferences.getInt(key, 0);
		return result;
	}
	
	
	
	public static void savePreferencesString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	
	public static String loadSavedPreferencesString(Context context, String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String result = sharedPreferences.getString(key, "");
		
				return result;
	}



	public static void savePreferencesFloat(Context context, String key, float value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static float loadSavedPreferencesFloat(Context context, String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		float result = sharedPreferences.getFloat(key,0);

		return result;
	}
	
}
