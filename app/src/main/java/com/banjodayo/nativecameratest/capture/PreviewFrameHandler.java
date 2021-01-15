package com.banjodayo.nativecameratest.capture;

import android.graphics.Bitmap;

/**
 * Created by oleg on 11/2/17.
 */

public interface PreviewFrameHandler {
    void onPreviewFrame(Bitmap bitmap , int width, int height);
}

