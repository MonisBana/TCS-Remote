package com.example.user.merchantapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProduct extends AppCompatActivity {

    private EditText mNameField,mDescField,mPriceField,mQuantityFild;
    private Spinner mCategory;
    private Button mUpdateBtn;
    private ImageButton mSelectImage;
    private String Category;
    private Uri mImageUri = null;
    private static final int PERMISSIONS_REQUEST_READ_STORAGE =100 ;
    public static final int GALLERY_REQUEST = 1;
    private DatabaseReference mPostReference,mUserReference;
    private ProgressDialog mProgressDialog;
    private StorageReference mStorageReference;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        mNameField = findViewById(R.id.nameField);
        mDescField = findViewById(R.id.descField);
        mPriceField = findViewById(R.id.priceField);
        mQuantityFild = findViewById(R.id.quantityField);
        mCategory = findViewById(R.id.category);
        mUpdateBtn = findViewById(R.id.updateBtn);
        mSelectImage = findViewById(R.id.imageSelect);
        mProgressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("productId");
        Category = intent.getStringExtra("category");
        mPostReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mUserReference = FirebaseDatabase.getInstance().getReference().child("User");
        mStorageReference = FirebaseStorage.getInstance().getReference();
        /*mNameField.setText("jbjhbfdj");
        mDescField.setText("jbhj");
        mPriceField.setText("76");
        mQuantityFild.setText("226");*/
        String name = intent.getStringExtra("name");
        String desc = intent.getStringExtra("desc");
        int price = intent.getIntExtra("price",1);
        int quantity = intent.getIntExtra("quantity",1);
        String image = intent.getStringExtra("image");
        //Toast.makeText(this,price+ "", Toast.LENGTH_SHORT).show();
        mNameField.setText(name);
        mDescField.setText(desc);
        mPriceField.setText(price+"");
        mQuantityFild.setText(quantity+"");
        Picasso.with(getApplicationContext()).load(image).into(mSelectImage);
        ArrayAdapter<String> categorys=new ArrayAdapter<String>(EditProduct.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.category_arrays));
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
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();

            }
        });
    }

    private void startPosting() {
        mProgressDialog.setMessage("Posting Product..");
        mProgressDialog.show();
        StorageReference filepath = mStorageReference.child("Blog_Images").child(mImageUri.getLastPathSegment());
        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String name = mNameField.getText().toString();
                String desc = mDescField.getText().toString();
                String image = downloadUrl.toString();
                int  quantity = Integer.parseInt(String.valueOf(mQuantityFild.getText()));
                int  price = Integer.parseInt(String.valueOf(mPriceField.getText()));
                String userId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("userId","abc");
                Product product = new Product(name, desc, image,id,userId,Category, price, quantity);
                mPostReference.child(Category).child(id).setValue(product);
                mUserReference.child(userId).child("Products").child(id).setValue(product);
                mProgressDialog.dismiss();
                Intent i = new Intent(EditProduct.this,MainActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
