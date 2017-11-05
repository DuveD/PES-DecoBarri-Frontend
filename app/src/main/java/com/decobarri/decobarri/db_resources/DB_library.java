package com.decobarri.decobarri.db_resources;

import android.content.Context;
import android.os.StrictMode;

import com.decobarri.decobarri.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class DB_library {
    private Context context;

    public DB_library(Context current){
        this.context = current;
    }


    public String db_call(String call, String param, String method){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String output;
        URL uri;
        HttpURLConnection connection = null;
        int responseCode;

        String responseResult = null;

        try {
            if(Objects.equals(method, "POST") || Objects.equals(method, "PUT")) {
                uri = new URL(context.getResources().getString(R.string.db_URL)+call);
                connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestMethod(method);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(param);
                out.close();
            }
            else {
                uri = new URL(context.getResources().getString(R.string.db_URL)+call + param);
                connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestProperty("Accept-Charset", "UTF-8");
            }

            responseCode = connection.getResponseCode();

            System.out.println("Call: " + uri);
            System.out.println("Param: " + param);
            System.out.println("Code: " + responseCode);

            if(responseCode!=200) {
                connection.disconnect();
                return String.valueOf(responseCode);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((output = reader.readLine()) != null) {
                System.out.println(output);
                responseResult = output;
            }

        } catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
            System.exit(1);

        } catch (IOException ioe) {

            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);

        } finally {
            assert connection != null;
            connection.disconnect();

        } // end of 'finally' clause

        //if (responseResult == null) responseResult = "Void answer =(";
        return responseResult;
    }
}
