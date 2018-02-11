package com.antiblangsak.antiblangsak;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by martinovela on 12/23/17.
 */

public class SharedPrefManager {

    public static final String ANTIBLANGSAK_APP = "antiblangsakApp";

    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";
    public static final String TOKEN = "TOKEN";
    public static final String STATUS_LOGIN = "LOGIN";
    public static final String BANK_ACC_PHOTO_BASE64 = "BANK_ACC";
    public static final String KTP_PHOTO_BASE64 = "KTP";
    public static final String KK_PHOTO_BASE64 = "KK";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(ANTIBLANGSAK_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getBankAccPhotoBase64() {
        return sp.getString(BANK_ACC_PHOTO_BASE64, "");
    }

    public String getKtpPhotoBase64() {
        return sp.getString(KTP_PHOTO_BASE64, "");
    }

    public String getKkPhotoBase64() {
        return sp.getString(KK_PHOTO_BASE64, "");
    }

    public void clearPhotos() {
        spEditor.remove(BANK_ACC_PHOTO_BASE64);
        spEditor.remove(KTP_PHOTO_BASE64);
        spEditor.remove(KK_PHOTO_BASE64);
        spEditor.commit();
    }

    public String getName(){
        return sp.getString(NAME, "");
    }

    public String getEmail(){
        return sp.getString(EMAIL, "");
    }

    public int getId(){
        return sp.getInt(ID, 0);
    }

    public String getToken(){
        return sp.getString(TOKEN, "");
    }

    public Boolean getStatusLogin(){
        return sp.getBoolean(STATUS_LOGIN, false);
    }
}