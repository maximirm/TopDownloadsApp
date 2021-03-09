package org.example.top10downloader;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: starting AsyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: done");

    }


    private class DownloadData {

        protected void execute(final String url) {
            Log.d(TAG, "doInBackground: starts with " + url);

            new Thread(() -> {
                final String rssFeed = downloadXML(url);
                runOnUiThread(() -> {
                    // send the data back
                    onPostExecute(rssFeed);
                });
            }).start();
        }

        protected void onPostExecute(String s) {


            Log.d(TAG, "onPostExecute: parameter is " + s);
        }

        private String downloadXML(String urlPath){

            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: the response code was " + response);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead <0){
                        break;
                    }
                    if(charsRead >0){
                        xmlResult.append(String.copyValueOf(inputBuffer,0,charsRead));
                    }
                }
                reader.close();

                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data:" + e.getMessage());
            } catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security Exception. Need permission? " + e.getMessage() );
//                e.printStackTrace();
            }
            return null;

        }

    }




//    private class DownloadData extends AsyncTask<String, Void, String> {
//
//        private static final String TAG = "DownloadData";
//
//        @Override
//        protected void onPostExecute(String s) {
//
//
//            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: parameter is " + s);
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            Log.d(TAG, "doInBackground: starts with: " + strings[0]);
//            return "doInBackground completed.";
//        }
//    }




}