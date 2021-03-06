package com.alrehablife.alrehab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.support.v4.view.GestureDetectorCompat;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener
{

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    protected WebView browser = null;
    protected GestureDetector gestureDetector;

    private View mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        this.gestureDetector = new GestureDetector(this,this);
        this.gestureDetector.setOnDoubleTapListener(this);

        browser = (WebView) findViewById(R.id.webView);

        mContentView = browser;



        browser = (WebView) findViewById(R.id.webView);

        CookieManager.getInstance().acceptCookie();
        WebSettings webSettings=browser.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.getAllowContentAccess();
        webSettings.setAppCacheEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setDomStorageEnabled(true);


        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.setKeepScreenOn(true);



        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        browser.setWebViewClient(webViewClient);


        browser.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
                if (newProgress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                progressBar.setProgress(newProgress);
                //progressTxt.setText(newProgress);
                if (newProgress == 100){
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });


        browser.loadUrl("http://android.alrehablife.com/");


    }
    protected void RefreshPage()
    {
        browser = (WebView) findViewById(R.id.webView);
        browser.reload();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();

    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay

        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };


    public class WebViewClientImpl extends WebViewClient {

        private Activity activity = null;
        private UrlCache urlCache = null;

        public WebViewClientImpl(Activity activity) {
            this.activity = activity;
            this.urlCache = new UrlCache(activity);

            this.urlCache.register("http://android.alrehablife.com/", "index.html",
                    "text/html", "UTF-8", 5 * UrlCache.ONE_MINUTE);

            this.urlCache.register("http://android.alrehablife.com/aboutus.html", "aboutus.html",
                    "text/html", "UTF-8", 5 * UrlCache.ONE_MINUTE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if(url.indexOf("alrehablife.com") > -1 ) return false;

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }



        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if("http://android.alrehablife.com/".equals(url)){
                this.urlCache.load("http://android.alrehablife.com/");
            }
            else
            {
                this.urlCache.load(url);
            }
        }


    }

    private final Handler mHideHandler = new Handler();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e)
    {
        RefreshPage();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        RefreshPage();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        RefreshPage();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        RefreshPage();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        RefreshPage();
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        RefreshPage();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        RefreshPage();
        return true;
    }
}
