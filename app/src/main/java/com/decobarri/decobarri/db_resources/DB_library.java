package com.decobarri.decobarri.db_resources;

import android.content.Context;
import android.os.StrictMode;

import com.decobarri.decobarri.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DB_library {
    private Context context;

    public DB_library(Context current){
        this.context = current;
    }


    public String db_call(String call){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //-----------------------------------------------------//
        //  Step 1:  Start creating a few objects we'll need.
        //-----------------------------------------------------//

        String output;
        URL uri;
        InputStream result = null;
        DataInputStream req;

        String responseResult = null;

        try {

            //------------------------------------------------------------//
            // Step 2:  Create the URL.                                   //
            //------------------------------------------------------------//
            // Note: Put your real URL here, or better yet, read it as a  //
            // command-line arg, or read it from a file.                  //
            //------------------------------------------------------------//

            uri = new URL(context.getResources().getString(R.string.db_URL)+call);

            //----------------------------------------------//
            // Step 3:  Open an input stream from the url.  //
            //----------------------------------------------//

            result = uri.openStream();         // throws an IOException

            //-------------------------------------------------------------//
            // Step 4:                                                     //
            //-------------------------------------------------------------//
            // Convert the InputStream to a buffered DataInputStream.      //
            // Buffering the stream makes the reading faster; the          //
            // readLine() method of the DataInputStream makes the reading  //
            // easier.                                                     //
            //-------------------------------------------------------------//

            req = new DataInputStream(new BufferedInputStream(result));

            //------------------------------------------------------------//
            // Step 5:                                                    //
            //------------------------------------------------------------//
            // Now just read each record of the input stream, and print   //
            // it out.  Note that it's assumed that this problem is run   //
            // from a command-line, not from an application or applet.    //
            //------------------------------------------------------------//

            while ((output = req.readLine()) != null) {
                //System.out.println(output);
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

            //---------------------------------//
            // Step 6:  Close the InputStream  //
            //---------------------------------//

            try {
                result.close();
            } catch (IOException ioe) {
                // just going to ignore this one
            }

        } // end of 'finally' clause

        if (responseResult == null) output = "Void answer =(";
        return responseResult;
    }
}
