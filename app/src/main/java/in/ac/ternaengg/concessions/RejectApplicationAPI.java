package in.ac.ternaengg.concessions;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class RejectApplicationAPI extends AsyncTask<String, String,String> {

    public static  final String api_call_url = "http://192.168.43.15:27018/rejectApplication";
    OutputStream out = null;
    BufferedReader reader = null;
    int position;

    interface  RejectApplicationAPIListener {
        void gotRejectApplicationResponse(String o,int pos);
    }

    private RejectApplicationAPIListener listener;

    public void setListener(RejectApplicationAPIListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        if(listener!=null)
        {
            listener.gotRejectApplicationResponse(o,position);
        }
    }


    public void start(String applicant_id,int position){
        try {
            Log.d("RejectApplicationAPI","[AcceptApplicationAPI] Applicant ID - "+applicant_id);
            JSONObject postDict = new JSONObject();
            this.position = position;
            postDict.put("applicant_id",applicant_id);
            if(postDict.length()>0)
            {
                Log.d("AcceptApplicationAPI",postDict.toString());
                execute(postDict.toString());
            }
        } catch (JSONException e) {
            Log.d("RejectApplicationAPI","Failed To Put ApplicantID in Dictionary");
            Log.d("RejectApplicationAPI",""+e.toString());

        }

    }


    @Override
    protected String doInBackground(String... strings) {
        Log.d("RejectApplicationAPI","INSIDE DO IN BACKGROUND");
        String JSONResponse = null;
        String JSONData = strings[0];

        try{
            URL url = new URL(api_call_url);
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("Accept","application/json");

            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            writer.write(JSONData);
            writer.close();
            InputStream inputStream =urlConnection.getInputStream();
            StringBuffer buffer =new StringBuffer();
            if(inputStream==null)
            {
                return  "failed";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return "failed";
            }
            JSONResponse = buffer.toString();
            Log.i("AcceptApplicationAPI","RESPONSE DATA - "+JSONResponse.toString());
            return JSONResponse;
        }catch (Exception e)
        {
            Log.d("RejectApplicationAPI","[FAILED TO MAKE API CALL]");
            Log.d("RejectApplicationAPI",e.toString());
        }
        return "failed";
    }
}
