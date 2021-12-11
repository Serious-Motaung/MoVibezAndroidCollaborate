package com.example.movibes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager{

    // Shared Preferences
    SharedPreferences sharedPreferences;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Constructor
    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("AppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    //Create set login method
    public void setLogin(boolean login){
        editor.putBoolean("KEY_LOGIN",login);
        editor.commit();
    }

    //Create get login method
    public boolean getLogin(){
        return sharedPreferences.getBoolean("KEY_LOGIN",false);
    }

    //Create set profile ID method
    public void setProfileID(String username){
        editor.putString("KEY_USERNAME",username);
        editor.commit();
    }

    //Create get profile ID method
    public String getProfileID(){
        return sharedPreferences.getString("KEY_USERNAME","");
    }

    //Create set profile ID method
    public void setUserRole(String role){
        editor.putString("KEY_ROLE_ID",role);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserRole(){
        return sharedPreferences.getString("KEY_ROLE_ID","");
    }

    //Create set profile ID method
    public void setUserProfilePic(String profilePic){
        editor.putString("KEY_PROFILE_PIC",profilePic);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserProfilePic(){
        return sharedPreferences.getString("KEY_PROFILE_PIC","");
    }

    //Create set profile ID method
    public void setUserEstablishment(String establishment){
        editor.putString("KEY_ESTABLISHMENT",establishment);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserEstablishment(){
        return sharedPreferences.getString("KEY_ESTABLISHMENT","");
    }

    //Create set profile ID method
    public void setUserName(String name){
        editor.putString("KEY_NAME",name);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserName(){
        return sharedPreferences.getString("KEY_NAME","");
    }

    //Create set profile ID method
    public void setUserAddress(String address){
        editor.putString("KEY_ADDRESS",address);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserAddress(){
        return sharedPreferences.getString("KEY_ADDRESS","");
    }

    //Create set profile ID method
    public void setUserContact(String address){
        editor.putString("KEY_ADDRESS",address);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserContact(){
        return sharedPreferences.getString("KEY_ADDRESS","");
    }

    //Create set profile ID method
    public void setUserId(String address){
        editor.putString("KEY_ADDRESS",address);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserId(){
        return sharedPreferences.getString("KEY_ADDRESS","");
    }

    //Create set profile ID method
    public void setUserGender(String address){
        editor.putString("KEY_ADDRESS",address);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserGender(){
        return sharedPreferences.getString("KEY_ADDRESS","");
    }

    //Create set profile ID method
    public void setUserInterests(String address){
        editor.putString("KEY_ADDRESS",address);
        editor.commit();
    }

    //Create get profile ID method
    public String getUserInterest(){
        return sharedPreferences.getString("KEY_ADDRESS","");
    }

}
