package com.decobarri.decobarri.ActivityResources;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class AsyncCustomTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    private String onPostMessage;

    protected AsyncCustomTask(Context context) {
        this.context = context;
        this.onPostMessage = null;

    }

    protected AsyncCustomTask(Context context, String message) {
        this.context = context;
        this.onPostMessage = message;

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
        if (onPostMessage != null) Toast.makeText(context, onPostMessage, Toast.LENGTH_LONG).show();
    }

    public abstract void doInBackgroundFunction();


    public abstract void onPostExecuteFunction();
}
