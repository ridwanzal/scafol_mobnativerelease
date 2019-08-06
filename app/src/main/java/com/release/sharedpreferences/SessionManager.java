package com.release.sharedpreferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;

import com.release.activity.ActivityLogin;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = ",";

    public static final String KEY_USERID = "userid";

    public static final String KEY_USERNAME = "username";

    public static final String KEY_DINASID = "dinasid";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_ROLE = "role";

    public static final String KEY_BIDANG ="bi_id";

    public SessionManager(Context mContext){
        this._context = mContext;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSessionUsername(String nama, String username, String userid, String dinasid, String email, String role, String bi_id){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, nama);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_DINASID, dinasid);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_BIDANG, role);
        editor.commit();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, ActivityLogin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_DINASID, pref.getString(KEY_DINASID, null));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));


        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, ActivityLogin.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
}
