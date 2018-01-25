package com.jetslice.retroart1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.doctoror.particlesdrawable.ParticlesDrawable;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import static com.jetslice.retroart1.R.id.particle_drawable;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        HTextView hTextView = (HTextView) findViewById(R.id.textjk);
        hTextView.setAnimateType(HTextViewType.LINE);
        hTextView.animateText("RetroArt");

        TextView mTextBtx=(TextView) findViewById(R.id.edit_btx);
        mTextBtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(mIntent);
            }
        });


    }


}

