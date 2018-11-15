package com.example.lin.shareexample;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CodeUtils;

@SuppressLint("Registered")
public abstract class BaseActivity extends FragmentActivity implements QrFragment.OnObtainQrCode, ShareDialog.OnShareEvent {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ((TextView) findViewById(R.id.title)).setText(obtainFlag());
        QrFragment qrFragment = QrFragment.newInstance(obtainFlag());
        getSupportFragmentManager().beginTransaction().add(R.id.container, qrFragment).commit();
    }

    protected abstract String obtainFlag();

    @Override
    public Bitmap obtain(final String flag) {
        final Bitmap image = CodeUtils.createImage(ShareConstant.BASE_URL + flag + "?cv=" + ShareConstant.SHARE_ID, 400, 400, null);
        ShareDialog.newInstance(image, flag).show(getSupportFragmentManager(), "shareDialog");
        return image;
    }

    @Override
    public void onShare(Bitmap b) {
//      todo 保存
        MediaStore.Images.Media.insertImage(getContentResolver(), b, "分享图片 - " + obtainFlag(), "");
    }
}
