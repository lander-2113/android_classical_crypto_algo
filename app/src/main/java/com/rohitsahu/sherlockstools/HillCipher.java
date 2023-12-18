package com.rohitsahu.sherlockstools;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class HillCipher extends AppCompatActivity {

    RadioGroup radioGroup;
    private String m;
    private String k;
    private String corp;
    private String korinv;
    private String checkMessage;
    private EditText hillCiphereditfield;
    private EditText hillKeyeditfield;
    private TextView hillmessageview;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hill_cipher);
        corp = "";
        korinv = "";
        m = "";
        k = "";
        checkMessage = "";
        radioGroup = findViewById(R.id.hillradio);
        hillmessageview = findViewById(R.id.hillmessageview);
        hillCiphereditfield = findViewById(R.id.hilleditmessage);
        hillKeyeditfield = findViewById(R.id.hilleditkey);
        flag = false;
    }
        int[][] keymatrix;
        int[] linematrix;
        int[] resultmatrix;

        public void divide(String temp, int s) {
            while (temp.length() > s) {
                String sub = temp.substring(0, s);
                temp = temp.substring(s, temp.length());
                perform(sub);
            }
            if (temp.length() == s)
                perform(temp);
            else if (temp.length() < s) {
                for (int i = temp.length(); i < s; i++)
                    temp = temp + 'x';
                perform(temp);
            }
        }

        public void perform(String line) {
            linetomatrix(line);
            linemultiplykey(line.length());
            result(line.length());
        }

        public void keytomatrix(String key, int len) {
            keymatrix = new int[len][len];
            int c = 0;
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    keymatrix[i][j] = ((int) key.charAt(c)) - 97;
                    c++;
                }
            }
        }

        public void linetomatrix(String line) {
            linematrix = new int[line.length()];
            for (int i = 0; i < line.length(); i++) {
                linematrix[i] = ((int) line.charAt(i)) - 97;
            }
        }

        public void linemultiplykey(int len) {
            resultmatrix = new int[len];
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    resultmatrix[i] += keymatrix[i][j] * linematrix[j];
                }
                resultmatrix[i] %= 26;
            }
        }

        public void result(int len) {
            String result = "";
            for (int i = 0; i < len; i++) {
                result += (char) (resultmatrix[i] + 97);
            }
            //System.out.print(result);
            corp += result;
        }

        public boolean check(String key, int len) {
            keytomatrix(key, len);
            try{
                int d = determinant(keymatrix, len);
                d = d % 26;
                if (d == 0) {
                    throw new RuntimeException("Invalid key!!! Key is not invertible because determinant=0...");
                } else if (d % 2 == 0 || d % 13 == 0) {
                    throw new RuntimeException("Invalid key!!! Key is not invertible because determinant has common factor with 26...");
                }
            }catch(Exception e){
                checkMessage = e.getMessage();
                return false;
            }

            return true;

        }

        public int determinant(int A[][], int N) {
            int res;
            if (N == 1)
                res = A[0][0];
            else if (N == 2) {
                res = A[0][0] * A[1][1] - A[1][0] * A[0][1];
            } else {
                res = 0;
                for (int j1 = 0; j1 < N; j1++) {
                    int m[][] = new int[N - 1][N - 1];
                    for (int i = 1; i < N; i++) {
                        int j2 = 0;
                        for (int j = 0; j < N; j++) {
                            if (j == j1)
                                continue;
                            m[i - 1][j2] = A[i][j];
                            j2++;
                        }
                    }
                    res += Math.pow(-1.0, 1.0 + j1 + 1.0) * A[0][j1]
                            * determinant(m, N - 1);
                }
            }
            return res;
        }
        public void cofact(int num[][], int f) {
            int b[][], fac[][];
            b = new int[f][f];
            fac = new int[f][f];
            int p, q, m, n, i, j;
            for (q = 0; q < f; q++) {
                for (p = 0; p < f; p++) {
                    m = 0;
                    n = 0;
                    for (i = 0; i < f; i++) {
                        for (j = 0; j < f; j++) {
                            b[i][j] = 0;
                            if (i != q && j != p) {
                                b[m][n] = num[i][j];
                                if (n < (f - 2))
                                    n++;
                                else {
                                    n = 0;
                                    m++;
                                }
                            }
                        }
                    }
                    fac[q][p] = (int) Math.pow(-1, q + p) * determinant(b, f - 1);
                }
            }
            trans(fac, f);
        }
        void trans(int fac[][], int r) {
            int i, j;
            int b[][], inv[][];
            b = new int[r][r];
            inv = new int[r][r];
            int d = determinant(keymatrix, r);
            int mi = mi(d % 26);
            mi %= 26;
            if (mi < 0)
                mi += 26;
            for (i = 0; i < r; i++) {
                for (j = 0; j < r; j++) {
                    b[i][j] = fac[j][i];
                }
            }
            for (i = 0; i < r; i++) {
                for (j = 0; j < r; j++) {
                    inv[i][j] = b[i][j] % 26;
                    if (inv[i][j] < 0)
                        inv[i][j] += 26;
                    inv[i][j] *= mi;
                    inv[i][j] %= 26;
                }
            }
            // System.out.println("\nInverse key:");
            matrixtoinvkey(inv, r);
        }

        public int mi(int d) {
            int q, r1, r2, r, t1, t2, t;
            r1 = 26;
            r2 = d;
            t1 = 0;
            t2 = 1;
            while (r1 != 1 && r2 != 0) {
                q = r1 / r2;
                r = r1 % r2;
                t = t1 - (t2 * q);
                r1 = r2;
                r2 = r;
                t1 = t2;
                t2 = t;
            }
            return (t1 + t2);
        }

        public void matrixtoinvkey(int inv[][], int n) {
            String invkey = "";
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    invkey += (char) (inv[i][j] + 97);
                }
            }
            //System.out.print(invkey);
            //res[1] = invkey;
            korinv = invkey;
        }



    public boolean process(String key, String message){
        this.k = ""; this.m = "";
        key = key.toLowerCase(); message = message.toLowerCase();
        try{
            // key processing
            if(key.length() != 9)throw new RuntimeException("Invalid key length, must be 9");
            for(int i =0; i<9; i++){
                if(key.charAt(i)>= 'a' && key.charAt(i)<='z'){
                    this.k = this.k + key.charAt(i);
                }else{
                    throw new RuntimeException("Unrecognized character.");
                }
            }
            // message processing
            if(message.length() == 0)throw new RuntimeException("Message length invalid");
            for(int i=0; i<message.length(); i++){
                if(message.charAt(i) >= 'a' && message.charAt(i) <= 'z'){
                    this.m = this.m + message.charAt(i);
                }else if(message.charAt(i) == ' ' || message.charAt(i) == '\n'){

                }else{
                    throw new RuntimeException("Invalid message, contains other than [a-zA-Z, ' ', '\\n']");
                }
            }
            switch (this.m.length()%3){
                case 1:
                    this.m += "xx";
                    break;
                case 2:
                    this.m += "x";
                    break;
            }
        }catch(Exception e){
            checkMessage = e.getMessage();
            return false;
        }
        return true;
    }

    public void hillOK(View view){
            k = "";
            m = "";

             flag = process(hillKeyeditfield.getText().toString(),
                    hillCiphereditfield.getText().toString());
            if(flag && check(k, 3)){
                hillmessageview.setText("Key and message: OK\nNow select 'Encrypt' or 'Decrypt'.");
            }else{
                flag = false;
                hillmessageview.setText(checkMessage);
            }
    }

    public void onRadioButtonClicked(View view) {

        int id = radioGroup.getCheckedRadioButtonId();
        corp = "";
        korinv = "";
        divide(m, 3);
        cofact(keymatrix, 3);
        String res = "";
        if(!flag)return;
        switch(id) {
            case R.id.hillenc:
                res = "Plain Text: " + corp + "\n" + "Inverse key: " + korinv;
                hillmessageview.setText(res);
                break;
            case R.id.hilldec:
                res = "Plain Text: " + corp + "\n" + "Key: " + korinv;
                hillmessageview.setText(res);
                break;
        }
        flag = false;
    }

}

