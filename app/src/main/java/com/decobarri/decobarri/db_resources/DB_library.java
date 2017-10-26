package com.decobarri.decobarri.db_resources;

import android.content.Context;
import android.os.StrictMode;

import com.decobarri.decobarri.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;

public class DB_library {
    private Context context;

    public DB_library(Context current){
        this.context = current;
    }


    public String db_call(String call, String param, String method){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //-----------------------------------------------------//
        //  Step 1:  Start creating a few objects we'll need.  //
        //-----------------------------------------------------//

        String output;
        URL uri;
        InputStream result = null;
        HttpURLConnection connection = null;

        String responseResult = null;

        try {

            //------------------------------------------------------------//
            // Step 2:  Create the URL.                                   //
            //------------------------------------------------------------//
            // Note: Put your real URL here, or better yet, read it as a  //
            // command-line arg, or read it from a file.                  //
            //------------------------------------------------------------//



            if(Objects.equals(method, "POST") || Objects.equals(method, "PUT")) {

                uri = new URL(context.getResources().getString(R.string.db_URL)+call);

                connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestMethod(method);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(param);
                out.close();

                int responseCode = connection.getResponseCode();
                if(responseCode!=200) return String.valueOf(responseCode);
                System.out.println("Sending 'POST' request to URL : " + uri);
                System.out.println("Post parameters : " + param);
                System.out.println("Response Code : " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((output = reader.readLine()) != null) {
                    System.out.println(output);
                    responseResult = output;
                }
            }
            else {
                uri = new URL(context.getResources().getString(R.string.db_URL)+call + param);
                connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                int responseCode = connection.getResponseCode();
                result = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(result));
                System.out.println("Sending 'GET' request to URL : " + uri);
                System.out.println("Post parameters : " + param);
                System.out.println("Response Code : " + responseCode);
                while ((output = reader.readLine()) != null) {
                    System.out.println(output);
                    responseResult = output;
                }
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
