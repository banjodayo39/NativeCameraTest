package com.banjodayo.nativecameratest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.FrameLayout;

import com.banjodayo.nativecameratest.R;
import com.banjodayo.nativecameratest.capture.PreviewFrameHandler;
import com.banjodayo.nativecameratest.capture.VideoCameraPreview;
import com.banjodayo.nativecameratest.render.GLVideoRenderer;

public class OpenGLCamera extends AppCompatActivity implements PreviewFrameHandler {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    protected VideoCameraPreview mPreview;
    private GLVideoRenderer mVideoRenderer;
    GLSurfaceView glSurfaceView;
    FrameLayout frameLayout;

    protected int mParams;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl_camera);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mPreview = new VideoCameraPreview(this);
        mPreview.init(displayMetrics.widthPixels, displayMetrics.heightPixels);

         glSurfaceView = findViewById(R.id.gl_surface_view);
        mVideoRenderer = new GLVideoRenderer(this);
        mVideoRenderer.init(glSurfaceView);
         frameLayout = ((FrameLayout) findViewById(R.id.preview));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        }



    }

    protected int getOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        return (ORIENTATIONS.get(rotation) + mPreview.getSensorOrientation() + 270) % 360;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        glSurfaceView.destroyDrawingCache();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPreview.startCamera();
        glSurfaceView.onResume();

    }

    @Override
    public void onPause() {
        mPreview.stopCamera();
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    public void onPreviewFrame(Bitmap bitmap, int width, int height) {
        mVideoRenderer.getBitmapLiveData().setValue(bitmap);
        mVideoRenderer.loadTexture(bitmap);
        mVideoRenderer.requestRender();

        frameLayout.addView(mPreview);

    }
}