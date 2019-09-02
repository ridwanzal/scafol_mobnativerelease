package com.release.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.android.Auth;

import com.release.R;

import java.io.IOException;

public class FragmentUploadData extends Fragment {

    private static final int STORAGE_PERMISSION_CODE = 123;
    int PICK_IMAGE_REQUEST = 1;
    LinearLayout btn_uploadimage;
    LinearLayout btn_uploadfile;
    private Uri fileUri;
    private Bitmap bitmap;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploaddata, container, false);
        btn_uploadimage = view.findViewById(R.id.btn_uploadimage);
        btn_uploadfile = view.findViewById(R.id.btn_uploadfile);

        btn_uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser(1);
            }
        });

        btn_uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser(2);
            }
        });

        return view;
    }

    private void fileChooser(int type){
        switch (type){
            case 1 :
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
            case 2 :
                Intent intent2 = new Intent();
                intent2.setType("file/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST){
            fileUri = data.getData();
        }
    }

}
