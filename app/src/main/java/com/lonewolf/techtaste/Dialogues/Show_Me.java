package com.lonewolf.techtaste.Dialogues;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lonewolf.techtaste.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Show_Me {

    private static AlertDialog.Builder alert;
    private static AlertDialog dialog;
    public static void showPics(Activity activity, LinearLayout linearLayout, String title, List<Uri> list){
        alert = new AlertDialog.Builder(activity);
        dialog = alert.create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.layout_show_pictures_list, linearLayout, false);

        LinearLayout linear = view.findViewById(R.id.linImages);
        //TextView tTitle = view.findViewById(R.id.txtTitle);

        //tTitle.setText(title);

        for(int a= 0; a<list.size(); a++) {
            View views = inflater.inflate(R.layout.layout_show_images, linear, false);
            ImageView image = views.findViewById(R.id.imgPic);
            ImageView close = views.findViewById(R.id.imgClose);


            Picasso.with(activity).load(list.get(a)).into(image);

            linear.addView(views);

        }


        dialog.setView(view);
        dialog.show();

    }

}
