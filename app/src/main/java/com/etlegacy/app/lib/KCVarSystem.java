package com.etlegacy.app.lib;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class KCVarSystem {
    public static Map<String, KCVar.Group> CVars()
    {
        Map<String, KCVar.Group> _cvars = new LinkedHashMap<>();

        KCVar.Group ETL_CVARS = new KCVar.Group("ETL", true)
                .AddCVar(
                        KCVar.CreateCVar("r_screenshotFormat", "integer", "0", "Screenshot format", KCVar.FLAG_NO_ARCHIVE,
                                "0", "TGA (default)",
                                "2", "PNG",
                                "3", "JPG"
                        ),
                        KCVar.CreateCVar("r_screenshotJpegQuality", "integer", "100", "Screenshot quality for JPG images (0-100)", KCVar.FLAG_POSITIVE)
                );

        _cvars.put("ETL", ETL_CVARS);

        return _cvars;
    }

    public static List<KCVar.Group> Match(String game)
    {
        Map<String, KCVar.Group> _cvars = CVars();
        List<KCVar.Group> res = new ArrayList<>();
        if(null == game || game.isEmpty() || !_cvars.containsKey(game))
        {
            res.add(_cvars.get("ETL"));
            return res;
        }
        return res;
    }
}