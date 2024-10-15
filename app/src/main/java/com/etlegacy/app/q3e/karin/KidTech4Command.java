package com.etlegacy.app.q3e.karin;

public final class KidTech4Command extends KidTechCommand
{
    public static String SetProp(String str, String name, Object val)
    {
        return KidTechCommand.SetProp(ARG_PREFIX_IDTECH, str, name, val);
    }

    public static String GetProp(String str, String name, String...def)
    {
        return KidTechCommand.GetProp(ARG_PREFIX_IDTECH, str, name, def);
    }

    public static String RemoveProp(String str, String name)
    {
        return KidTechCommand.RemoveProp(ARG_PREFIX_IDTECH, str, name);
    }

    public static boolean IsProp(String str, String name)
    {
        return KidTechCommand.IsProp(ARG_PREFIX_IDTECH, str, name);
    }

    public static Boolean GetBoolProp(final String str, String name, Boolean...def)
    {
        return KidTechCommand.GetBoolProp(ARG_PREFIX_IDTECH, str, name, def);
    }

    public static String SetBoolProp(String str, String name, boolean val)
    {
        return KidTech4Command.SetProp(str, name, btostr(val));
    }

    public static String RemoveParam(String str, String name)
    {
        return KidTechCommand.RemoveParam(ARG_PREFIX_IDTECH, str, name);
    }

    public static String SetParam(String str, String name, Object val)
    {
        return KidTechCommand.SetParam(ARG_PREFIX_IDTECH, str, name, val);
    }

    public static String SetCommand(String str, String name, boolean...prepend)
    {
        return KidTechCommand.SetCommand(ARG_PREFIX_IDTECH, str, name, prepend);
    }

    public static String RemoveCommand(String str, String name)
    {
        return KidTechCommand.RemoveCommand(ARG_PREFIX_IDTECH, str, name);
    }

    public static String GetParam(String str, String name, String...def)
    {
        return KidTechCommand.GetParam(ARG_PREFIX_IDTECH, str, name, def);
    }

    public static boolean HasParam(String str, String name)
    {
        return KidTechCommand.HasParam(ARG_PREFIX_IDTECH, str, name);
    }

    public KidTech4Command(String str)
    {
        super(ARG_PREFIX_IDTECH, str);
    }
}