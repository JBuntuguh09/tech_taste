package com.lonewolf.techtaste;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lonewolf.techtaste.Dialogues.Show_Me;
import com.lonewolf.techtaste.Resources.Settings;

import java.util.List;
import java.util.regex.Matcher;

public class Splash extends AppCompatActivity {

    private Thread mSplashThread;
    private ProgressBar progressBar;
    private TextView splashMessage;

    private ImageView imageView;
    private LinearLayout image;
    private List<String> links;
    private boolean screenPaused = false;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        settings = new Settings(this);

        progressBar = findViewById(R.id.progressBar);
        splashMessage = findViewById(R.id.splashText);
        image =  findViewById(R.id.re);

        String spMess ="Welcome to Tech Taste";
        if(!spMess.equals("null")) {
            splashMessage.setText(spMess);
        }

        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(4000);
                    }
                }
                catch(InterruptedException ex){
                }

                if(!screenPaused){
                    //finish();

                    // Run next activity
                    //if(settings.getNotFirstLogin()) {
                        Intent intent = new Intent(Splash.this, MainActivity.class);

                        startActivity(intent);
                        finish();
                   // Show_Me.help(Splash.this, image);
                  //  }
//                    else {
//                        Intent intent = new Intent(Splash.this, Welcome.class);
//
//                        startActivity(intent);
//                        finish();
//                    }
                }

            }
        };

        startAnimation();

        //mSplashThread.start();
    }

    /**
     * Processes splash screen touch events
     */

    private void startAnimation(){

        splashMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink();
            }
        });

        Animation animationFade, animateBounce;
        animationFade = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animateBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);


        splashMessage.startAnimation(animationFade);
        image.startAnimation(animateBounce);

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        progressAnimator.setDuration(4000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
        mSplashThread.start();
    }




    public void makeLinks(final TextView textView, List<String> links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.size(); i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links.get(i);

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);


        textView.setLinkTextColor(Color.BLUE); // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        ClickableSpan normalLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "2019", Toast.LENGTH_SHORT).show();
            }

        };

        ClickableSpan noUnderLineClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "NoUnderLine Link", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.MAGENTA); // specific color for this link
            }
        };




    }

    ClickableSpan highlightClickSpan = new ClickableSpan() {
        @Override
        public void onClick(View view) {
//            Toast.makeText(getApplicationContext(), "Highlight Link", Toast.LENGTH_SHORT)
//                    .show();
            openLink();
            view.invalidate(); // need put invalidate here to make text change to GREEN after clicked
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            if (splashMessage.isPressed() && splashMessage.getSelectionStart() != -1 && splashMessage.getText()
                    .toString()
                    .substring(splashMessage.getSelectionStart(), splashMessage.getSelectionEnd())
                    .equals("2019")) {
                ds.setColor(Color.RED); // need put invalidate here to make text change to RED when pressed on Highlight Link                    textView.invalidate();
            } else {
                ds.setColor(Color.GREEN);
            }
            // dont put invalidate here because if you put invalidate here `updateDrawState` will called forever
        }
    };




    private void openLink(){
        Matcher m = Patterns.WEB_URL.matcher("");
        String strURL =null;
        while (m.find()) {
            String url = m.group();
            //  Log.w("URL", "URL extracted: " + url);
            if(url.contains("https") || url.contains("www") || url.contains("http")){
                strURL = url;
                break;
            }
        }

        if(strURL != null){
            screenPaused = true;
            if(!strURL.contains("http")){
                strURL = "http://"+strURL;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strURL));
            startActivity(intent);
        }
    }




    @Override
    protected void onResume() {
        super.onResume();

    }}
