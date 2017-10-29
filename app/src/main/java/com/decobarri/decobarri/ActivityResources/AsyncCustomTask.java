package com.decobarri.decobarri.ActivityResources;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncCustomTask extends AsyncTask<Void, Void, Void> {
    private Context mCon;

    protected AsyncCustomTask(Context con) {
        mCon = con;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            this.doInBackgroundFunction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute( Void nope ){
        try {
            this.onPostExecuteFunction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(mCon, "Finished complex background doInBackgroundFunction!", Toast.LENGTH_LONG).show();
    }

    public void doInBackgroundFunction() {

    }


    public void onPostExecuteFunction() {

    }
}
