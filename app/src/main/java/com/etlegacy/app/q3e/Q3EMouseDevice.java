package com.etlegacy.app.q3e;

public class Q3EMouseDevice {
    public static boolean DeviceIsRoot()
    {
        try
        {
            // return com.stericson.RootTools.RootTools.isRootAvailable();
            Class<?> clazz = Class.forName("com.stericson.RootTools.RootTools");
            return (boolean)clazz.getDeclaredMethod("isRootAvailable").invoke(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
