package com.example.android.readme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by tali on 19.12.17.
 */

public class NewsPreference extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //get preference and fetch to summary
        Preference pref = findPreference(getString(R.string.setting_number_of_result));
        bindPreferenceSummaryToValue(pref, getString(R.string.setting_number_of_result));
    }

    private void bindPreferenceSummaryToValue(Preference pref, String string) {
        pref.setOnPreferenceChangeListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pref.getContext());
        String value = preferences.getString(string, "");
        onPreferenceChange(pref, value);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String value = o.toString();
        preference.setSummary(value);
        return true;
    }
}
