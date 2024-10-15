package com.etlegacy.app.q3e;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.preference.PreferenceManager;

import java.util.Locale;

public final class Q3ELang {
    public static final String CONST_LANG_SYSTEM = "system";

    public static String tr(Context context, int id, Object...args)
    {
        return context.getResources().getString(id, args);
    }

    public static void Locale(Context context)
    {
        String lang = PreferenceManager.getDefaultSharedPreferences(context).getString(Q3EPreference.LANG, CONST_LANG_SYSTEM);
        Locale(context, lang);
    }

    public static void Locale(Context context, String language)
    {
        if(null == language || language.isEmpty() || CONST_LANG_SYSTEM.equalsIgnoreCase(language))
            return;

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Locale loc;
        loc = Locale.forLanguageTag(language);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        configuration.setLocale(loc);

        resources.updateConfiguration(configuration, metrics);
    }
}
