package com.mab.merchantapp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText mNameField,mDescField,mPriceField,mQuantityFild;
    private Spinner mCategory;
    private Button mPostBtn;
    private ImageButton mSelectImage;
    private String Category;
    private ApiService mApiService;
    private Uri mImageUri = null;
    private static final int PERMISSIONS_REQUEST_READ_STORAGE =100 ;
    public static final int GALLERY_REQUEST = 1;
    File file;
    private String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNameField = findViewById(R.id.nameField);
        mDescField = findViewById(R.id.descField);
        mPriceField = findViewById(R.id.priceField);
        mQuantityFild = findViewById(R.id.quantityField);
        mCategory = findViewById(R.id.category);
        mPostBtn = findViewById(R.id.postBtn);
        mSelectImage = findViewById(R.id.imageSelect);
        mNameField.setText("jbjhbfdj");
        mDescField.setText("jbhj");
        mPriceField.setText("76");
        mQuantityFild.setText("226");
        mApiService = ApiUtils.getAPIService();
        ArrayAdapter<String> categorys=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.category_arrays));
        categorys.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(categorys);
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category = mCategory.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"),GALLERY_REQUEST);
            }
        });
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSIONS_REQUEST_READ_STORAGE);
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postProduct(mImageUri);
            }
        });
    }

    private void postProduct(Uri uri) {
        String name = mNameField.getText().toString();
        String desc = mDescField.getText().toString();
        String price = mPriceField.getText().toString();
        String quantity = mQuantityFild.getText().toString();
        String category = Category;
        String token = "Bearer " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","abc");
        RequestBody namePart = RequestBody.create(MultipartBody.FORM,name);
        RequestBody descPart = RequestBody.create(MultipartBody.FORM,desc);
        RequestBody pricePart = RequestBody.create(MultipartBody.FORM,price);
        RequestBody quantityPart = RequestBody.create(MultipartBody.FORM,quantity);
        RequestBody categoryPart = RequestBody.create(MultipartBody.FORM,category);
        Toast.makeText(this, name+desc+price+quantity+category+"", Toast.LENGTH_SHORT).show();
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("./upload/",file.getName() , reqFile);
       mApiService.postProduct(body,namePart,descPart,pricePart,quantityPart,categoryPart,token).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if(response.isSuccessful()){
                   Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {

           }
       });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);

            String wholeID = DocumentsContract.getDocumentId(mImageUri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(column[0]);
                if (cursor.moveToFirst())
                    mediaPath = cursor.getString(column_index);

                cursor.close();
            }
            if (mediaPath == null) {
                Toast.makeText(this, "media path", Toast.LENGTH_SHORT).show();
            } else
                file = new File(mediaPath);

            if (file == null) {
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
   public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        Toast.makeText(this, byteBuff.toByteArray()+"", Toast.LENGTH_SHORT).show();
        return byteBuff.toByteArray();
    }


}
