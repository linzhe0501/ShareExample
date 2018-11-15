package com.example.lin.shareexample;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareDialog extends DialogFragment {

    private FragmentActivity activity;
    private ImageView iv;

    public static ShareDialog newInstance(Bitmap bitmap, String title) {
        Bundle args = new Bundle();
        args.putParcelable("bitmap", bitmap);
        args.putString("title", title);
        ShareDialog fragment = new ShareDialog();
        fragment.setArguments(args);
        return fragment;
    }

    OnShareEvent shareEvent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        shareEvent = (OnShareEvent) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        Display d = getActivity().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams l = window.getAttributes();
        l.height = (int) (d.getHeight() * 0.8);
        l.width = (int) (d.getWidth() * 0.8);
        window.setAttributes(l);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_share, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle arguments = getArguments();
        final Bitmap bitmap = arguments.getParcelable("bitmap");
        String title = arguments.getString("title");
        ((TextView) view.findViewById(R.id.title)).setText(title);
        iv = view.findViewById(R.id.iv);
        iv.setImageBitmap(bitmap);

        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              todo 截图
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                final Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
                view.setDrawingCacheEnabled(false);
                shareEvent.onShare(b);
                dismiss();
            }
        });
    }

    public interface OnShareEvent {
        void onShare(Bitmap b);
    }
}
