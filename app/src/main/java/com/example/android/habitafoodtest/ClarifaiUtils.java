package com.example.android.habitafoodtest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.Concept;
import com.clarifai.grpc.api.Data;
import com.clarifai.grpc.api.Image;
import com.clarifai.grpc.api.Input;
import com.clarifai.grpc.api.MultiOutputResponse;
import com.clarifai.grpc.api.PostModelOutputsRequest;
import com.clarifai.grpc.api.V2Grpc;
import com.clarifai.grpc.api.status.StatusCode;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ClarifaiUtils {
    private static StorageReference mStorageRef;
    static FirebaseStorage mStorage;
    static StorageReference storageRef;
    static ArrayList<Elements> data=new ArrayList<>();
    static Context context;
    public static void fetchData(byte[] bp,Context mcontext) {
            context=mcontext;
            mStorage=FirebaseStorage.getInstance();
            storageRef=mStorage.getReference();
            mStorageRef = storageRef.child("clarifai_photos");
            fetchUrl(bp);
        }

    private static void fetchUrl(byte[] bp) {
        final String[] url = {""};
        StorageReference photoUpload=mStorageRef.child("cPhoto");
        UploadTask uploadTask = photoUpload.putBytes(bp);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("Firebase:","upload failure! "+exception.getLocalizedMessage());
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(context,"Upload successful! Please wait...",Toast.LENGTH_SHORT).show();
            }
        });
        Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return photoUpload.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    url[0]=downloadUri.toString();
                    getClarifaiData(url[0]);
                    Log.i("onComplete:","url is generated");
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private static void getClarifaiData(String s) {
        V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getInsecureGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials("273bae03b5fc4157b80dee27acad750c"));
        MultiOutputResponse response = stub.postModelOutputs(
                PostModelOutputsRequest.newBuilder()
                        .setModelId("bd367be194cf45149e75f01d59f77ba7")
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setUrl(s)
                                        )
                                )
                        )
                        .build()
        );
        if (response.getStatus().getCode() != StatusCode.SUCCESS) {
            throw new RuntimeException("Request failed, status: " + response.getStatus());
        }

        for (Concept c : response.getOutputs(0).getData().getConceptsList()) {
/*
                System.out.println(String.format("%12s: %,.2f", c.getName(), c.getValue()));
*/
            String str=c.getName();
            char ch=str.charAt(0);
            ch=Character.toUpperCase(ch);
            str=ch+str.substring(1);
            float num=c.getValue();
            DecimalFormat df=new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);
            String n=df.format(num);
            data.add(new Elements(str,n));

        }

    }

    public static ArrayList<Elements> getData()
    {
        return data;
    }
}
