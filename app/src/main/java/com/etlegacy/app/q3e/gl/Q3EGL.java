package com.etlegacy.app.q3e.gl;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import com.etlegacy.app.q3e.Q3EUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class Q3EGL {
    public static void DrawVerts_GL1(GL11 gl, int texid, int cnt, FloatBuffer texcoord, FloatBuffer vertcoord, ByteBuffer inds, float trax, float tray, float r, float g, float b, float a)
    {
        gl.glColor4f(r, g, b, a);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texid);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texcoord);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertcoord);
        gl.glTranslatef(trax, tray, 0);
        gl.glDrawElements(GL10.GL_TRIANGLES, cnt, GL10.GL_UNSIGNED_BYTE, inds);
        gl.glTranslatef(-trax, -tray, 0);
    }

    public static int loadGLTexture(GL10 gl, Bitmap bmp)
    {
        if(null == bmp)
            return 0;

        int[] t = new int[1];

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glGenTextures(1, t, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, t[0]);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        Bitmap powerof2bmp = Bitmap.createScaledBitmap(bmp, Q3EUtils.nextpowerof2(bmp.getWidth()), Q3EUtils.nextpowerof2(bmp.getHeight()), true);

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, powerof2bmp, 0);
        bmp.recycle();
        return t[0];
    }

    public static void glDeleteTexture(GL10 gl, int texture)
    {
        if(texture <= 0)
            return;
        final int[] ts = { texture };
        gl.glDeleteTextures(1, ts, 0);
    }
}
