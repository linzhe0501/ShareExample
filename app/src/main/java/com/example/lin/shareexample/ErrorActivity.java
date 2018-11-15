package com.example.lin.shareexample;

import android.graphics.Bitmap;

import com.uuzuche.lib_zxing.activity.CodeUtils;

public class ErrorActivity extends BaseActivity {
    @Override
    protected String obtainFlag() {
        return "second";
    }

    @Override
    public Bitmap obtain(final String flag) {
        final Bitmap image = CodeUtils.createImage(ShareConstant.BASE_URL + flag, 400, 400, null);
        ShareDialog.newInstance(image, flag).show(getSupportFragmentManager(), "shareDialog");
        return image;
    }
}
