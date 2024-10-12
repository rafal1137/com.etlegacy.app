package com.etlegacy.app.lib;

import com.etlegacy.app.q3e.Q3EUtils;

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

        KCVar.Group RENDERER_CVARS = new KCVar.Group("Renderer", true)
                .AddCVar(
                        KCVar.CreateCVar("r_screenshotFormat", "integer", "0", "Screenshot format", 0,
                                "0", "TGA (default)",
                                "1", "BMP",
                                "2", "PNG",
                                "3", "JPG",
                                "4", "DDS"
                        ),
                        KCVar.CreateCVar("r_screenshotJpegQuality", "integer", "100", "Screenshot quality for JPG images (0-100)", KCVar.FLAG_POSITIVE)
                );

        _cvars.put("RENDERER", RENDERER_CVARS);

        return _cvars;
    }

    public static List<KCVar.Group> Match(String game)
    {
        Map<String, KCVar.Group> _cvars = CVars();
        if(null == game || game.isEmpty() || !_cvars.containsKey(game))
        {
            if(Q3EUtils.q3ei.isETW)
                return Collections.singletonList(_cvars.get("RENDERER"));
            else
                return null;
        }
        else
        {
            if(_cvars.containsKey(game))
                return Arrays.asList(_cvars.get("RENDERER"), _cvars.get(game));
            else
            {
                return new ArrayList<>();
            }
        }
    }
}
