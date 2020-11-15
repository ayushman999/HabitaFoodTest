package com.example.android.habitafoodtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.V2Grpc;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.ByteString;

import io.grpc.Channel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Prediction extends AppCompatActivity {

    static RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    static ClarifaiAdapter mAdapter;
    static ArrayList<Elements> item;
    private StorageReference mStorageRef;
    Bitmap bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        Toast.makeText(this,"inside Prediction",Toast.LENGTH_SHORT).show();
        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        item=ClarifaiUtils.getData();
        mAdapter=new ClarifaiAdapter(item);
        recyclerView.setAdapter(mAdapter);


    }


}