package com.example.user.snapkart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudDocumentTextDetector;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudText;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class HomeActivity extends Activity implements OnClickListener, SurfaceHolder.Callback, Camera.PictureCallback {
    SurfaceView cameraView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    private String result;
    private TextView mResult;
    private ImageButton mClick,mSearch;
    private String Name,Mobile,Email,Address;
    private DatabaseReference mCustomerReference;
    private String CustomerId;
    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        Mobile = intent.getStringExtra("mobile");
        Email = intent.getStringExtra("email");
        Address = intent.getStringExtra("address");
        pd = new ProgressDialog(HomeActivity.this);
        cameraView = (SurfaceView) this.findViewById(R.id.surfaceView1);
        mClick = findViewById(R.id.click);
        mSearch = findViewById(R.id.searchBtn);
        mCustomerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
        surfaceHolder = cameraView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        cameraView.setFocusable(true);
        cameraView.setFocusableInTouchMode(true);
        mClick.setOnClickListener(this);
        mSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                result="";
                pd.dismiss();
                Intent i  = new Intent(HomeActivity.this,SearchActivity.class);
                i.putExtra("result",result);
                startActivity(i);
            }
        });
        mCustomerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
        Boolean login = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("login",Boolean.TRUE);
        //Toast.makeText(this, UserId+"", Toast.LENGTH_SHORT).show();
        if(login.equals(Boolean.FALSE)){
            String id = mCustomerReference.push().getKey();
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("customerId",id).apply();
            Customer customer = new Customer(Name,Mobile,Email,id,Address);
            mCustomerReference.child(id).setValue(customer);
            //Toast.makeText(this,"Login", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClick(View v) {
        if(mClick.isPressed()){
            mClick.setImageResource(R.drawable.camera_in);
        }
        else {
            mClick.setImageResource(R.drawable.camera_act);
        }
        pd.show();
        pd.setMessage("Please wait.....");
        camera.takePicture(null, null, this);
    }
    public void onPictureTaken(byte[] data, Camera camera) {
        //Uri imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        runTextRecognition(bitmap);
        /*try {
            OutputStream imageFileOS = getContentResolver().openOutputStream(
                    imageFileUri);
            imageFileOS.write(data);
            imageFileOS.flush();
            imageFileOS.close();
        } catch (Exception e) {
            Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            t.show();
        }*/
        camera.startPreview();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        camera.startPreview();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
            Camera.Parameters parameters = camera.getParameters();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
            }
            List<String> colorEffects = parameters.getSupportedColorEffects();
            Iterator<String> cei = colorEffects.iterator();
            while (cei.hasNext()) {
                String currentEffect = cei.next();
                if (currentEffect.equals(Camera.Parameters.EFFECT_SOLARIZE)) {
                    parameters.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
                    break;
                }
            }
            camera.setParameters(parameters);
        } catch (IOException exception) {
            camera.release();
        }
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
    }
    private void runTextRecognition(Bitmap bitmap) {
                                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                                    FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                                            .getVisionTextDetector();
                                    detector.detectInImage(image)
                                            .addOnSuccessListener(
                                                    new OnSuccessListener<FirebaseVisionText>() {
                                                        @Override
                                                        public void onSuccess(FirebaseVisionText texts) {

                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception

                                e.printStackTrace();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<FirebaseVisionText>() {
            @Override
            public void onComplete(@NonNull Task<FirebaseVisionText> task) {
                pd.dismiss();
                Intent i  = new Intent(HomeActivity.this,SearchActivity.class);
                i.putExtra("result",result);
                startActivity(i);
            }
        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        result = "";
        List<FirebaseVisionText.Block> blocks = texts.getBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(this, "No Text Found", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    result = result+ elements.get(k).getText()+" " ;
                }
            }
        }
    }
/*   private void runTextRecognition(Bitmap bitmap) {
       FirebaseVisionCloudDetectorOptions options =
               new FirebaseVisionCloudDetectorOptions.Builder()
                       .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                       .setMaxResults(15)
                       .build();
       FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
       FirebaseVisionCloudDocumentTextDetector detector = FirebaseVision.getInstance()
               .getVisionCloudDocumentTextDetector(options);
       detector.detectInImage(image)
               .addOnSuccessListener(
                       new OnSuccessListener<FirebaseVisionCloudText>() {
                           @Override
                           public void onSuccess(FirebaseVisionCloudText texts) {

                               processTextRecognitionResult(texts);
                           }
                       })
               .addOnFailureListener(
                       new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               // Task failed with an exception

                               e.printStackTrace();
                           }
                       }).addOnCompleteListener(new OnCompleteListener<FirebaseVisionCloudText>() {
           @Override
           public void onComplete(@NonNull Task<FirebaseVisionCloudText> task) {
               Intent i  = new Intent(HomeActivity.this,SearchActivity.class);
               i.putExtra("result",result);
               startActivity(i);
           }
       });
   }

    private void processTextRecognitionResult(FirebaseVisionCloudText text) {
        result = "";
        if (text == null) {
            Toast.makeText(this, "No text Found", Toast.LENGTH_SHORT).show();
            return;
        }
        List<FirebaseVisionCloudText.Page> pages = text.getPages();
        for (int i = 0; i < pages.size(); i++) {
            FirebaseVisionCloudText.Page page = pages.get(i);
            List<FirebaseVisionCloudText.Block> blocks = page.getBlocks();
            for (int j = 0; j < blocks.size(); j++) {
                List<FirebaseVisionCloudText.Paragraph> paragraphs = blocks.get(j).getParagraphs();
                for (int k = 0; k < paragraphs.size(); k++) {
                    FirebaseVisionCloudText.Paragraph paragraph = paragraphs.get(k);
                    List<FirebaseVisionCloudText.Word> words = paragraph.getWords();
                    for (int l = 0; l < words.size(); l++) {
                        FirebaseVisionCloudText.Word word = words.get(k);
                        List<FirebaseVisionCloudText.Symbol> symbols = word.getSymbols();
                        for (int m = 0; m < symbols.size(); m++) {
                            result = result+ symbols.get(k).getText()+" " ;
                        }
                    }
                }
            }
        }
    }*/
}
