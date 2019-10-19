package com.azizova.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AddItemActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private Button mAddButton;

    private String mName;
    private String mPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mNameEditText = findViewById(R.id.name_edittext);
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
                checkEditTextHasText();
            }
        });
        mPriceEditText = findViewById(R.id.price_edittext);
        mPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPrice = s.toString();
                checkEditTextHasText();
            }
        });

        mAddButton = findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice)) {
                    setResult(RESULT_OK, new Intent().putExtra("name", mName).putExtra("price", mPrice));
                }
                finish();
            }
        });
    }

    public void checkEditTextHasText(){
        mAddButton.setEnabled(!TextUtils.isEmpty(mName)&&!TextUtils.isEmpty(mPrice));
    }
}
