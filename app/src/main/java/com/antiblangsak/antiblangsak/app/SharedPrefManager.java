package com.antiblangsak.antiblangsak.app;
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

    public static final String HAS_FAMILY = "HAS_FAMILY";
    public static final String FAMILY_ID = "FAMILY_ID";
    public static final String FAMILY_STATUS = "FAMILY_STATUS";

    public static final String STATUS_LOGIN = "LOGIN";
    public static final String BANK_ACC_PHOTO_BASE64 = "BANK_ACC";
    public static final String KTP_PHOTO_BASE64 = "KTP";
    public static final String KK_PHOTO_BASE64 = "KK";
    public static final String CLAIM_PHOTO_BASE64 = "CLAIM";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(ANTIBLANGSAK_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void logout() {
        spEditor.remove(ID);
        spEditor.remove(EMAIL);
        spEditor.remove(TOKEN);
        spEditor.remove(STATUS_LOGIN);

        spEditor.remove(HAS_FAMILY);
        spEditor.remove(FAMILY_ID);
        spEditor.remove(FAMILY_STATUS);
        spEditor.commit();
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

    public String getPhoto(String key) {
        if (key.equals(BANK_ACC_PHOTO_BASE64)) {
            return getBankAccPhotoBase64();
        } else if (key.equals(CLAIM_PHOTO_BASE64)) {
            return getClaimPhotoBase64();
        } else if (key.equals(KTP_PHOTO_BASE64)){
            return getKtpPhotoBase64();
        } else if (key.equals(KK_PHOTO_BASE64)) {
            return getKkPhotoBase64();
        } else {
            return null;
        }
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

    public String getClaimPhotoBase64() {
        return sp.getString(CLAIM_PHOTO_BASE64, "");
    }

    public void clearPhotos() {
        spEditor.remove(BANK_ACC_PHOTO_BASE64);
        spEditor.remove(KTP_PHOTO_BASE64);
        spEditor.remove(KK_PHOTO_BASE64);
        spEditor.remove(CLAIM_PHOTO_BASE64);
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

    public Boolean hasFamily() {
        return sp.getBoolean(HAS_FAMILY, false);
    }

    public int getFamilyId() {
        return sp.getInt(FAMILY_ID, -2);
    }

    public int getFamilyStatus() {
        return sp.getInt(FAMILY_STATUS, -2);
    }
}