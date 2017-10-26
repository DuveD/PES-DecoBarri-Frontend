package com.decobarri.decobarri.ActivityResources;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.decobarri.decobarri.MainMenu;

public class UpdateTask extends AsyncTask<Void, Void, Void> {
    private Context mCon;

    public UpdateTask(Context con){
        mCon = con;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try{
            Thread.sleep(4000);
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } {
            return null;
        }
    }

    @Override
    protected void onPostExecute( Void nope ){
        Toast.makeText(mCon, "Finished complex background function!", Toast.LENGTH_LONG).show();

        //((MainMenu) mCon).resetUpdatong();
    }
}
