package com.banjodayo.nativecameratest.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OpenGLView extends GLSurfaceView {

    public OpenGLView(Context context) {
        super(context);
        //init();
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
      //  init();
    }

    private void init(){
        setEGLContextClientVersion(3);
        setPreserveEGLContextOnPause(true);
        setRenderer(new OpenGLRenderer());
    }
}
