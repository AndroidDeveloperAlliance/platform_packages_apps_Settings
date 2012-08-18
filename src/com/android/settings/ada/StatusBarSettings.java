/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.ada;

import android.os.Bundle;
import android.content.ContentResolver;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class StatusBarSettings extends SettingsPreferenceFragment implements  Preference.OnPreferenceChangeListener {

    private static final String TAG = "StatusBarSettings";

    private static final String KEY_STATUSBAR_BATTERYICON = "adamod_statusbar_battery_icon";
    private static final String KEY_STATUSBAR_CLOCKPOSITION = "adamod_statusbar_clock_position";
    private static final String KEY_STATUSBAR_CLOCKAMPM = "adamod_statusbar_clock_ampm";
    private static final String KEY_STATUSBAR_CLOCKWEEKDAY = "adamod_statusbar_clock_weekday";
    
    private ListPreference mStatusBarBatteryIconPref;
    private ListPreference mStatusBarClockPositionPref;
    private ListPreference mStatusBarClockAMPMPref;
    private ListPreference mStatusBarClockWeekdayPref;
    

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         ContentResolver resolver = getActivity().getContentResolver();

        addPreferencesFromResource(R.xml.status_bar);

        initCustomizationPreferenceWidgets();
    }

     private void updateState() {
        readCustomizationSettings();
     }
     

    @Override
    public void onResume() {
        super.onResume();

        updateState();

    }

public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        	saveCustomizationSetting(key, objValue);

        return true;
    }

     public void initCustomizationPreferenceWidgets() {

      if (getPreferenceManager() != null) {
        //Status Bar Battery Icon
        mStatusBarBatteryIconPref = (ListPreference) findPreference(KEY_STATUSBAR_BATTERYICON);
        mStatusBarBatteryIconPref.setOnPreferenceChangeListener(this);
        
        //Status Bar Clock Position
        mStatusBarClockPositionPref = (ListPreference) findPreference(KEY_STATUSBAR_CLOCKPOSITION);
        mStatusBarClockPositionPref.setOnPreferenceChangeListener(this);
        
        mStatusBarClockAMPMPref = (ListPreference) findPreference(KEY_STATUSBAR_CLOCKAMPM);
        mStatusBarClockAMPMPref.setOnPreferenceChangeListener(this);
        
        mStatusBarClockWeekdayPref = (ListPreference) findPreference(KEY_STATUSBAR_CLOCKWEEKDAY);
        mStatusBarClockWeekdayPref.setOnPreferenceChangeListener(this);
	}
    }

    public void readCustomizationSettings() {
    	readListPreferenceValue(mStatusBarBatteryIconPref, Settings.System.STATUSBAR_BATTERY_ICON, 0);
    	readListPreferenceValue(mStatusBarClockPositionPref, Settings.System.STATUSBAR_CLOCK_STYLE, 1);
    	readListPreferenceValue(mStatusBarClockAMPMPref, Settings.System.STATUSBAR_CLOCK_AM_PM_STYLE, 2);
    	readListPreferenceValue(mStatusBarClockWeekdayPref, Settings.System.STATUSBAR_CLOCK_WEEKDAY, 0);
    }
    
    public void saveCustomizationSetting(String key, Object objValue) {
    	//save appropriate key's value.
    	if (KEY_STATUSBAR_BATTERYICON.equals(key)) {
    		int val = Integer.parseInt((String) objValue); 
    		saveListPreferenceValue(Settings.System.STATUSBAR_BATTERY_ICON, val);
    	}
    	else if (KEY_STATUSBAR_CLOCKPOSITION.equals(key)) {
    		int val = Integer.parseInt((String) objValue); 
    		saveListPreferenceValue(Settings.System.STATUSBAR_CLOCK_STYLE, val);
    	}
    	else if (KEY_STATUSBAR_CLOCKAMPM.equals(key)) {
    		int val = Integer.parseInt((String) objValue); 
    		saveListPreferenceValue(Settings.System.STATUSBAR_CLOCK_AM_PM_STYLE, val);
    	}
    	else if (KEY_STATUSBAR_CLOCKWEEKDAY.equals(key)) {
    		int val = Integer.parseInt((String) objValue); 
    		saveListPreferenceValue(Settings.System.STATUSBAR_CLOCK_WEEKDAY, val);
    	}
    }
    
    public void readListPreferenceValue(ListPreference pref, String settingKey, int defaultValue) {
		int valueIndex = Settings.System.getInt(getActivity().getContentResolver(), settingKey, defaultValue);
		String value = (valueIndex) + "";
		Log.w(TAG, "Reading Preference: " + settingKey + ". Value = " + value + ". Setting Control: " + pref.getKey());
	 	pref.setValue(value);
    }
    
    public void saveListPreferenceValue(String key, int value) {
    	Log.w(TAG, "Saving Preference: " + key + " Value: " + value);
    	Settings.System.putInt(getActivity().getContentResolver(), key, value);
    }
}
