//package com.example.scafolmobile.dropbox;
//import android.content.Context;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//
//import com.dropbox.core.DbxException;
//import com.dropbox.core.v2.DbxClientV2;
//import com.dropbox.core.v2.files.FileMetadata;
//import com.dropbox.core.v2.files.WriteMode;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//
//public class UploadFileTask extends AsyncTask<String, Void, FileMetadata>{
//    private final Context mContext;
//    private final DbxClientV2 mDbxClient;
//    private final Callback mCallback;
//    private Exception mException;
//
//    public interface Callback {
//        void onUploadComplete(FileMetadata result);
//        void onError(Exception e);
//    }
//
//    public UploadFileTask(Context context, DbxClientV2 dbxClient, Callback callback) {
//        mContext = context;
//        mDbxClient = dbxClient;
//                mCallback = callback;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onPostExecute(FileMetadata fileMetadata) {
//        super.onPostExecute(fileMetadata);
//        if (mException != null) {
//            mCallback.onError(mException);
//        } else if (fileMetadata == null) {
//            mCallback.onError(null);
//        } else {
//            mCallback.onUploadComplete(fileMetadata);
//        }
//    }
//
//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }
//
////    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
////    @Override
////    protected FileMetadata doInBackground(String... strings) {
////        String localUri = strings[0];
////        File localFile = UriHelpers.getFileForUri(mContext, Uri.parse(localUri));
////
////        if (localFile != null) {
////            String remoteFolderPath = strings[1];
////            // Note - this is not ensuring the name is a valid dropbox file name
////            String remoteFileName = localFile.getName();
////            try (InputStream inputStream = new FileInputStream(localFile)) {
////                return mDbxClient.files().uploadBuilder(remoteFolderPath + "/" + remoteFileName)
////                        .withMode(WriteMode.OVERWRITE)
////                        .uploadAndFinish(inputStream);
////            } catch (DbxException | IOException e) {
////                mException = e;
////            }
////        }
////
////        return null;
////    }
//}
