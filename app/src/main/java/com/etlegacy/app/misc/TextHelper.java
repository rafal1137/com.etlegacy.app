package com.etlegacy.app.misc;

import android.text.Html;

import com.etlegacy.app.lib.KCVar;
import com.etlegacy.app.lib.KCVarSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TextHelper {
    public static final boolean USING_HTML = true;

    public static String GetDialogMessageEndl()
    {
        return USING_HTML ? "<br>" : "\n";
    }

    public static String FormatDialogMessageSpace(String space)
    {
        return USING_HTML ? space.replaceAll(" ", "&nbsp;") : space;
    }

    public static String FormatDialogMessageHeaderSpace(String space)
    {
        if(!USING_HTML)
            return space;
        int i = 0;
        for(; i < space.length(); i++)
        {
            if(space.charAt(i) != ' ')
                break;
        }
        if(i == 0)
            return space;
        return FormatDialogMessageSpace(space.substring(0, i)) + space.substring(i);
    }

    public static CharSequence GetDialogMessage(String text)
    {
        return USING_HTML ? Html.fromHtml(text) : text;
    }

    public static class ChangeLog
    {
        public String date;
        public int release;
        public List<String> logs;

        public ChangeLog Date(String date)
        {
            this.date = date;
            return this;
        }

        public ChangeLog Release(int release)
        {
            this.release = release;
            return this;
        }

        public ChangeLog Log(String...args)
        {
            if(logs == null)
                logs = new ArrayList<>();
            Collections.addAll(logs, args);
            return this;
        }

        @Override
        public String toString()
        {
            return GenString(GetDialogMessageEndl());
        }

        public String GenString(String endl)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("------- ").append(date).append(" (R").append(release).append(") -------");
            sb.append(endl);
            if(logs != null && !logs.isEmpty())
            {
                for(String str : logs)
                {
                    if(str != null)
                        sb.append(FormatDialogMessageSpace("  * ")).append(str);
                    sb.append(endl);
                }
            }
            return sb.toString();
        }

        public static ChangeLog Create(String date, int release, String...args)
        {
            ChangeLog cl = new ChangeLog();
            cl.Date(date)
                    .Release(release)
                    .Log(args)
            ;
            return cl;
        }
    }

    public static String GenLinkText(String link, String name)
    {
        StringBuilder sb = new StringBuilder();
        if(USING_HTML)
        {
            String nameText = name != null && !name.isEmpty() ? name : link;
            sb.append("<a href='").append(link).append("'>").append(nameText).append("</a>");
        }
        else
        {
            if(name != null && !name.isEmpty())
                sb.append(name).append('(').append(link).append(')');
            else
                sb.append(link);
        }
        return sb.toString();
    }

    private static String GenCVarString(KCVar cvar, String endl)
    {
        StringBuilder sb = new StringBuilder();
        if(cvar.category == KCVar.CATEGORY_COMMAND)
        {
            sb.append(FormatDialogMessageSpace("  *[Command] ")).append(cvar.name);
            if(!KCVar.TYPE_NONE.equals(cvar.type))
                sb.append(" (").append(cvar.type).append(")");
        }
        else
            sb.append(FormatDialogMessageSpace("  *[CVar] ")).append(cvar.name).append(" (").append(cvar.type).append(") default: ").append(cvar.defaultValue);
        sb.append(FormatDialogMessageSpace("    ")).append(cvar.description);
        sb.append(endl);
        if(null != cvar.values)
        {
            for(KCVar.Value str : cvar.values)
            {
                sb.append(FormatDialogMessageSpace("    "));
                sb.append(str.value).append(" - ").append(str.desc);
                sb.append(endl);
            }
        }
        return sb.toString();
    }

    public static CharSequence GetCvarText()
    {
        StringBuilder sb = new StringBuilder();
        final String endl = GetDialogMessageEndl();
        for(Map.Entry<String, KCVar.Group> item : KCVarSystem.CVars().entrySet())
        {
            KCVar.Group value = item.getValue();
            sb.append("------- ").append(value.name).append(" -------");
            sb.append(endl);
            for(KCVar cvar : value.list)
                sb.append(GenCVarString(cvar, endl));
            sb.append(endl);
        }
        return GetDialogMessage(sb.toString());
    }

    private TextHelper() {}
}
