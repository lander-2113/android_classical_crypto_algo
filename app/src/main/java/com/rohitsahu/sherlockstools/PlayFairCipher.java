package com.rohitsahu.sherlockstools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class PlayFairCipher extends AppCompatActivity {


    private String keyMatrix;// for storing the actual key matrix.
    private String actualMessageFromEditText;
                                // for the message that is either plaintext or citpher text
    private String KeyWord;// for the key word after the duplicates has been removed
    private String Key;// for the 25 character with keyword as the first some first characters.
    private char   matrix_arr[][];// the key matrix.
    private EditText keyField;// keyword from the user.
    private EditText messageField;// message from the user.
    private TextView matrixView;
    private TextView textviewn;
    private boolean flag;
    // flag to check if the keyword and message are according to the algortihm.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_fair_cipher);
        // initializing the variables.
        textviewn = findViewById(R.id.textviewn);
        matrixView = findViewById(R.id.matrix);
        keyMatrix = "";
        KeyWord = "";
        Key = "";
        matrix_arr = new char[5][5];
        keyField = findViewById(R.id.keyField);
        messageField = findViewById(R.id.messageField);
        flag = false;
    }

    public void onRadioButtonClicked(View view){
        RadioGroup radioGroup = findViewById(R.id.ed);
        int id = radioGroup.getCheckedRadioButtonId();
        if(!flag){// when either of the input is not correct.
            Toast.makeText(getApplicationContext(),
                    "Invalid Input", Toast.LENGTH_SHORT).show();
        }else{
            TextView head = findViewById(R.id.testView);
            textviewn.setVisibility(View.VISIBLE);
            matrixView.setText(keyMatrix);

            TextView messageText = findViewById(R.id.messageView);
            switch (id){
                case R.id.encRadio:
                    String cipherText = encryptMessage(actualMessageFromEditText);
                    head.setText("Encrypted");
                    messageText.setText(cipherText.toUpperCase());
                    break;
                case R.id.decRadio:
                    String plainText = decryptMessage(actualMessageFromEditText);
                    head.setText("Decrypted");
                    messageText.setText(plainText.toLowerCase());
                    break;
                default:
                    Toast.makeText(getApplicationContext(),
                            "Invalid Input", Toast.LENGTH_SHORT).show();
            }
        }
        // reset the variables
        reset();
    }
    public void reset(){
        flag = false;
        actualMessageFromEditText = "";
        keyMatrix = "";
        KeyWord = "";
        Key = "";
    }

    public void OK(View view){
        String key = process(keyField.getText().toString(), true);
        actualMessageFromEditText = process(messageField.getText().toString(), false);
        if(!key.equals("1") && !actualMessageFromEditText.equals("1")){
            setKey(key);
            KeyGen();
            flag = true;
            Toast.makeText(getApplicationContext(), "Now select to encrypt or decrypt",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),
                    "Invalid Input", Toast.LENGTH_SHORT).show();
        }

    }

    public void setKey(String k) {
        // removes duplicates from the key.
        String K_adjust = new String();
        boolean flag = false;
        K_adjust = K_adjust + k.charAt(0);
        for (int i = 1; i < k.length(); i++) {
            for (int j = 0; j < K_adjust.length(); j++) {
                if (k.charAt(i) == K_adjust.charAt(j)) {
                    flag = true;
                }
            }
            if (!flag)
                K_adjust = K_adjust + k.charAt(i);
            flag = false;
        }
        KeyWord = K_adjust;
    }

    public void KeyGen(){
        // adds the remaining alphabets to the modified key
        // and generates the matrix key.
        boolean flag = true;
        char current;
        Key = KeyWord;

        for (int i = 0; i < 26; i++){
            current = (char) (i + 97);

            if (current == 'j')
                continue;

            for (int j = 0; j < KeyWord.length(); j++){
                // checking if the alphabet is contained in the,
                // modified key. If it is contained then it is
                // not added to the key.
                if (current == KeyWord.charAt(j)){
                    flag = false;
                    break;
                }
            }

            if (flag)
                Key = Key + current;

            flag = true;
        }
        //System.out.println(Key);
        matrix();
    }

    private void matrix(){
        int counter = 0;
        keyMatrix = "";
        for (int i = 0; i < 5; i++){
            String s = " ";
            for (int j = 0; j < 5; j++){
                matrix_arr[i][j] = Key.charAt(counter);
                keyMatrix = keyMatrix + s + String.format(Locale.getDefault(), "%c ", matrix_arr[i][j]);
                s = "";
                counter++;
            }
            keyMatrix = keyMatrix + ((i != 4)?"\n":"");
        }
    }

    private String format(String old_text) {
        // modifies duplicate pairs, in the plain text.
        int i = 0;
        int len = 0;
        String text = new String();
        len = old_text.length();
        for (int tmp = 0; tmp < len; tmp++) {// finding 'j' and placing i in place of it.
            if (old_text.charAt(tmp) == 'j') {
                text = text + 'i';
            }else
                text = text + old_text.charAt(tmp);
        }
        len = text.length();
        for (i = 0; i < len; i = i + 2) {
            if (text.charAt(i + 1) == text.charAt(i)) {
                text = text.substring(0, i + 1) + 'x' + text.substring(i + 1);
            }
        }
        return text;
    }

    private String[] Divid2Pairs(String new_string) {
        String Original = format(new_string);
        int size = Original.length();
        if (size % 2 != 0) {
            size++;
            Original = Original + 'x';
        }
        String x[] = new String[size / 2];
        int counter = 0;
        for (int i = 0; i < size / 2; i++)
        {
            x[i] = Original.substring(counter, counter + 2);
            counter = counter + 2;
        }
        return x;
    }

    public int[] GetDiminsions(char letter) {
        int[] key = new int[2];
        if (letter == 'j')
            letter = 'i';

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix_arr[i][j] == letter) {
                    key[0] = i;
                    key[1] = j;
                    break;
                }
            }
        }// takes worst case O(n^2) time.
        return key;
    }

    public String encryptMessage(String Source) {
        String src_arr[] = Divid2Pairs(Source);
        String Code = new String();
        char one, two;
        int part1[], part2[];

        for (int i = 0; i < src_arr.length; i++) {

            one = src_arr[i].charAt(0);
            two = src_arr[i].charAt(1);
            part1 = GetDiminsions(one);
            part2 = GetDiminsions(two);

            if (part1[0] == part2[0]) {// same row
                if (part1[1] < 4)
                    part1[1]++;
                else
                    part1[1] = 0;
                if (part2[1] < 4)
                    part2[1]++;
                else
                    part2[1] = 0;
            } else if (part1[1] == part2[1]) {// same col
                if (part1[0] < 4)
                    part1[0]++;
                else
                    part1[0] = 0;
                if (part2[0] < 4)
                    part2[0]++;
                else
                    part2[0] = 0;
            } else {// different row and different col
                int temp = part1[1];
                part1[1] = part2[1];
                part2[1] = temp;
            }
            Code = Code + matrix_arr[part1[0]][part1[1]]
                    + matrix_arr[part2[0]][part2[1]];
        }
        return Code;
    }

    public String decryptMessage(String cipher){
        String[] pairedCipher = Divid2Pairs(cipher);
        String plain = "";
        char one, two;
        int part1[], part2[];
        for(int i=0; i<pairedCipher.length; i++){
            one = pairedCipher[i].charAt(0);
            two = pairedCipher[i].charAt(1);
            part1 = GetDiminsions(one);
            part2 = GetDiminsions(two);
            if(part1[0] == part2[0]){// same row, change columns
                // immediate left
                part1[1] = (part1[1] > 0) ? part1[1] - 1 : 4;
                part2[1] = (part2[1] > 0) ? part2[1] - 1 : 4;

            } else if(part1[1] == part2[1]){// same column, change rows.
                // immediate top.
                part1[0] = (part1[0] > 0) ? part1[0] - 1 : 4;
                part2[0] = (part2[0] > 0) ? part2[0] - 1 : 4;
            }else{
                int temp = part1[1];
                part1[1] = part2[1];
                part2[1] = temp;
            }
            plain = plain +  matrix_arr[part1[0]][part1[1]] +
                    matrix_arr[part2[0]][part2[1]];
        }
        return plain;
    }

    public String process(String msg, boolean flag){// 1 denoting invalid string.
        if(msg.length() == 0)return "1";
        msg = msg.toLowerCase();
        String s = "";
        for(int i=0; i<msg.length(); i++){
            if(msg.charAt(i) >= 'a' &&  msg.charAt(i) <= 'z')
                s = s + msg.charAt(i);
            else if(msg.charAt(i) != ' '){
                return "1";
            }
        }
        return (s.length() %2 == 0 || flag)?s:(s.concat("z"));
    }

}