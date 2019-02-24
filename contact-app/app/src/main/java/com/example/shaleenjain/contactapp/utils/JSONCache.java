package com.example.shaleenjain.contactapp.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class JSONCache {
    private static String cacheFileName = "sentMessageText.txt";

    Context context;

    public JSONCache(Context context) {
        this.context = context;
    }

    public JSONArray readJSONFromFile() {
        JSONArray jarray = new JSONArray();
        String data;
        try {
            InputStream is = context.openFileInput(cacheFileName);
            if ( is != null ) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String receivedString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receivedString = br.readLine()) != null ) {
                    stringBuilder.append(receivedString);
                }

                is.close();
                data = stringBuilder.toString();

                Log.d("JSONCache", data);
                jarray = new JSONArray(data);
            }else{
                Log.e("JSONCache", "No data or file found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("JSONCache", "exception: " + e.toString());
        }

        return jarray;
    }

    public void writeAppendToJSONFile(JSONObject jo)
    {
        JSONArray jarray = readJSONFromFile();
        jarray.put(jo);
        String data = jarray.toString();
        try {
            OutputStreamWriter osw = new OutputStreamWriter(
                    context.openFileOutput(cacheFileName, Context.MODE_PRIVATE));
            osw.write(data);
            osw.close();
        }
        catch (IOException e) {
            Log.e("JSONCache", "File write failed: " + e.toString());
        }
        Log.e("JSONCache","cache file content after write" + data);
    }
}
