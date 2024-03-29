package com.lonewolf.techtaste.Resources;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ShortCut_To {
    public static final String DATEWITHTIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATEWITHTIMEDDMMYYY = "dd-MM-yyyy'T'HH:mm:ss.SSS'Z'";
    public static final String DATEFORMATDDMMYYYY = "dd/MM/yyyy";
    public static final String DATEFORMATYYYYMMDD = "yyyy-MM-dd";
    public static final String TIME = "hh:mm a";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String[] dropList = {"Select type", "Flyers", "Posters", "Stickers", "Logo", "Business cards", "Invitation cards", "Letterhead", "Calendar", "Testimonials"};

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMATYYYYMMDD, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDateFormat2() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMATDDMMYYYY, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDatewithTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEWITHTIMEDDMMYYY, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String[] getServices = {"Select Service",  "Technical Support", "Android App",  "Web App","Apple App", "Website Development", "Enquiries", "Other"};

    public static Bitmap decodeBase64(String input) {

        try {
            byte[] decodedByte = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedByte, 0,
                    decodedByte.length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    public static String getTimeFromDate(String str){
        if(str != null && !str.equalsIgnoreCase("null") && str.trim().length()!=0){
            SimpleDateFormat sdf1 = new SimpleDateFormat(DATEWITHTIME, Locale.US);
            SimpleDateFormat sdf2 = new SimpleDateFormat(TIME, Locale.US);

            try {
                Date date = sdf1.parse(str);
                return sdf2.format(date);
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }else {
            return " ";
        }
    }

}
