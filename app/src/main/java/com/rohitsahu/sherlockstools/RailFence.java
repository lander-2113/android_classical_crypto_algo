package com.rohitsahu.sherlockstools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RailFence extends AppCompatActivity {

    private EditText raileditkeyfield;
    private EditText raileditmessagefield;
    private boolean flag;
    private int key;
    private String msg;
    private TextView railmessageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_fence);
        railmessageview = findViewById(R.id.railmessageview);
        raileditkeyfield = findViewById(R.id.raileditkeyfield);
        raileditmessagefield = findViewById(R.id.raileditmessagefield);
        flag = false;
        key = 0;
        msg = "";
        railmessageview.setText("Enter the 'Depth' and the message");
    }

    public boolean process(){
        msg = "";
        key = 0;
        String message = raileditmessagefield.getText().toString().toLowerCase();
        String temp = String.valueOf(raileditkeyfield.getText());
        if(temp.equals("") || message.equals(""))return false;
        int k = Integer.parseInt(temp);
        // processing the message.
        try{
            for(int i=0; i<message.length(); i++){
                if(message.charAt(i) >= 'a' || message.charAt(i) <= 'z'){
                    msg = msg + message.charAt(i);
                }else if(!(message.charAt(i) == ' ')){
                    throw new RuntimeException("Message contains invalid character");
                }
            }
            key = k%msg.length();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i("Cehcking", "process() run");
        return true;
    }

    public void Check(View view){
        flag = process();
        if(flag){
            Toast.makeText(getApplicationContext(), "key and message verified", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRadioButtonClicked(View view){
        Log.i("radio", "Clicked");
        if(flag){
            RadioGroup radioGroup = findViewById(R.id.railradio);
            int id = radioGroup.getCheckedRadioButtonId();
            switch(id){
                case R.id.railenc:
                    String cipher = getEncryptedData(msg, key);
                    railmessageview.setText("Cipher : " + cipher);
                    break;
                case R.id.raildec:
                    String plain = getDecryptedData(msg, key);
                    railmessageview.setText("Message: " + plain);
                    break;
            }
        }
        flag = false;
    }

    String getDecryptedData(String data, int numRails) {
        char[] decrypted = new char[data.length()];
        int n = 0;
        for(int k = 0 ; k < numRails; k ++) {
            int index = k;
            boolean down = true;
            while(index < data.length() ) {
                //System.out.println(k + " " + index+ " "+ n );
                decrypted[index] = data.charAt(n++);

                if(k == 0 || k == numRails - 1) {
                    index = index + 2 * (numRails - 1);
                }
                else if(down) {
                    index = index +  2 * (numRails - k - 1);
                    down = !down;
                }
                else {
                    index = index + 2 * k;
                    down = !down;
                }
            }
        }
        return new String(decrypted);
    }

    String getEncryptedData(String data, int numRails) {
        char[] encrypted = new char[data.length()];
        int n = 0;


        for(int k = 0 ; k < numRails; k ++) {
            int index = k;
            boolean down = true;
            while(index < data.length() ) {
                //System.out.println(k + " " + index+ " "+ n );
                encrypted[n++] = data.charAt(index);

                if(k == 0 || k == numRails - 1) {
                    index = index + 2 * (numRails - 1);
                }
                else if(down) {
                    index = index +  2 * (numRails - k - 1);
                    down = !down;
                }
                else {
                    index = index + 2 * k;
                    down = !down;
                }
            }
        }
        return new String(encrypted);
    }



}