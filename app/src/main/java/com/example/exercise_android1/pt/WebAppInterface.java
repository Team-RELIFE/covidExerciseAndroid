package com.example.exercise_android1.pt;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

//@Metadata(
//        mv = {1, 1, 16},
//        bv = {1, 0, 3},
//        k = 1,
//        d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"},
//        d2 = {"Lcom/example/exercise_android1/pt/WebAppInterface;", "", "mContext", "Landroid/content/Context;", "(Landroid/content/Context;)V", "showToast", "", "toast", "", "Exercise_android1.app"}
//)
public final class WebAppInterface {
    private final Context mContext;

    @JavascriptInterface
    public final void showToast(@NotNull String toast) {
        Intrinsics.checkParameterIsNotNull(toast, "toast");
        Toast.makeText(this.mContext, (CharSequence)toast, Toast.LENGTH_SHORT).show();
    }

    public WebAppInterface(@NotNull Context mContext) {
        super();
        Intrinsics.checkParameterIsNotNull(mContext, "mContext");
        this.mContext = mContext;
    }
}
