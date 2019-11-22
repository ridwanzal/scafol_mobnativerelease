package com.release.dropbox;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dropbox.core.android.Auth;
import com.dropbox.core.android.DbxOfficialAppConnector;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.users.FullAccount;
import com.release.R;
import com.release.dropbox.internal.OpenWithActivity;
import com.release.service.ServiceReminder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

/**
 * Activity that displays the content of a path in dropbox and lets users navigate folders,
 * and upload/download files
 */
public class FilesActivityDirect extends DropboxActivity {
    private static final String TAG = FilesActivityDirect.class.getName();
    private static final String AUTHORITY="com.release.dropbox.FilesActivity.provider";
    SharedPreferences preferences;

    public final static String EXTRA_DETAIL = "";
    public final static String EXTRA_PATH = "";
    private static final int PICKFILE_REQUEST_CODE = 1;
    private static final int PICKFILE_CAMERA_REQUEST_CODE = 2;
    private String mPath;
    private String mDetail;
    private FilesAdapter mFilesAdapter;
    private FileMetadata mSelectedFile;
    private TextView count_files;
    private TextView paket_name;
    String mCurrentPhotoPath;
    private Uri mPhotoUri;
    private ImageView dump_image;
    private Button fab2;
    private Button fab;
    private Button fab3;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    public static Intent getIntent(Context context, String path) {
        Intent filesIntent = new Intent(context, FilesActivityDirect.class);
        filesIntent.putExtra(FilesActivityDirect.EXTRA_PATH, path);
        return filesIntent;
    }


    public static Intent getIntent(Context context, String path, String detail) {
        Intent filesIntent = new Intent(context, FilesActivityDirect.class);
        filesIntent.putExtra(FilesActivityDirect.EXTRA_PATH, path);
        filesIntent.putExtra(FilesActivityDirect.EXTRA_DETAIL, detail);
        return filesIntent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasToken()){
//            Toasty.success(getApplicationContext(),"Yes it is "  + hasToken(), Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String path = getIntent().getStringExtra(EXTRA_PATH);
        String pa_judul = getIntent().getStringExtra(EXTRA_DETAIL);
        mPath = path == null ? "" : path;
        mDetail = pa_judul == null ? "" : pa_judul;
        setContentView(R.layout.activity_files);
         fab = (Button)findViewById(R.id.fab);
         fab2 = (Button)findViewById(R.id.fab2);
         fab3 = (Button)findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performWithPermissions(FileAction.UPLOAD);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performWithPermissions(FileAction.CAMERA);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FilesActivityDirect.this, ActivityTag.class);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                String path = getIntent().getStringExtra(EXTRA_PATH);
                i.putExtra("path", path);
                startActivity(i);
//                performWithPermissions(FileAction.CAMERA);
            }
        });
    }

    private void launchFilePicker() {
        // Launch intent to pick file for upload
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    private void launchFilePickerImage() {
        // Launch intent to pick file for upload
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }


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

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Uri imageUri;
    private static int TAKE_PICTURE = 1;
    private void launchCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
                return true;
            case R.id.nav_reset :
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Reset token")
                        .setMessage("Apakah anda gagal meretrieve gambar? Reset token anda dan login kembali")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                                sessionManager.logoutUser();
                                preferences = getSharedPreferences("dropbox-sample", getApplicationContext().MODE_PRIVATE);
                                editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("MainActivity", "Aborting mission...");
                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_gallery, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            // This is the result of a call to launchFilePicker
                Log.d(TAG, "request code pick file gambar" +  data.toString());
                Log.d(TAG, data.toString());
                uploadFile(data.getData().toString());
            }
        }else if(requestCode == PICKFILE_CAMERA_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                dump_image = findViewById(R.id.dump_image);
                File file = new File(mCurrentPhotoPath);
                Bitmap imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media
                            .getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dump_image.setImageBitmap(imageBitmap);
                Uri result_uri = getImageUri(getApplicationContext(), imageBitmap);
                uploadFile(result_uri.toString());
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int actionCode,  String [] permissions,  int [] grantResults) {
        FileAction action = FileAction.fromCode(actionCode);

        boolean granted = true;
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Log.w(TAG, "User denied " + permissions[i] +
                        " permission to perform file action: " + action);
                granted = false;
                break;
            }
        }

        if (granted) {
            performAction(action);
        } else {
            switch (action) {
                case UPLOAD:
                    Toast.makeText(this,
                            "Can't upload file: read access denied. " +
                                    "Please grant storage permissions to use this functionality.",
                            Toast.LENGTH_LONG)
                        .show();
                    break;
                case DOWNLOAD:
                    Toast.makeText(this,
                            "Can't download file: write access denied. " +
                                    "Please grant storage permissions to use this functionality.",
                            Toast.LENGTH_LONG)
                        .show();
                    break;
                case CAMERA:
                    Toast.makeText(this,
                            "Can't download file: write access denied. " +
                                    "Please grant storage permissions to use this functionality.",
                            Toast.LENGTH_LONG)
                            .show();
                    break;
            }
        }
    }

    private void performAction(FileAction action) {
        switch(action) {
            case UPLOAD:
                Boolean contains = mPath.contains("documents");
                if(contains){
                    launchFilePicker();
                }else{
                    launchFilePickerImage();
                }
                break;
            case DOWNLOAD:
                if (mSelectedFile != null) {
                    downloadFile(mSelectedFile);
                } else {
                    Log.e(TAG, "Tidak ada file terpilih untuk di download");
                }
                break;
            case CAMERA:
                launchCamera();
                break;
            default:
                Log.e(TAG, "Can't perform unhandled file action: " + action);
        }
    }

    @Override
    protected void loadData() {
        final TextView notfoundfile = findViewById(R.id.notfoundfile);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Loading");
        dialog.show();

        if(hasToken()){
//            Toasty.success(getApplicationContext(), "Anda berhasil masuk folder", Toasty.LENGTH_LONG).show();
            new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
                @Override
                public void onComplete(FullAccount result) {
                    if(result.getEmail().equals("scafoltk@gmail.com")){
                        new ListFolderTask(DropboxClientFactory.getClient(), new ListFolderTask.Callback() {
                            @Override
                            public void onDataLoaded(ListFolderResult result) {
                                dialog.dismiss();
                                notfoundfile.setVisibility(View.GONE);
                                mFilesAdapter.setFiles(result.getEntries());
                            }

                            @Override
                            public void onError(Exception e) {
                                dialog.dismiss();
                                notfoundfile.setVisibility(View.VISIBLE);
                                findViewById(R.id.files_list).setVisibility(View.GONE);
                                Log.e(TAG, "Gagal mengambil file", e);
                                Toasty.normal(FilesActivityDirect.this,
                                        "Tidak ada item, silahkan upload",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }).execute(mPath);
                    }
//
                }

                @Override
                public void onError(Exception e) {

                }
            }).execute();

            //init picaso client
            PicassoClient.init(getApplicationContext(),DropboxClientFactory.getClient());
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.files_list);
            mFilesAdapter = new FilesAdapter(PicassoClient.getPicasso(), new FilesAdapter.Callback() {
                @Override
                public void onFolderClicked(FolderMetadata folder) {
                    startActivity(FilesActivityDirect.getIntent(FilesActivityDirect.this, folder.getPathLower()));
                }

                @Override
                public void onFileClicked(final FileMetadata file) {
                    mSelectedFile = file;
                    performWithPermissions(FileAction.DOWNLOAD);
                }
            });

//            Intent intents = getIntent();
//            String id_upload = intents.getStringExtra("upload_type");
            Boolean contains = mPath.contains("documents");
            if(contains){
                //            Toasty.success(getApplicationContext(), "ini dokumen", Toast.LENGTH_LONG).show();
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }else{
                //            Toasty.success(getApplicationContext(), "ini photo", Toast.LENGTH_LONG).show();
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
            }
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
            recyclerView.setAdapter(mFilesAdapter);
//            getSupportActionBar().setSubtitle(mFilesAdapter.getItemCount() + " Total Images");
            paket_name = findViewById(R.id.paket_name_oflistfiles);
            paket_name.setText(mDetail.toString());
            mSelectedFile = null;
        }

    }

    public void startServiceReminder(){
        Intent serviceIntent = new Intent(this, ServiceReminder.class);
        serviceIntent.putExtra("inputExtra", "Let's update our progress");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void downloadFile(FileMetadata file) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Fetching & Downloading Files");
        dialog.show();

        new DownloadFileTask(FilesActivityDirect.this, DropboxClientFactory.getClient(), new DownloadFileTask.Callback() {
            @Override
            public void onDownloadComplete(File result) {
                dialog.dismiss();

                if (result != null) {
                    viewFileInExternalApp(result);
                }
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Gagal mendownload file", e);
                Toasty.error(FilesActivityDirect.this,
                        "Error, Tidak dapat memproses",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(file);

    }

    private void viewFileInExternalApp(File result) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = result.getName().substring(result.getName().indexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        Uri photoUri = FileProvider.getUriForFile(this, "com.release.fileprovider", result);
        Intent intent = new Intent(Intent.ACTION_VIEW, photoUri);
        intent.setDataAndType(photoUri, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try{
            startActivity(intent);
        }catch (android.content.ActivityNotFoundException e){
            Log.e(TAG, e.toString());
        }
    }

    private void uploadFile(final String fileUri) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading");
        dialog.show();

        new UploadFileTask(this, DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
                dialog.dismiss();

//                String message = result.getName() + " size " + result.getSize() + " modified " +
//                        DateFormat.getDateTimeInstance().format(result.getClientModified());
//                Toast.makeText(FilesActivityDirect.this, message, Toast.LENGTH_SHORT)
//                        .show();

                // Reload the folder
                loadData();
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Failed to upload file.  :  " + e, e);
                Toasty.error(FilesActivityDirect.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(fileUri, mPath);
    }

    private void performWithPermissions(final FileAction action) {
        if (hasPermissionsForAction(action)) {
            performAction(action);
            return;
        }

        if (shouldDisplayRationaleForAction(action)) {
            new AlertDialog.Builder(this)
                    .setMessage("This app requires storage access to download and upload files.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissionsForAction(action);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            requestPermissionsForAction(action);
        }
    }

    private boolean hasPermissionsForAction(FileAction action) {
        for (String permission : action.getPermissions()) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldDisplayRationaleForAction(FileAction action) {
        for (String permission : action.getPermissions()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    private void requestPermissionsForAction(FileAction action) {
        ActivityCompat.requestPermissions(
                this,
                action.getPermissions(),
                action.getCode()
        );
    }

    private enum FileAction {
        DOWNLOAD(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        UPLOAD(Manifest.permission.READ_EXTERNAL_STORAGE),
        CAMERA(Manifest.permission.CAMERA);
        private static final FileAction [] values = values();

        private final String [] permissions;

        FileAction(String ... permissions) {
            this.permissions = permissions;
        }

        public int getCode() {
            return ordinal();
        }

        public String [] getPermissions() {
            return permissions;
        }

        public static FileAction fromCode(int code) {
            if (code < 0 || code >= values.length) {
                throw new IllegalArgumentException("Invalid FileAction code: " + code);
            }
            return values[code];
        }
    }
}
