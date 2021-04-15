package com.tiff.tiffinbox.Customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tiff.tiffinbox.R;

public class Description extends AppCompatActivity {

    TextView tvdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        tvdesc = findViewById(R.id.tvDescription);
        description();
    }

    private void description(){
        Intent desc = getIntent();
        String description;
        if (desc!=null)
        {
            description = desc.getStringExtra("desc");
            tvdesc.setText(description);
        }
    }
}