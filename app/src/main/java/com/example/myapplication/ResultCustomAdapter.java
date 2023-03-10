package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.face_user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ResultCustomAdapter extends BaseAdapter {
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private Face[] faces;
    private Context context;
    private LayoutInflater inflater;
    private Bitmap orig;

//    Context mContext = context.getApplicationContext();


    public ResultCustomAdapter(Face[] faces, Context context, Bitmap orig) {
        this.faces = faces;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.orig = orig;
    }


    @Override
    public int getCount() {
        return faces.length;
    }

    @Override
    public Object getItem(int position) {
        return faces[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.listview_layout, null);
        }

        TextView age, gender, smile;

        ImageView imageView;

        age = view.findViewById(R.id.textAge);
        gender = view.findViewById(R.id.textGender);
        smile = view.findViewById(R.id.textSmile);

        imageView = view.findViewById(R.id.imgThumb);

        age.setText("??????: " + faces[position].faceAttributes.age);
        gender.setText("??????: " + faces[position].faceAttributes.gender);

        TreeMap<Double, String> treeMap = new TreeMap<>();
        treeMap.put(faces[position].faceAttributes.emotion.happiness, "??????");
        treeMap.put(faces[position].faceAttributes.emotion.anger, "??????");
        treeMap.put(faces[position].faceAttributes.emotion.disgust, "??????");
        treeMap.put(faces[position].faceAttributes.emotion.sadness, "??????");
        treeMap.put(faces[position].faceAttributes.emotion.neutral, "??????");
        treeMap.put(faces[position].faceAttributes.emotion.surprise, "??????");
        treeMap.put(faces[position].faceAttributes.emotion.fear, "?????????");

        ArrayList<Double> arrayList = new ArrayList<>();
        TreeMap<Integer, String> rank = new TreeMap<>();

        int counter = 0;
        for (Map.Entry<Double, String> entry : treeMap.entrySet()) {
            String key = entry.getValue();
            Double value = entry.getKey();
            rank.put(counter, key);
            counter++;
            arrayList.add(value);
        } ;


        smile.setText(rank.get(rank.size() - 1) + ": " + 100 * arrayList.get(rank.size() - 1) + "% " + rank.get(rank.size() - 2) + ": " + 100 * arrayList.get(rank.size() - 2) + "%");
        face_user user = new face_user();
        user.sentiment = rank.get(rank.size() - 1);
        user.percent = String.valueOf(Math.floor(100 * arrayList.get(rank.size() - 1))) + "%";
        user.myuid = myUid;
        FirebaseDatabase.getInstance().getReference().child("face recognition").child(myUid).setValue(user);

        FaceRectangle faceRectangle = faces[position].faceRectangle;
        Bitmap bitmap = Bitmap.createBitmap(orig, faceRectangle.left, faceRectangle.top, faceRectangle.width, faceRectangle.height);

        if((Objects.equals(rank.get(rank.size() - 1), "??????") && 100 * arrayList.get(rank.size() - 1) > 10) ||
                (Objects.equals(rank.get(rank.size() - 1), "??????") && 100 * arrayList.get(rank.size() - 1) > 15) ||
                (Objects.equals(rank.get(rank.size() - 1), "?????????") && 100 * arrayList.get(rank.size() - 1) > 15)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("????????? ?????? ????????? ????????????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Dialog", "??????");
                        Toast.makeText(context.getApplicationContext(), "??????", Toast.LENGTH_LONG).show();
                    }
                });
            builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context.getApplicationContext(),"?????? ?????? ?????????~~~~", Toast.LENGTH_LONG).show();

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }



        imageView.setImageBitmap(bitmap);
        imageView.setImageBitmap(bitmap);
        return view;
    }
}



