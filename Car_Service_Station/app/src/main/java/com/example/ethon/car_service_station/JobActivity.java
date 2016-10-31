package com.example.ethon.car_service_station;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethon.car_service_station.domain.JSONConverter;
import com.example.ethon.car_service_station.domain.Job;
import com.example.ethon.car_service_station.domain.Staff;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;




public class JobActivity extends AppCompatActivity {

    private Staff staff;
    Job theJob;
    String userString;
    String jobString;
    private static final String PREFS_NAME="MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        Button takeJob=(Button)findViewById(R.id.takeJobBtn);

        try
        {
            //Read the stuff from the SharedPrefs...
            SharedPreferences settings=getSharedPreferences(PREFS_NAME, 0);
            userString=settings.getString("userObject", "");
            jobString=settings.getString("jobItem","");
            staff= JSONConverter.convertJSONtoStaff(new JSONObject(userString));
            theJob=JSONConverter.convertJSONtoJob(new JSONObject(jobString));

        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        TextView jobDesc=(TextView)findViewById(R.id.jobLabel);
        TextView jobDate=(TextView)findViewById(R.id.jobDate);

        jobDesc.setText(theJob.getDescription());
        jobDate.setText(theJob.getJobDate());

        takeJob.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        new HttpASyncTask().execute("http://servicecenter-paulie.rhcloud.com/staff/"+
                                staff.getId()+"/update");
                    }
                }
        );
    }

    public String PUT(String url)
    {
        InputStream inputStream;
        String result="";

        try
        {
            HttpClient httpClient= new DefaultHttpClient();

            HttpPut httpPut=new HttpPut(url);
            String json;

            //update that staff member's JobList..
            List<Job> jobList=new ArrayList<>();
            jobList.add(theJob);

            Staff newStaff=new Staff
                    .Builder(staff.getLastName())
                    .copy(staff)
                    .jobs(jobList)
                    .build();

            json= JSONConverter.convertStaffToJSON(newStaff).toString();
            StringEntity se=new StringEntity(json);

            httpPut.setEntity(se);

            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");

            HttpResponse httpResponse=httpClient.execute(httpPut);
            inputStream=httpResponse.getEntity().getContent();

            if(inputStream!=null)
            {
                result=convertInputStreamToString(inputStream);
            }

            else
                result="Error Occurred!!";
        }

        catch(Exception ex)
        {
            Log.d("InputStream",ex.getLocalizedMessage());
        }
        return result;
    }


    private class HttpASyncTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params){
            return PUT(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {
                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result="";
        while((line=bufferedReader.readLine())!=null)
        {
            result+=line;
        }

        inputStream.close();
        return result;
    }
}
