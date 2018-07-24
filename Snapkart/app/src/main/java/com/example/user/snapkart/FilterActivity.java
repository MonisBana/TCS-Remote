package com.example.user.snapkart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class FilterActivity extends AppCompatActivity {
    private Spinner mCategory;
    private String Category;
    private Button mDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mCategory = findViewById(R.id.category);
        mDone = findViewById(R.id.done);
        ArrayAdapter<String> categorys=new ArrayAdapter<String>(FilterActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.category_arrays));
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
        final DiscreteSeekBar discreteSeekBar1 =  findViewById(R.id.discrete1);
        discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value * 1000;
            }
        });
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int price = discreteSeekBar1.getProgress()*1000;
                Toast.makeText(FilterActivity.this,price+ "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FilterActivity.this,SearchActivity.class);
                i.putExtra("category",Category);
                i.putExtra("price",price);
                startActivity(i);
            }
        });
    }
}
