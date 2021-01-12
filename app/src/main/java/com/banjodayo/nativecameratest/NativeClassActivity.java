package com.banjodayo.nativecameratest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.banjodayo.nativecameratest.opengl.AirHockeyGLView;
import com.banjodayo.nativecameratest.opengl.AirHockeyRenderer;
import com.banjodayo.nativecameratest.opengl.OpenGLRenderer;

public class NativeClassActivity extends AppCompatActivity {


    boolean renderSet = false;
    private AirHockeyGLView airHockeyGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_class);

        TextView tv = findViewById(R.id.sample_text);
        airHockeyGLView = (AirHockeyGLView) findViewById(R.id.airHockeyView);
        setGLSupport();

    }

    private void setGLSupport(){
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();

        final boolean supportsEs3 =
                configurationInfo.reqGlEsVersion >= 0x20000
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));

        if(supportsEs3){
            airHockeyGLView.setEGLContextClientVersion(2);
            airHockeyGLView.setRenderer(new AirHockeyRenderer(this));

            renderSet = true;
        } else {
            Toast.makeText(this, "Open GL would not work", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (renderSet){
            airHockeyGLView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(renderSet){
            airHockeyGLView.onPause();
        }
    }


}