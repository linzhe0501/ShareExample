package com.example.lin.shareexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class QrFragment extends Fragment {

    private String flag;

    public static QrFragment newInstance(String flag) {
        Bundle args = new Bundle();
        args.putString("FLAG", flag);
        QrFragment fragment = new QrFragment();
        fragment.setArguments(args);
        return fragment;
    }

    OnObtainQrCode qrCode;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        qrCode = (OnObtainQrCode) context;
        assert getArguments() != null;
        flag = getArguments().getString("FLAG");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageView imageView = view.findViewById(R.id.iv);
        view.findViewById(R.id.obtainQrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = qrCode.obtain(flag);
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    public interface OnObtainQrCode {
        Bitmap obtain(String flag);
    }

}
