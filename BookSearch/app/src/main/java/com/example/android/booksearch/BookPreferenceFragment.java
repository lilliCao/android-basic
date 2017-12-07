package com.example.android.booksearch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by tali on 06.12.17.
 */

public class BookPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    //Add preference to save default sort, default language
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load preference from xml
        addPreferencesFromResource(R.xml.preferences);

        //Get preference and fetch to summary
        Preference pref = findPreference(getString(R.string.setting_sort));
        Preference language = findPreference(getString(R.string.setting_language));
        Preference number = findPreference(getString(R.string.setting_number_of_result));
        bindPreferenceSummaryToValue(pref, getString(R.string.setting_sort));
        bindPreferenceSummaryToValue(language, getString(R.string.setting_language));
        bindPreferenceSummaryToValue(number, getString(R.string.setting_number_of_result));
    }

    private void bindPreferenceSummaryToValue(Preference pref, String field) {
        pref.setOnPreferenceChangeListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pref.getContext());
        String value = preferences.getString(field, "");
        onPreferenceChange(pref, value);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String value = o.toString();
        preference.setSummary(value);
        return true;
    }
}
