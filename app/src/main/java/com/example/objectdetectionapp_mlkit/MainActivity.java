package com.example.objectdetectionapp_mlkit;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.objectdetectionapp_mlkit.Helper.GraphicOverlay;
import com.example.objectdetectionapp_mlkit.Helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {



    private Button objectDetectButton;
    private GraphicOverlay graphicOverlay;
    private CameraView cameraView;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectDetectButton = findViewById(R.id.detect_object);
        graphicOverlay=findViewById(R.id.graphic_overlay);
        cameraView=findViewById(R.id.camera_view);
        alertDialog = new SpotsDialog.Builder().setContext(this).setMessage("Please Wait, processing ...").setCancelable(false).build();

        objectDetectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
            }
        });


        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                 alertDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap=Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();

                ProcessObjectDetection(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });




    }

    private void ProcessObjectDetection(Bitmap bitmap) {

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionObjectDetectorOptions options = new FirebaseVisionObjectDetectorOptions.Builder()
                .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build();
        FirebaseVisionObjectDetector detector = FirebaseVision.getInstance().getOnDeviceObjectDetector(options);

        detector.processImage(firebaseVisionImage)
                .addOnSuccessListener(
                        new OnSuccessListener<List<FirebaseVisionObject>>() {

                            public void onSuccess(List<FirebaseVisionObject> detectedObjects) {
                                // Task completed successfully
                                // ...

                                getObjectResults(detectedObjects);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                            }
                        });
    }

    private void getObjectResults(List<FirebaseVisionObject> detectedObjects) {

        int count=0;
        for(FirebaseVisionObject object:detectedObjects)
        {
            Rect rect = object.getBoundingBox();
            RectOverlay rectOverlay = new RectOverlay(graphicOverlay,rect);

            graphicOverlay.add(rectOverlay);

            count=count+1;
        }
        alertDialog.dismiss();
    }


}