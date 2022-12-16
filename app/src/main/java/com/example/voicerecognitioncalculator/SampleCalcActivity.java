package com.example.voicerecognitioncalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SampleCalcActivity extends AppCompatActivity {

    private int[] numbericButton = {R.id.btnZero,R.id.btnOne,R.id.btnTwo,R.id.btnThree,
            R.id.btnFour,R.id.btnFive,R.id.btnSix,R.id.btnSeven,R.id.btnEight,R.id.btnNine};

    //IDs of all the operators
    private int[] operatorsButton = {R.id.btnAdd,R.id.btnSubtract,R.id.btnMultiplay,
            R.id.btnDevide};

     TextView txtScreen;
    private boolean lastNumberic;
    private boolean stateError;
    private boolean lastDot;
     ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    Context context;

    TextView HistoryCalc;


    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_calc);


        reference = FirebaseDatabase.getInstance().getReference("Calculator");

        txtScreen = findViewById(R.id.txtScreen);
        btnSpeak = findViewById(R.id.btnSpeak);
        HistoryCalc=findViewById(R.id.HistoryCalc);

        HistoryCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SampleCalcActivity.this,HistoryCalcActivity.class));
            }
        });

        //find and set onClickListener to numeric button
        setNumericOnClickListener();

        //find and set onCLickListener to the operatos , equal button and decimal point button
        setOperationOnClickListener();

        pushData();
    }

    private void pushData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Calculator");

        String UniqueID = reference.getKey();

       // reference.child(UniqueID).setValue(txtScreen);
    }

    private void setNumericOnClickListener() {
        View.OnClickListener listener= new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //just append / set the text of clicked button
                Button button = (Button) v;
                if (stateError){
                    //if current state is Error , replace the error message
                    txtScreen.setText(button.getText());
                    stateError = false;
                }else {
                    txtScreen.append(button.getText());
                }
                //set the FLAG
                lastNumberic = true;
            }
        };

        //assign the listener to all the numeric button
        for (int id : numbericButton){
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperationOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the current state is Error donot append the operator
                //if last input is number only , append the operator
                if (lastNumberic && !stateError){
                    Button button = (Button) v;
                    txtScreen.append(button.getText());
                    lastNumberic=false;
                    lastDot = false;//reset the DOT FLAG
                }
            }
        };

        //assign the listener to all the operator button
        for (int id : operatorsButton){
            findViewById(id).setOnClickListener(listener);


//            String UniqueID = reference.getKey();
//
//            HashMap<Object,Object> hashMap = new HashMap<>();
//
//            hashMap.put("A",numbericButton);
//            hashMap.put("Operator",operatorsButton);
//            hashMap.put("B",numbericButton);
//            hashMap.put("Ans",txtScreen);
//
//
//            reference.child(UniqueID).setValue(hashMap);
        }
        //decimal point
        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumberic && !stateError && lastDot){
                     txtScreen.append(".");
                    lastNumberic = false;
                    lastDot = false;
                }
            }
        });

        //clear button
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText("");
                lastNumberic = false;
                stateError = false;
                lastDot = false;
            }
        });
        //equal button
        findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });

        //speak button
        findViewById(R.id.btnSpeak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateError){
                    txtScreen.setText("Try Again");
                    stateError = false;
                }else {
                    promptSpeechInput();
                }
                lastNumberic = true;
            }
        });
    }
    private void onEqual() {

        if (lastNumberic && !stateError){
            String txt = txtScreen.getText().toString();

            
            double result = 0;
            
            //create an expression
            try {
                Expression expression = null;
                try {
                    expression = new ExpressionBuilder(txt).build();
                     result = expression.evaluate();
                    txtScreen.setText(Double.toString(result));
                }catch (Exception e){
                    txtScreen.setText("Error");
                }
            }catch (ArithmeticException ex){
                txtScreen.setText("Error");
                stateError = true;
                lastNumberic = false;
            }

            String UniqueID = reference.push().getKey();
            HashMap<Object,Object> hashMap = new HashMap<>();
            hashMap.put("UniqueID",UniqueID);
            hashMap.put("Operation",txt+" = "+result);
         reference.child(UniqueID).setValue(hashMap);

        }
    }
    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }
    }


    //Receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if (resultCode == RESULT_OK && null != data){

                    ArrayList<String> result = data.
                             getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String change = result.get(0);

                    change = change.replace("x","*");
                    change = change.replace("X","*");
                    change = change.replace("add","+");
                    //  change = change.replace("Add","+");
                    change = change.replace("sub","-");
                    //   change = change.replace("Sub","-");
                    change = change.replace("to","2");
                    change = change.replace("plus","+");
                    //  change = change.replace("Plus","+");
                    change = change.replace("minus","-");
                    //  change = change.replace("Minus","-");
                    change = change.replace("times","*");
                    change = change.replace("into","*");
                    //   change = change.replace("Into","*");
                    change = change.replace("in2","*");
                    //    change = change.replace("In2","*");
                    change = change.replace("multiply by","*");
                    //  change = change.replace("Multiply by","*");
                    change = change.replace("divide by","/");
                    change = change.replace("divide","*");
                    change = change.replace("equal","=");
                    change = change.replace("Equals","=");

                    if (change.contains("=")){
                        change = change.replace("=","");
                        txtScreen.setText(change);
                        onEqual();
                    }else {
                        txtScreen.setText(change);
                    }
                }
                break;
            }

        }
    }
}