package com.banjodayo.nativecameratest.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class AirHockeyGLView extends GLSurfaceView {
    public AirHockeyGLView(Context context) {
        super(context);
    }

    public AirHockeyGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setRenderer(new AirHockeyRenderer(getContext()));
    }
}
