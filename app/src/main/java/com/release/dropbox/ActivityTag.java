package com.release.dropbox;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.release.R;
import com.release.activity.ActivityMyLocation;
import com.release.sharedexternalmodule.DateInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class ActivityTag extends AppCompatActivity {

    private static final String TAG = ActivityTag.class.getName();
    private static final String AUTHORITY="com.release.dropbox.FilesActivity.provider";
    private static final int PICKFILE_CAMERA_REQUEST_CODE = 2;
    private static final int MY_PERMISSION_OPEN_CAMERA = 3;
    private static final int MY_PERMISSION_LOCATION = 3;
    private static final int MY_PERMISSION_WRITE_STORAGE = 4;
    private static final int MY_PERMISSION_READ_STORAGE = 5;

    ImageView imageView;
    TextView latitude;
    TextView times;
    TextView longitude;
    RelativeLayout relativeLayout;
    Button camera;
    Button cme;
    String currentPhotoPath;

    private FilesAdapter mFilesAdapter;
    private FileMetadata mSelectedFile;
    private String mPath;
    private URI mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.img);
        longitude = findViewById(R.id.latlang);
        latitude = findViewById(R.id.latlang2);
        String path = getIntent().getStringExtra("path");
        mPath = path == null ? "" : path;
        camera = findViewById(R.id.camera);
        cme = findViewById(R.id.cme);
        relativeLayout = findViewById(R.id.main);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                dispatchTakePictureIntent();
//                if(checkCameraPermission()){
//                    if(checkLocationPermission()){
//                        if(checkWriteStorage()){
//                            if(checkReadStorage()){
//                                dispatchTakePictureIntent();
//                            }
//                        }
//                    }
//                }
                int PERMISSION_ALL = 1;
                String[] PERMISSIONS = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA
                };
                if(!checkAllPermission(this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(ActivityTag.this, PERMISSIONS, PERMISSION_ALL);
                    dispatchTakePictureIntent();
                }else{
                    dispatchTakePictureIntent();
                }

//                checkCameraPermission();
//                checkLocationPermission();
            }
        });

        cme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    checkWriteStorage();
//                    checkReadStorage();
                    camera.setVisibility(View.GONE);
                    cme.setVisibility(View.GONE);
                    Bitmap b = takescreenshot(relativeLayout);
                    imageView.setImageBitmap(b);
                    Uri result_uri = getImageUri(getApplicationContext(), b);
                    Toast.makeText(ActivityTag.this, "uri :" + result_uri, Toast.LENGTH_SHORT).show();
                    uploadFile(result_uri.toString());
                    camera.setVisibility(View.VISIBLE);
                    cme.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    e.printStackTrace();
                }
//                finish();
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkAllPermission(View.OnClickListener listener, String... permissions){
        if (listener != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle("Camera Permission")
                        .setMessage("Allow to use device camera")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(ActivityTag.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSION_OPEN_CAMERA);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(ActivityTag.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_OPEN_CAMERA);
            }
            return false;
        } else {
            return true;
        }
    }
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Allow to get your current location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(ActivityTag.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSION_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(ActivityTag.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean checkWriteStorage(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Allow to get your current location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(ActivityTag.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSION_WRITE_STORAGE);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_WRITE_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean checkReadStorage(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Allow to get your current location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(ActivityTag.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MY_PERMISSION_READ_STORAGE);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_READ_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(ActivityTag.this);

        if (requestCode == PICKFILE_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
//                Toast.makeText(this, "Berhasil hore", Toast.LENGTH_SHORT).show();
                File file = new File(currentPhotoPath);
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude.setVisibility(View.VISIBLE);
                            longitude.setVisibility(View.VISIBLE);
                            times.setVisibility(View.GONE);
                            latitude.setText("Latitude : " + location.getLatitude());
                            longitude.setText("Longitude : " + location.getLongitude());
                            times.setText(DateInfo.dateTime());
                        }
                    }
                });
                if (bitmap != null) {
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                }
                imageView.setImageBitmap(bitmap);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void uploadFile(String fileUri) {
        new UploadFileTask(this, DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
//                dialog.dismiss();

                String message = result.getName() + " size " + result.getSize() + " modified " +
                        DateFormat.getDateTimeInstance().format(result.getClientModified());
                Toast.makeText(ActivityTag.this, message, Toast.LENGTH_SHORT)
                        .show();
                new ListFolderTask(DropboxClientFactory.getClient(), new ListFolderTask.Callback() {
                    @Override
                    public void onDataLoaded(ListFolderResult result) {
//                        dialog.dismiss();
                        finish();
                        startActivity(FilesActivityDirect.getIntent(ActivityTag.this, mPath));
//                        notfoundfile.setVisibility(View.GONE);
//                        mFilesAdapter.setFiles(result.getEntries());
                    }

                    @Override
                    public void onError(Exception e) {
//                        dialog.dismiss();
//                        notfoundfile.setVisibility(View.VISIBLE);
                        Log.e(TAG, "Gagal mengambil file", e);
                        Toasty.normal(ActivityTag.this,
                                "Tidak ada item, silahkan upload",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }).execute(mPath);
            }

            @Override
            public void onError(Exception e) {
//                dialog.dismiss();

                Log.e(TAG, "Failed to upload file.", e);
                Toasty.error(ActivityTag.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(fileUri, mPath);
    }

    public static Bitmap takescreenshot(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

//    public static Bitmap takescreenshotOfRootView(View v){
//        return takescreenshot(v.getRootView());
//    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d(TAG, "photoFile: " +photoFile);
            } catch (IOException ex) {
                // Error occurred while creating the
                Log.e(TAG, ex.toString());
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.release.fileprovider",
                        photoFile);
                Log.d(TAG, "photoURI" + photoURI);
                Log.d(TAG, photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICKFILE_CAMERA_REQUEST_CODE);
            }
        }
    }


}
