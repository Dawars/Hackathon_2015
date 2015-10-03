package me.dawars.hackathon2015;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends WearableActivity {
    private static final int SPEECH_REQUEST_CODE = 0;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        while (!((BaseApplication) getApplication()).myBlueComms.connect()) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySpeechRecognizer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((BaseApplication) getApplication()).myBlueComms.close();
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0).toLowerCase();
            Log.v(TAG, spokenText);
            Intent intent = new Intent(this, CardActivity.class);
            if (spokenText.contains("temperature")) {
                int temp = ((BaseApplication) getApplication()).myBlueComms.getDataI('t');
                intent.putExtra(CardActivity.TITLE, "Temperature");
                intent.putExtra(CardActivity.DESC, temp + " °C");
                startActivity(intent);
                finish();
            } else if (spokenText.contains("humidity")) {
                int temp = ((BaseApplication) getApplication()).myBlueComms.getDataI('h');
                intent.putExtra(CardActivity.TITLE, "Humidity");
                intent.putExtra(CardActivity.DESC, temp + " %");
                startActivity(intent);
                finish();
            } else if ((spokenText.contains("light") && spokenText.contains("level") || spokenText.contains("luminosity"))) {
                int temp = ((BaseApplication) getApplication()).myBlueComms.getDataI('p');
                intent.putExtra(CardActivity.TITLE, "Light level");
                intent.putExtra(CardActivity.DESC, temp);
                startActivity(intent);
                finish();
            } else if (spokenText.contains("lamp") || spokenText.contains("light") || spokenText.contains("led")) {
                if(spokenText.contains("to ")) {
                    String color = ColorSearch.search(spokenText.substring(spokenText.indexOf("to ") + 3));
                    if(color != null) {
                        ((BaseApplication) getApplication()).myBlueComms.setLed(color);
                    }
                } else if(spokenText.contains("turn")) {
                    ((BaseApplication) getApplication()).myBlueComms.turnLed(!spokenText.contains("off"));
                }
            } else if (spokenText.contains("light")) {
                float temp = ((BaseApplication) getApplication()).myBlueComms.getDataI('p');
                intent.putExtra(CardActivity.TITLE, "Light level");
                intent.putExtra(CardActivity.DESC, temp == 1 ? "Dark" : "Bright");
                startActivity(intent);
                finish();
            }

            if (spokenText.equals("exit"))
                exit();
        } else {
            exit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void exit() {
        Log.v(TAG, "exit()");
        ((BaseApplication) getApplication()).myBlueComms.close();
        finish();
    }
}
