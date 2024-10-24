package com.etlegacy.app.q3e;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import com.etlegacy.app.q3e.karin.KFDManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;

public class Q3EUtils {
    public static Q3EInterface q3ei = new Q3EInterface(); //k: new

    public static int SupportMouse()
    {
        // return Q3EGlobals.MOUSE_EVENT;
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? Q3EGlobals.MOUSE_EVENT : Q3EGlobals.MOUSE_DEVICE;
    }

    public static String GetGameHomeDirectoryPath()
    {
        return ".etlegacy/legacy";
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

    public static InputStream OpenResource(Context cnt, String assetname)
    {
        InputStream is;
        is = KFDManager.Instance(cnt).OpenRead(assetname);
        return is;
    }

    public static Bitmap LoadControlBitmap(Context context, String path, String type)
    {
        InputStream is = null;
        Bitmap texture = null;
        switch (type)
        {
            case "/android_asset":
                is = OpenResource_assets(context, path);
                break;
            case "":
                is = OpenResource(context, path);
                break;
            default:
                if(type.startsWith("/"))
                {
                    type = type.substring(1);
                    is = OpenResource_assets(context, type + "/" + path);
                }
                else
                {
                    if((is = OpenResource(context, type + "/" + path)) == null)
                        is = OpenResource_assets(context, path);
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

    public static String GetAppInternalPath(Context context, String filename)
    {
        String path;
        path = context.getDataDir().getAbsolutePath();
        if(null != filename && !filename.isEmpty())
            path += filename;
        return path;
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
}
