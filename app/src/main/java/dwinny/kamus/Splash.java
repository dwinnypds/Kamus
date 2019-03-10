package dwinny.kamus;

import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dwinny.kamus.DB.Kamus_Helper;
import dwinny.kamus.DB.Kata;
import dwinny.kamus.preference.AppPref;

public class Splash extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progress_bar);
        new LoadData().execute();
    }

    public ArrayList<Kata> preLoadRaw(int data) {
        ArrayList<Kata> kataArrayList = new ArrayList<>();
        BufferedReader reader;
        String line = null;
        try {
            Resources res = getResources();
            InputStream rawDict = res.openRawResource(data);

            reader = new BufferedReader(new InputStreamReader(rawDict));

            do {
                line = reader.readLine();
                Log.e("LINE", line);
                String[] splitStr = line.split("\t");
                Kata kata;
                kata = new Kata(splitStr[0], splitStr[1]);
                kataArrayList.add(kata);

            } while (line != null);


        } catch (Exception e) {
            e.printStackTrace();
             Log.e("Error 2", e.getMessage());
        }
        return kataArrayList;
    }


    private class LoadData extends AsyncTask<Void, Integer, Void> {
        Kamus_Helper kamus_helper;
        double progress;
        double maxProgress = 100;
        AppPref appPref;

        @Override
        protected void onPreExecute() {

            kamus_helper = new Kamus_Helper(Splash.this);
            appPref = new AppPref(Splash.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean firstRun = appPref.getFirstRun();
            if (firstRun){
                ArrayList<Kata> indo = preLoadRaw(R.raw.indonesia_english);

                Log.e("INDO", indo.toString());
                ArrayList<Kata> english = preLoadRaw(R.raw.english_indonesia);
                publishProgress((int)progress);

                try {
                    kamus_helper.open();
                } catch (SQLException e){
                    e.printStackTrace();
                }

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (english.size()
                        + indo.size()
                );

                kamus_helper.insertTransaction(english, true);
                kamus_helper.insertTransaction(indo, false);

                progress += progressDiff;
                publishProgress((int)progress);


                kamus_helper.close();
                appPref.setFirstRun(false);
                publishProgress((int)maxProgress);

            }else {
                try {
                    synchronized (this){
                        this.wait(1000);
                        publishProgress(50);

                        this.wait(300);
                        publishProgress((int)maxProgress);
                    }
                }catch (Exception e){e.printStackTrace();
                    Log.e("Error", e.getMessage());}

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }


    }

}
