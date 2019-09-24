package com.client.vpman.mapkakaam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;
import java.util.Locale;

public class ChatBot extends AppCompatActivity {

    MaterialButton materialButton;
    private final int REQ_CODE = 100;
    MaterialTextView materialTextView,materialTextView1;
    private TextToSpeech textToSpeech;
    Translate translate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        materialButton=findViewById(R.id.speak);
        materialTextView1=findViewById(R.id.SpeakGetText);
        translate = TranslateOptions.getDefaultInstance().getService();

        materialTextView=findViewById(R.id.Speakresponse);
     /*   Typeface hindiFont = Typeface.createFromAsset(getAssets(), "fonts/hindi.TTF");
        materialTextView.setTypeface(hindiFont);*/
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


String data= String.valueOf(materialTextView1.getText());

TranslateData translateData=new TranslateData();
translateData.execute(data);



       /* new AsyncTask<String,Void,String>()
        {
            @Override
            protected String doInBackground(String... strings) {

                Translation translation =
                        translate.translate(
                                data,
                                Translate.TranslateOption.sourceLanguage("en"),
                                Translate.TranslateOption.targetLanguage("ru"));
                return translation.getTranslatedText();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                materialTextView.setText(s);
            }
        };*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
/*
                        Detection detection = translate.detect(String.valueOf(materialTextView1.getText()));
                        String detectedLanguage = detection.getLanguage();*/


                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    materialTextView1.setText((CharSequence) result.get(0));


                }
                break;
            }
        }
    }

    class TranslateData extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... params) {

            String text = params[0]; //text to translate
            Translate translate = TranslateOptions.getDefaultInstance().getService();
            Translation translation =
                    translate.translate(
                            text,
                            Translate.TranslateOption.sourceLanguage("en"),
                            Translate.TranslateOption.targetLanguage("ru"));



            return translation.getTranslatedText();
        }

        //this method will run after doInBackground execution
        protected void onPostExecute(String result) {
            materialTextView.setText(result);
        }


    }
}
