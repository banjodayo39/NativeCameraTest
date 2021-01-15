package com.banjodayo.nativecameratest.render;

import android.content.Context;


import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;

import androidx.lifecycle.MutableLiveData;

import com.banjodayo.nativecameratest.objects.Mallet;
import com.banjodayo.nativecameratest.objects.Table;
import com.banjodayo.nativecameratest.programs.ColorShaderProgram;
import com.banjodayo.nativecameratest.programs.TextureShaderProgram;
import com.banjodayo.nativecameratest.utils.ImageRenderingHelper;
import com.banjodayo.nativecameratest.utils.AirHockeyMatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class GLVideoRenderer extends VideoRenderer implements GLSurfaceView.Renderer {

    private GLSurfaceView mGLSurface;
    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private Table table;

    private Mallet mallet;
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;

    private MutableLiveData<Bitmap> bitmapLiveData;

    public GLVideoRenderer(Context context) {
      //  create(Type.GL_YUV420_FILTER.getValue());
        this.context = context;

    }

    public void init(GLSurfaceView glSurface) {
        mGLSurface = glSurface;
        // Create an OpenGL ES 2 context.
        mGLSurface.setEGLContextClientVersion(2);
        mGLSurface.setRenderer(this);
        mGLSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void requestRender() {
        if (mGLSurface != null) {
            mGLSurface.requestRender();
        }
    }

    public MutableLiveData<Bitmap> getBitmapLiveData() {
        if(bitmapLiveData == null){
            bitmapLiveData = new MutableLiveData<>();
        }
        return bitmapLiveData;
    }

    public void loadTexture(Bitmap bitmap){
        if(bitmap != null){
            texture = ImageRenderingHelper.loadTexture(bitmap);
        }
    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        table = new Table();
        mallet = new Mallet();
        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        if(bitmapLiveData != null){
            texture = ImageRenderingHelper.loadTexture(bitmapLiveData.getValue());
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width  , height);

        if(bitmapLiveData != null && bitmapLiveData.getValue() != null){
            texture = ImageRenderingHelper.loadTexture(bitmapLiveData.getValue());

        }
        AirHockeyMatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 1f, 10f);

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);


//        // screen rotation
//        final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
//        if (width > height) {
//        // Landscape
//            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        } else {
//        // Portrait or square
//            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//        }
    }


    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        // Draw the table.
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();

        // Draw the mallets.
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();
    }

}

