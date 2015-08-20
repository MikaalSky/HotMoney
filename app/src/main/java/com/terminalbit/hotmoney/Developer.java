package com.terminalbit.hotmoney;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Developer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Developer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Developer extends Fragment {
    File appDirectory;
    int Moneys;
    int balance;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_developer,container,false);
        appDirectory = getActivity().getApplicationContext().getFilesDir();
        //int Moneys = generator.nextInt(balance + 1);
        try {
            Moneys = readJSON("data.json").getInt("hotmoney_count");
            balance = readJSON("data.json").getInt("total_balance");
            Log.i("It's a-okay in json land", Moneys + " | " + balance);
        }catch(JSONException e){
            Moneys = 0;
            balance = 0;
            Log.i("note", "Failed to get the stuff. We should write defaults to file.");
        }
        /*int Temperature = (int) (((((double)Moneys)/((double)balance))*(212-32)) + 32);
        if(Temperature == 0){
            Temperature = 32;
        }*/
        final EditText Balance = (EditText) v.findViewById(R.id.balance);
        final SeekBar Ratio = (SeekBar) v.findViewById(R.id.ratio);
        final TextView txt = (TextView) v.findViewById(R.id.stats);
        Balance.setText(Integer.toString(balance));
        Ratio.setMax(balance);
        Ratio.setProgress(Moneys);
        Ratio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Moneys = progress;
                txt.setText("Balance: " + balance + " | Hot: " + Moneys);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });
        Balance.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (Balance.getText().toString().equals("")) {
                    balance = 0;
                } else {
                    balance = Integer.parseInt(Balance.getText().toString());
                }
                Ratio.setMax(balance);
                txt.setText("Balance: " + balance + " | Hot: " + Moneys);
                return false;
            }
        });
        txt.setText("Balance: " + balance + " | Hot: " + Moneys);
        Button save = (Button) v.findViewById(R.id.saver);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonPress();
            }
        });
        return v;
    }
    public void onSaveButtonPress() {
        //SAVE THE STUFF!
        String example = "{\"hotmoney_count\":0,\"total_balance\":0}";
        writeJSON("data.json","{\"hotmoney_count\":" + Moneys + ",\"total_balance\":" + balance + "}");
        Toast.makeText(getActivity().getApplicationContext(), "Saved!!!1!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
