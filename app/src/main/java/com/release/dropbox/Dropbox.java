package com.release.dropbox;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.DbxDownloadStyleBuilder;
import com.dropbox.core.v2.users.FullAccount;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

// dropbox module
public class Dropbox {
    private static final String ACCESS_TOKEN = "sl.AHvAGES5f6SyrQN0rann12OWfRklDgvwqZe5ipXiUaXQ-oDDZN-l0-efmvy5LkOy4Mpw9j882DK8oRID4a3_HfTGwPL1f-Ox8GXBM_lnsSuE8PKAIr-kDiUovPvkidzgKksn1C3Q";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public static void main(String[] args) throws DbxException, IOException {
            // Create Dropbox client
            DbxRequestConfig config = new DbxRequestConfig("", "en_US");
            DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

            // Get current account info
            FullAccount account = client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());

            // Get files and folder metadata from Dropbox root directory
            ListFolderResult result = client.files().listFolder("");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    System.out.println(metadata.getPathLower());
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }

            // Upload "test.txt" to Dropbox
            try (InputStream in = new FileInputStream("test.txt")) {
                FileMetadata metadata = client.files().uploadBuilder("/test.txt")
                        .uploadAndFinish(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
