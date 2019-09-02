package com.lonewolf.techtaste.Resources;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {

    private SharedPreferences sharedPreferences;
    private final static String STORAGE = "Storage Variables";
    private final static String NOT_FIRST_LOGIN = "First login";
    private final static String FULL_NAME = "user full name";
    private final static String PHONE_NUM = "my phone num";
    private final static String EMAIL_ADDRESS = "my email add";
    private final static String USERROLE = "user rolz";
    private final static String FEATURETYPE = "feature type";
    private final static String KEEPLOGGEDIN = "keep me logged in";



    public Settings(Context context) {
        sharedPreferences = context.getSharedPreferences(STORAGE,
                Context.MODE_PRIVATE);
    }

    public Editor getEditor() {
        Editor editor = sharedPreferences.edit();
        return editor;
    }

    public SharedPreferences getPref() {
        return sharedPreferences;
    }


    public Boolean getNotFirstLogin(){
        return sharedPreferences.getBoolean(NOT_FIRST_LOGIN, false);
    }

    public void setNotFirstLogin(boolean notfirstLogin){
        Editor editor = getEditor();
        editor.putBoolean(NOT_FIRST_LOGIN, notfirstLogin);
        editor.commit();
    }

    public String getFullName(){
        return sharedPreferences.getString(FULL_NAME, "");
    }

    public void setFullName(String fullName){
        Editor editor = getEditor();
        editor.putString(FULL_NAME, fullName);
        editor.commit();
    }

    public String getPhoneNum(){
        return sharedPreferences.getString(PHONE_NUM, "");
    }

    public void setPhoneNum(String phoneNum){
        Editor editor = getEditor();
        editor.putString(PHONE_NUM, phoneNum);
        editor.commit();
    }


    public String getEmailAddress(){
        return sharedPreferences.getString(EMAIL_ADDRESS, "");
    }

    public void setEmailAddress(String emailAddress){
        Editor editor = getEditor();
        editor.putString(EMAIL_ADDRESS, emailAddress);
        editor.commit();
    }

    public String getUserrole(){
        return sharedPreferences.getString(USERROLE, "");
    }

    public void setUserrole(String userrole){
        Editor editor = getEditor();
        editor.putString(USERROLE,userrole );
        editor.commit();
    }

    public String getFeaturetype(){
        return sharedPreferences.getString(FEATURETYPE, "");
    }

    public void setFeaturetype(String featuretype){
        Editor editor =getEditor();
        editor.putString(FEATURETYPE, featuretype);
        editor.commit();
    }

    public boolean getKeeploggedin(){
        return sharedPreferences.getBoolean(KEEPLOGGEDIN, false);
    }

    public void setKeeploggedin(boolean keeploggedin){
        Editor editor = getEditor();
        editor.putBoolean(KEEPLOGGEDIN, keeploggedin);
        editor.commit();
    }
}
