package com.example.exercise_android1.pt;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
        mv = {1, 1, 16},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J&\u0010\t\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0017J\u001a\u0010\u000e\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000f\u001a\u00020\bH\u0016J\u001c\u0010\u0010\u001a\u00020\u00112\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014H\u0016¨\u0006\u0015"},
        d2 = {"Lcom/example/exercise_android1/pt/MyWebViewClient;", "Landroid/webkit/WebViewClient;", "()V", "onPageFinished", "", "view", "Landroid/webkit/WebView;", "url", "", "onReceivedError", "request", "Landroid/webkit/WebResourceRequest;", "error", "Landroid/webkit/WebResourceError;", "printToast", "msg", "shouldOverrideUrlLoading", "", "startActivity", "intent", "Landroid/content/Intent;", "Exercise_android1.app"}
)
public class MyWebViewClient extends WebViewClient {
    public void onPageFinished(@Nullable WebView view, @Nullable String url) {
        super.onPageFinished(view, url);
        if (VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

    }

    @RequiresApi(23)
    public void onReceivedError(@Nullable WebView view, @Nullable WebResourceRequest request, @Nullable WebResourceError error) {
        label130: {
            Integer var4 = error != null ? error.getErrorCode() : null;
            byte var5 = -4;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "서버에서 사용자 인증 실패");
                    break label130;
                }
            }

            var5 = -12;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "잘못된 URL");
                    break label130;
                }
            }

            var5 = -6;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "서버로 연결 실패");
                    break label130;
                }
            }

            var5 = -11;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "SSL handshake 수행 실패");
                    break label130;
                }
            }

            var5 = -13;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "일반 파일 오류");
                    break label130;
                }
            }

            var5 = -14;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "파일을 찾을 수 없습니다");
                    break label130;
                }
            }

            var5 = -2;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "서버 또는 프록시 호스트 이름 조회 실패");
                    break label130;
                }
            }

            var5 = -7;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "서버에서 읽거나 서버로 쓰기 실패");
                    break label130;
                }
            }

            var5 = -5;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "프록시에서 사용자 인증 실패");
                    break label130;
                }
            }

            var5 = -9;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "너무 많은 리디렉션");
                    break label130;
                }
            }

            var5 = -8;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "연결 시간 초과");
                    break label130;
                }
            }

            var5 = -15;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "페이지 로드중 너무 많은 요청 발생");
                    break label130;
                }
            }

            var5 = -1;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "일반 오류");
                    break label130;
                }
            }

            var5 = -3;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "지원되지 않는 인증 체계");
                    break label130;
                }
            }

            var5 = -10;
            if (var4 != null) {
                if (var4 == var5) {
                    this.printToast(view, "URI가 지원되지 않는 방식");
                }
            }
        }

        super.onReceivedError(view, request, error);
    }

    public void printToast(@Nullable WebView view, @NotNull String msg) {
        Intrinsics.checkParameterIsNotNull(msg, "msg");
        Toast.makeText(view != null ? view.getContext() : null, (CharSequence)msg, Toast.LENGTH_SHORT).show();
    }

    public boolean shouldOverrideUrlLoading(@Nullable WebView view, @Nullable String url) {
        Uri var10000 = Uri.parse(url);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "Uri.parse(url)");
        if (Intrinsics.areEqual(var10000.getHost(), "http://m.martroo.com/am")) {
            return false;
        } else {
            Intent var3 = new Intent("android.intent.action.VIEW", Uri.parse(url));
            boolean var4 = false;
            boolean var5 = false;
            int var7 = 0;
            this.startActivity(var3);
            return true;
        }
    }

    public void startActivity(@NotNull Intent intent) {
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        this.startActivity(intent);
    }
}
