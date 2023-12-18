package com.rohitsahu.sherlockstools;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CiptherActivity extends AppCompatActivity {

    EditText inputText;//input text field
    String caesarInput;
    EditText keyinput;//input key
    int caesarKey = 0;
    TextView displayMessageView;
    String caesarOutput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cipther);

        inputText = findViewById(R.id.inputText);//input message
        keyinput = findViewById(R.id.key);//input key
        displayMessageView = findViewById(R.id.display);//display text view

    }

    @SuppressLint({"NonConstantResourceId", "ShowToast"})
    public void onRadioButtonClicked(View view){
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        int id = radioGroup.getCheckedRadioButtonId();

        switch (id){
            case R.id.encryptButton:
                if( !inputText.getText().toString().matches("")  && !keyinput.getText().toString().matches("")){
                    caesarInput = inputText.getText().toString();
                    caesarKey = Integer.parseInt(keyinput.getText().toString());
                    if(caesarKey >1 && caesarKey <=25)
                        caesarOutput = caesarEncrypt(caesarInput, caesarKey);
                }
                break;

            case R.id.decryptButton:
                if(!inputText.getText().toString().matches("")  && !keyinput.getText().toString().matches("")){
                    caesarInput = inputText.getText().toString();
                    caesarKey = Integer.valueOf(keyinput.getText().toString());
                    if(caesarKey >1 && caesarKey <=25)
                        caesarOutput = caesarDecrypt(caesarInput, caesarKey);
                }
                break;
        }
    }

    public void ok(View view){
        if(!inputText.getText().toString().matches("")  && !keyinput.getText().toString().matches(""))
            displayMessageView.setText(caesarOutput);
        else
            Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
    }


    public static String caesarEncrypt(String plainText, int key){
        plainText = plainText.toLowerCase();
        String cipher = "";
        for(int i=0; i<plainText.length(); i++){
            if( plainText.charAt(i) >= 'a' && plainText.charAt(i) <= 'z' ){
                char c = (char)(plainText.charAt(i) + key);
                if( c > 'z')
                    c = (char)(c-122 + 96);
                cipher += String.valueOf(c);
            }
        }
        return cipher.toUpperCase();
    }

    public static String caesarDecrypt(String cipher, int key) {
        cipher = cipher.toLowerCase();
        String plainText = "";
        for (int i = 0; i < cipher.length(); i++) {
            if (cipher.charAt(i) >= 'a' && cipher.charAt(i) <= 'z') {
                char p = (char) (cipher.charAt(i) - key);
                if (p < 'a')
                    p = (char) (p + 26);
                plainText += p;
            }
        }
        return plainText;
    }

}