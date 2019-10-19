package com.azizova.loftmoney;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AddItemActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private Button mAddButton;
    String type;

    private String mName;
    private String mPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

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
        setTextColorForType(mNameEditText);
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
        setTextColorForType(mPriceEditText);

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
        Boolean enabled = !TextUtils.isEmpty(mName)&&!TextUtils.isEmpty(mPrice);
        mAddButton.setEnabled(enabled);
        setTextColorForType(mAddButton);
    }

    private void setTextColorForType(TextView textView){
        switch (type){
            case MainActivity.EXPENSE:
                if (textView instanceof Button){
                    if (textView.isEnabled()) {
                        textView.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue));
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_blue, 0, 0, 0);
                    }
                }
                else
                    textView.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue));
                break;
            case MainActivity.INCOME:
                if (!(textView instanceof Button)){
                    textView.setTextColor(ContextCompat.getColor(this, R.color.apple_green));
                }
                break;

        }
    }
}
