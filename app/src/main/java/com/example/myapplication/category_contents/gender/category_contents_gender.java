package com.example.myapplication.category_contents.gender;

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.myapplication.CustomAdapter;
//import com.example.myapplication.R;
//import com.example.myapplication.community_detail;
//import com.example.myapplication.model.community_user;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.CustomAdapter;
import com.example.myapplication.R;
import com.example.myapplication.community_detail;
import com.example.myapplication.model.category_user;
import com.example.myapplication.model.community_user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class category_contents_gender extends Fragment {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.category_contents_gender);
//    }

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManger;
    private ArrayList<community_user> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Context context;
    private String DestinationUid;
    //=================================gps


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_commuity, container, false);

        context = rootview.getContext();
        recyclerView = rootview.findViewById(R.id.recylerview); //?????? ??????
        recyclerView.setHasFixedSize(true); //?????????????????? ???????????? ??????
        layoutManger = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManger);
        arrayList = new ArrayList<>(); //User ????????? ?????? ????????? ????????? (????????? ?????????)
        database = FirebaseDatabase.getInstance(); //?????????????????? ?????????????????? ??????

        databaseReference = database.getReference("community"); //????????? ??????????????? user
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // ?????????????????? ????????????????????? ???????????? ???????????? ???
                arrayList.clear(); //?????? ?????????????????? ???????????? ?????? ?????????
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //??????????????? ????????? list??? ????????????

                    community_user user1 = snapshot.getValue(community_user.class); //??????????????? User ????????? ???????????? ??????
                    if(!user1.Cause1.contains("???")) {
                        continue;
                    }

                    arrayList.add(user1); // ?????? ??????????????? ?????????????????? ?????? ????????????????????? ?????? ??????
                }
                adapter.notifyDataSetChanged(); //????????? ?????? ??? ????????????
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        adapter = new CustomAdapter(arrayList, context, new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Intent intent = new Intent(view.getContext(), community_detail.class);
                final String destinationUid = arrayList.get(position).uid;
                final String date1 = arrayList.get(position).date;

                final Bundle InfoBundle = new Bundle();
                DestinationUid = destinationUid;
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //??????????????? ????????? list??? ????????????
                            String uid = snapshot.child("uid").getValue(String.class); //uid
                            String date2 = snapshot.child("date").getValue(String.class); //uid

                            if (uid.equals(destinationUid)&& date1.equals(date2)) {
                                InfoBundle.putString("destinationUid", destinationUid);
                                InfoBundle.putString("date", date2);
                                InfoBundle.putString("writing", snapshot.child("writing").getValue(String.class));
                                intent.putExtras(InfoBundle);

                                startActivity(intent);
                                break;
                            } else {
                                continue;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }
        });
        recyclerView.setAdapter(adapter); //????????????????????? ????????? ??????
        return rootview;
    }
}



