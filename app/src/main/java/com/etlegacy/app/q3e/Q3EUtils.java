package com.etlegacy.app.q3e;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.WindowInsets;

import androidx.appcompat.app.AppCompatActivity;

import com.etlegacy.app.q3e.karin.KFDManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;

public class Q3EUtils {
    public static Q3EInterface q3ei = new Q3EInterface();

    public static int SupportMouse()
    {
        // return Q3EGlobals.MOUSE_EVENT;
        return !Q3EMouseDevice.DeviceIsRoot() ? Q3EGlobals.MOUSE_EVENT : Q3EGlobals.MOUSE_DEVICE;
    }

    public static String GetAppStoragePath(Context context)
    {
        String path = Q3EUtils.GetAppStoragePath(context, null);
        File dir = new File(path);
        return dir.getParent();
    }

    public static String GetAppStoragePath(Context context, String filename)
    {
        String path;
        File externalFilesDir = context.getExternalFilesDir(null);
        if(null != externalFilesDir)
            path = externalFilesDir.getAbsolutePath();
        else
            path = Environment.getExternalStorageDirectory() + "/Android/data/" + Q3EGlobals.CONST_PACKAGE_NAME + "/files";
        if(null != filename && !filename.isEmpty())
            path += filename;
        return path;
    }

    public static String file_get_contents(String path)
    {
        if(null == path)
            return null;
        return file_get_contents(new File(path));
    }

    public static String file_get_contents(File file)
    {
        if(null == file || !file.isFile() || !file.canRead())
            return null;

        FileReader reader = null;
        try
        {
            reader = new FileReader(file);
            int BUF_SIZE = 1024;
            char[] chars = new char[BUF_SIZE];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = reader.read(chars)) > 0)
                sb.append(chars, 0, len);
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            Close(reader);
        }
    }

    public static void Close(Closeable closeable)
    {
        try
        {
            if(null != closeable)
                closeable.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean file_put_contents(String path, String content)
    {
        if(null == path)
            return false;
        return file_put_contents(new File(path), content);
    }

    public static boolean file_put_contents(File file, String content)
    {
        if(null == file)
            return false;

        FileWriter writer = null;
        try
        {
            writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            Close(writer);
        }
    }

    public static long Copy(OutputStream out, InputStream in, int...bufferSizeArg) throws RuntimeException
    {
        if(null == out)
            return -1;
        if(null == in)
            return -1;

        int bufferSize = bufferSizeArg.length > 0 ? bufferSizeArg[0] : 0;
        if (bufferSize <= 0)
            bufferSize = 8192;

        byte[] buffer = new byte[bufferSize];

        long size = 0L;

        int readSize;
        try
        {
            while((readSize = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, readSize);
                size += readSize;
                out.flush();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return size;
    }

    public static LinkedHashMap<String, String> GetControlsThemes(Context context)
    {
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        list.put("/android_asset", "Default");
        list.put("", "External");
        List<String> controls_theme = KFDManager.Instance(context).ListDir("controls_theme");
        for (String file : controls_theme)
        {
            list.put("controls_theme/" + file, file);
        }
        return list;
    }

    public static String GetAppInternalPath(Context context, String filename)
    {
        String path;
        path = context.getDataDir().getAbsolutePath();
        if(null != filename && !filename.isEmpty())
            path += filename;
        return path;
    }

    public static Bitmap LoadControlBitmap(Context context, String path, String type)
    {
        InputStream is = null;
        Bitmap texture = null;
        switch (type)
        {
            case "/android_asset":
                is = Q3EUtils.OpenResource_assets(context, path);
                break;
            case "":
                is = Q3EUtils.OpenResource(context, path);
                break;
            default:
                if(type.startsWith("/"))
                {
                    type = type.substring(1);
                    is = Q3EUtils.OpenResource_assets(context, type + "/" + path);
                }
                else
                {
                    if((is = Q3EUtils.OpenResource(context, type + "/" + path)) == null)
                        is = Q3EUtils.OpenResource_assets(context, path);
                }
                break;
        }

        try
        {
            texture = BitmapFactory.decodeStream(is);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Q3EUtils.Close(is);
        }
        return texture;
    }

    public static InputStream OpenResource(Context cnt, String assetname)
    {
        InputStream is;
        is = KFDManager.Instance(cnt).OpenRead(assetname);
        return is;
    }

    public static InputStream OpenResource_assets(Context cnt, String assetname)
    {
        InputStream is = null;
        try
        {
            is = cnt.getAssets().open(assetname);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return is;
    }

    public static float parseFloat_s(String str, float...def)
    {
        float defVal = null != def && def.length > 0 ? def[0] : 0.0f;
        if(null == str)
            return defVal;
        str = str.trim();
        if(str.isEmpty())
            return defVal;
        try
        {
            return Float.parseFloat(str);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return defVal;
        }
    }

    public static String Join(String d, String...strs)
    {
        if(null == strs)
            return null;
        if(strs.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]);
            if(i < strs.length - 1)
                sb.append(d);
        }
        return sb.toString();
    }

    public static int dip2px(Context ctx, int dip)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
    }

    public static int GetEdgeHeight(AppCompatActivity activity, boolean landscape)
    {
        int safeInsetTop = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        {
            WindowInsets rootWindowInsets = activity.getWindow().getDecorView().getRootWindowInsets();
            if (null != rootWindowInsets)
            {
                DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
                if (null != displayCutout)
                {
                    safeInsetTop = landscape ? displayCutout.getSafeInsetLeft() : displayCutout.getSafeInsetTop();
                }
            }
        }
        return safeInsetTop;
    }

    public static int GetEndEdgeHeight(AppCompatActivity activity, boolean landscape)
    {
        int safeInsetBottom = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        {
            WindowInsets rootWindowInsets = activity.getWindow().getDecorView().getRootWindowInsets();
            if (null != rootWindowInsets)
            {
                DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
                if (null != displayCutout)
                {
                    safeInsetBottom = landscape ? displayCutout.getSafeInsetRight() : displayCutout.getSafeInsetBottom();
                }
            }
        }
        return safeInsetBottom;
    }

    public static int[] GetFullScreenSize(AppCompatActivity activity)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return new int[]{size.x, size.y};
    }

    public static int[] GetNormalScreenSize(AppCompatActivity activity)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new int[]{size.x, size.y};
    }

    public static boolean isAppInstalled(AppCompatActivity ctx, String nm)
    {
        try
        {
            ctx.getPackageManager().getPackageInfo(nm, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public static int nextpowerof2(int x)
    {
        int candidate = 1;
        while (candidate < x)
            candidate *= 2;
        return candidate;
    }
}
