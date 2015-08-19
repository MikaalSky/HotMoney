package com.terminalbit.hotmoney;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import at.grabner.circleprogress.CircleProgressView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Balance.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Balance#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Balance extends Fragment {
    public int balance;
    File appDirectory;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_balance,container,false);
        CircleProgressView Circle = (CircleProgressView) v.findViewById(R.id.fills);
        appDirectory = getActivity().getApplicationContext().getFilesDir();
        //int Moneys = generator.nextInt(balance + 1);
        int Moneys;
        try {
            Moneys = readJSON("data.json").getInt("hotmoney_count");
            balance = readJSON("data.json").getInt("total_balance");
            Log.i("It's a-okay in json land", Moneys + " | " + balance);
        }catch(JSONException e){
            Moneys = 0;
            balance = 0;
            Log.i("note","Failed to get the stuff. :(");
            e.printStackTrace();
        }
        Circle.setValue(Moneys);
        int Temperature = (int) (((((double)Moneys)/((double)balance))*(212-32)) + 32);
        if(Temperature == 0){
            Temperature = 32;
        }
        Circle.setText(Temperature + "°");
        //212 to 32
        Circle.setMaxValue((float) balance);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("[detached]","got detached");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("[destroy]", "got destroy");
    }
    public JSONObject readJSON(String filename){
        File jsonFile = new File(appDirectory, filename);
        StringBuilder jsonTxt = new StringBuilder();
        Gson gson = new Gson();
        try{
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            String line;
            while((line = br.readLine()) != null){
                jsonTxt.append(line);
            }
            br.close();
        }catch(IOException e){
            //handle errors :)
            e.printStackTrace();
            Log.i("info", "WOAH, THE FILE's DEAD! Let's make a file. :P");
            String example = "{\"hotmoney_count\":0,\"total_balance\":0}";
            writeJSON("data.json",example);
            return gson.fromJson(example,JSONObject.class);
        }
        String jsonText = jsonTxt.toString();
        //Log.i("ghf",jsonText);
        JSONObject test = null;
        try {
            test = new JSONObject(jsonText);
        }catch(Exception e) {
            try {
                test = new JSONObject("{}");
            }catch(Exception E){}
        }
        return test;
    }
    public boolean writeJSON(String filename,String data){
        File writeing = new File(appDirectory, filename);
        FileOutputStream output;
        try{
            output = getActivity().getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            output.write(data.getBytes());
            output.close();
            Log.i("info","We did it, party! Wrote to le file!!!1!");
        }catch(Exception e){
            Log.i("info","WTF! Failed to write?!");
            return false;
        }
        return true;
    }
}
