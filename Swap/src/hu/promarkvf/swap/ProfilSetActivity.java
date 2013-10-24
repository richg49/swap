package hu.promarkvf.swap;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;

public class ProfilSetActivity extends PreferenceActivity {
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	static Context               cont;
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		this.setupSimplePreferencesScreen();
		cont = ProfilSetActivity.this;
		
	}
	
	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}
		this.addPreferencesFromResource(R.xml.pref_general);
		for (int i = 0; i < this.getPreferenceScreen().getPreferenceCount(); i++) {
			Preference pref = this.getPreferenceScreen().getPreference(i);
			if (pref instanceof PreferenceCategory) {
				PreferenceCategory prefc = (PreferenceCategory) pref;
				for (int j = 0; j < prefc.getPreferenceCount(); j++) {
					Preference pref1 = prefc.getPreference(j);
					bindPreferenceSummaryToValue(pref1);
				}
				
			} else {
				bindPreferenceSummaryToValue(pref);
			}
			
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}
	
	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}
	
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || !isXLargeTablet(context);
	}
	
	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			this.loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}
	
	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPrefSListener = new Preference.OnPreferenceChangeListener() {
		                                                                        @Override
		                                                                        public boolean onPreferenceChange(Preference preference, Object value) {
			                                                                        String stringValue = value.toString();
			                                                                        
			                                                                        if (preference instanceof ListPreference) {
				                                                                        ListPreference listPreference = (ListPreference) preference;
				                                                                        int index = listPreference.findIndexOfValue(stringValue);
				                                                                        
				                                                                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
				                                                                        
			                                                                        } else {
				                                                                        preference.setSummary(stringValue);
			                                                                        }
			                                                                        return true;
		                                                                        }
	                                                                        };
	
	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPrefSListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPrefSListener);
		
		// Trigger the listener immediately with the preference's
		// current value.
		sBindPrefSListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
	}
	
	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.addPreferencesFromResource(R.xml.pref_general);
			for (int i = 0; i < this.getPreferenceScreen().getPreferenceCount(); i++) {
				Preference pref = this.getPreferenceScreen().getPreference(i);
				if (pref instanceof PreferenceCategory) {
					PreferenceCategory prefc = (PreferenceCategory) pref;
					for (int j = 0; j < prefc.getPreferenceCount(); j++) {
						Preference pref1 = prefc.getPreference(j);
						bindPreferenceSummaryToValue(pref1);
					}
					
				} else {
					bindPreferenceSummaryToValue(pref);
				}
				
			}
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SwapActivity.profil.setEmail(mySharedPreferences.getString("prefemail", ""));
		SwapActivity.profil.setName(mySharedPreferences.getString("prefname", ""));
		SwapActivity.profil.setRname(mySharedPreferences.getString("prefrname", ""));
		SwapActivity.profil.setAddress_postcode(mySharedPreferences.getString("prefaddress_postcode", ""));
		SwapActivity.profil.setAddress_city(mySharedPreferences.getString("prefaddress_city", ""));
		SwapActivity.profil.setAddress_street(mySharedPreferences.getString("prefaddress_streat", ""));
		SwapActivity.profil.setGps_long(mySharedPreferences.getString("preflong", ""));
		SwapActivity.profil.setGps_lat(mySharedPreferences.getString("preflat", ""));
		SwapActivity.profil.SaveDb();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		setBackgroundColorForViewTree((ViewGroup) getWindow().getDecorView(), getResources().getColor(R.color.color_default_backg));
	}
	
	private static void setBackgroundColorForViewTree(ViewGroup viewGroup, int color) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View child = viewGroup.getChildAt(i);
			if (child instanceof ViewGroup)
				setBackgroundColorForViewTree((ViewGroup) child, color);
			child.setBackgroundColor(color);
		}
		viewGroup.setBackgroundColor(color);
	}
}
