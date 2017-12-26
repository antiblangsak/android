package com.antiblangsak.antiblangsak;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by martinovela on 12/23/17.
 */

public class SharedPrefManager {

    public static final String antiblangsak_app = "antiblangsakApp";

    public static final String name = "name";
    public static final String email = "email";
    public static final String id = "id";
    public static final String token = "token";

    public static final String status_login = "LOGIN";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(antiblangsak_app, Context.MODE_PRIVATE);
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

    public String getName(){
        return sp.getString(name, "");
    }

    public String getEmail(){
        return sp.getString(email, "");
    }

    public int getId(){
        return sp.getInt(id, 0);
    }

    public String getToken(){
        return sp.getString(token, "");
    }

    public Boolean getStatusLogin(){
        return sp.getBoolean(status_login, false);
    }


}