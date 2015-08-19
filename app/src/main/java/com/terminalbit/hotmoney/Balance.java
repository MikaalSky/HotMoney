package com.terminalbit.hotmoney;

import android.app.Activity;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_balance,container,false);
        CircleProgressView Circle = (CircleProgressView) v.findViewById(R.id.fills);
        //int Moneys = generator.nextInt(balance + 1);
        int Moneys;
        //try {
            //Moneys = readJSON("data.json").getInt("hotmoney_count");
            //balance = readJSON("data.json").getInt("total_balance");
        //}catch(JSONException e){
            Moneys = 0;
            balance = 0;
        //}
        Circle.setValue(Moneys);
        int Temperature = (int) (((((double)Moneys)/((double)balance))*(212-32)) + 32);
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

    File appDirectory = Environment.getDataDirectory();
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
            return gson.fromJson("{}",JSONObject.class);
        }
        String jsonText = jsonTxt.toString();
        JSONObject test = gson.fromJson(jsonText, JSONObject.class);
        jsonFile.deleteOnExit();
        return test;
    }
}
