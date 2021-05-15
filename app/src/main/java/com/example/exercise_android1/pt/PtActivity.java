package com.example.exercise_android1.pt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exercise_android1.R;

import java.io.File;
import kotlin.Metadata;
//import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ren.yale.android.cachewebviewlib.CacheType;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptor;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptorInst;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptor.Builder;

@Metadata(
        mv = {1, 1, 16},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0010H\u0016J\u0012\u0010\u0012\u001a\u00020\u00102\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0015J\b\u0010\u0015\u001a\u00020\u0010H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0017"},
//        d2 = {"Lcom/example/exercise_android1/pt/PtActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "backPressedTime", "", "url", "", "getUrl", "()Ljava/lang/String;", "setUrl", "(Ljava/lang/String;)V", "webview", "Landroid/webkit/WebView;", "getWebview", "()Landroid/webkit/WebView;", "checkPressedTime", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setCookie", "Companion", "Exercise_android1.app"};
        d2 = {"Lcom/example/exercise_android1/pt/PtActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "backPressedTime", "", "url", "", "getUrl", "()Ljava/lang/String;", "setUrl", "(Ljava/lang/String;)V", "webview", "Landroid/webkit/WebView;", "getWebview", "()Landroid/webkit/WebView;", "checkPressedTime", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setCookie", "Companion", "Exercise_android1.app"}
)


public class PtActivity extends AppCompatActivity {
//    private long backPressedTime;
    @NotNull
    public String url = "https://www.naver.com";
    @NotNull
//    private WebView webview = findViewById(R.id.webview);
    private static final int MAX_BACK_PRESS_INTERVAL = 2000;
    private static final String TAG = "PtActivity";
//    public static final PtActivity.Companion Companion = new PtActivity.Companion((DefaultConstructorMarker)null);

    @NotNull
    public String getUrl() {
        return this.url;
    }

    public void setUrl(@NotNull String var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.url = var1;
    }

//    @NotNull
//    public final WebView getWebview() {
//        return this.webview;
//    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "WrongConstant"})
    @RequiresApi(21)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.pt_webview);

        WebView webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface((Context)this), "Website");
        webview.setWebViewClient((WebViewClient)(new WebViewClient() { }));

//        WebSettings var10000 = this.webview.getSettings();
//        Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//        var10000.setJavaScriptEnabled(true);
//        var10000 = this.webview.getSettings();
//        Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//        var10000.setLoadWithOverviewMode(true);
//        var10000 = this.webview.getSettings();
//        Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//        var10000.setUseWideViewPort(true);
//        this.webview.getSettings().setSupportZoom(true);
//        var10000 = this.webview.getSettings();
//        Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//        var10000.setBuiltInZoomControls(true);
//        var10000 = this.webview.getSettings();
//        Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//        var10000.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
//        if (VERSION.SDK_INT >= 19) {
//            var10000 = this.webview.getSettings();
//            Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//            var10000.setCacheMode(1);
//        } else {
//            var10000 = this.webview.getSettings();
//            Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//            var10000.setCacheMode(-1);
//        }

//        this.webview.getSettings().setAppCacheEnabled(true);
//        var10000 = this.webview.getSettings();
//        Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//        var10000.setDomStorageEnabled(true);
//        this.webview.addJavascriptInterface(new WebAppInterface((Context)this), "Website");
//        this.webview.setWebViewClient((WebViewClient)(new WebViewClient() {
//        }));

        Builder builder = new WebViewCacheInterceptor.Builder(this.getApplicationContext());
//        Builder builder = new Builder(this.getApplicationContext());
        builder.setCachePath(new File(this.getCacheDir(), "cache_webview"))
                .setCacheSize(1024 * 1024 * 500)
                .setConnectTimeoutSecond(20)
                .setReadTimeoutSecond(20)
                .setCacheType(CacheType.NORMAL);
        WebViewCacheInterceptorInst.getInstance().init(builder);
        Intent intent = this.getIntent();
        Intrinsics.checkExpressionValueIsNotNull(intent, "intent");

//        Bundle bundle = intent.getExtras();
//        if (bundle != null && bundle.getString("url") != null && !StringsKt.equals(bundle.getString("url"), "", true)) {
//            String var10001 = bundle.getString("url");
//            if (var10001 == null) {
//                Intrinsics.throwNpe();
//            }
//
//            this.setUrl(var10001);
//        }

        webview.loadUrl(this.getUrl());
        this.setCookie();
    }

    private void setCookie() {
        WebView webview = findViewById(R.id.webview);
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(0);
            cookieManager.setAcceptThirdPartyCookies(webview, true);
        } else {
            cookieManager.setAcceptCookie(true);
        }
//        if (VERSION.SDK_INT >= 21) {
//            WebView webview = findViewById(R.id.webview);
//            WebSettings var10000 = webview.getSettings();
//            Intrinsics.checkExpressionValueIsNotNull(var10000, "webview.settings");
//            var10000.setMixedContentMode(0);
//            cookieManager.setAcceptThirdPartyCookies(webview, true);
//        } else {
//            cookieManager.setAcceptCookie(true);
//        }

    }

//    public void onBackPressed() {
//        if (StringsKt.equals(this.webview.getOriginalUrl(), this.getUrl(), true)) {
//            this.checkPressedTime();
//        } else if (this.webview.canGoBack()) {
//            this.webview.goBack();
//        } else {
//            this.checkPressedTime();
//        }
//
//        if (VERSION.SDK_INT < 21) {
//            CookieSyncManager.getInstance().sync();
//        } else {
//            CookieManager.getInstance().flush();
//        }
//
//    }

//    private final void checkPressedTime() {
//        long presentTime = System.currentTimeMillis();
//        if (presentTime - this.backPressedTime <= (long)2000) {
//            super.onBackPressed();
//        } else {
//            this.backPressedTime = presentTime;
//            Toast.makeText(this.getApplicationContext(), (CharSequence)"뒤로 한번 더 누르면 종료됩니다.", 0).show();
//        }
//
//    }

//    public PtActivity() {
//        View var10001 = this.findViewById(R.layout.pt_webview);
//        Intrinsics.checkExpressionValueIsNotNull(var10001, "findViewById(R.id.webview)");
//        this.webview = (WebView)var10001;
//    }

//    @Metadata(
//            mv = {1, 1, 16},
//            bv = {1, 0, 3},
//            k = 1,
//            d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0007"},
//            d2 = {"Lcom/example/exercise_android1/pt/PtActivity$Companion;", "", "()V", "MAX_BACK_PRESS_INTERVAL", "", "TAG", "", "Exercise_android1.app"}
//    )
//    public static final class Companion {
//        private Companion() {
//        }
//
//        // $FF: synthetic method
//        public Companion(DefaultConstructorMarker $constructor_marker) {
//            this();
//        }
//    }
}