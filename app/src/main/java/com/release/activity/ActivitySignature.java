package com.release.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.release.R;
import com.thyme.smalam119.easysignatureview.SignatureViewActivity;

import java.util.Date;

public class ActivitySignature extends SignatureViewActivity {
    private TextView mTextMessage;
    private Date mDate;
    private Bitmap mSignatureBitmap;
    private byte[] mSignatureInBytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    @Override
    public void onClickDone(Bitmap bitmap) {
         //get the date of the signature
        mSignatureBitmap = bitmap; // signature in bitmap
    }

    @Override
    public void onClickDone(byte[] bytes) {
        mSignatureInBytes = bytes;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void setSubText(String subText) {
        super.setSubText(subText);
    }

    @Override
    public void onCancelButtonClicked() {
        super.onCancelButtonClicked();
    }

    @Override
    public void onDoneButtonClicked(Bitmap bitmap) {
        super.onDoneButtonClicked(bitmap);
    }

    @Override
    public void onClickCancel() {

    }
}
