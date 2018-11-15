package com.example.lin.shareexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.uuzuche.lib_zxing.camera.BitmapLuminanceSource;
import com.uuzuche.lib_zxing.decoding.DecodeFormatManager;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZXingLibrary.initDisplayOpinion(this);
        setContentView(R.layout.activity_main);
        PermissionManager.verifyStoragePermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void scan() {
        final List<String> pictureList = PictureTool.queryGallery(MainActivity.this);
        if (pictureList != null && pictureList.size() > 0) {
            for (int i = 0; i < pictureList.size(); i++) {
                Log.d("picture", "图片path = " + pictureList.get(i));
            }
            String lastPath = pictureList.get(pictureList.size() - 1);
            final Bitmap bitmap = BitmapFactory.decodeFile(lastPath);
            ((ImageView) findViewById(R.id.iv_0)).setImageBitmap(bitmap);
            parse(bitmap);
        }
    }

    private void parse(Bitmap bitmap) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        // 设置继续的字符编码格式为UTF8
        // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("parseQrcode", (rawResult == null ? "null" : rawResult.getText()));
        if (rawResult != null) {
            final String result = rawResult.getText();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.setData(Uri.parse(result));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();

        if (uri != null && "http".equals(uri.getScheme()) && ShareConstant.HOST.equals(uri.getHost())) {
            String cv = uri.getQueryParameter("cv");
            Log.d("onNewIntent", "cv = " + cv);
            if (ShareConstant.SHARE_ID.equals(cv)) {
                String path = uri.getPath();
                assert path != null;
                switch (path) {
                    case "/1":
                        startActivity(new Intent(MainActivity.this, FirstActivity.class));
                        break;
                    case "/2":
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                        break;
                    case "/3":
                        startActivity(new Intent(MainActivity.this, ThirdActivity.class));
                        break;
                }
                Toast.makeText(MainActivity.this, "正确的口令 = " + uri.toString(), Toast.LENGTH_LONG).show();
            } else {
                Log.d("onNewIntent", "错误的口令");
                Toast.makeText(MainActivity.this, "错误的口令 = " + uri.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.first:
                startActivity(new Intent(MainActivity.this, FirstActivity.class));
                break;
            case R.id.second:
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
                break;
            case R.id.third:
                startActivity(new Intent(MainActivity.this, ThirdActivity.class));
                break;
            case R.id.error:
                startActivity(new Intent(MainActivity.this, ErrorActivity.class));
                break;
            case R.id.scan:
                scan();
                break;
        }
    }

}
