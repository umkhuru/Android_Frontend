package com.example.ethon.car_service_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ethon.car_service_station.domain.JSONConverter;
import com.example.ethon.car_service_station.domain.Job;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class JobListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Job> jobList;
    private static final String PREFS_NAME="MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        new HttpASyncTask().execute("http://servicecenter-paulie.rhcloud.com/jobs");
    }

    private class HttpASyncTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params){
            return GET(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {
                try
                {
                    ArrayList<String> listData=new ArrayList<>();
                    JSONArray jsonArray=new JSONArray(s);
                    jobList=new ArrayList<>();

                    if(!jsonArray.isNull(0)&&jsonArray.length()!=0)
                    {
                        for(int x=0;x<jsonArray.length();x++)
                        {
                            listData.add(jsonArray.get(x).toString());
                        }

                        JSONObject jsonObject;

                        for(int x=0;x<listData.size();x++) {
                            jsonObject = new JSONObject(listData.get(x));
                            //Build that Job(s) and add onto the Job ArrayList
                            Job job = new Job
                                    .Builder(jsonObject.getString("jobDate"))
                                    .description(jsonObject.getString("description"))
                                    .id(jsonObject.getLong("id"))
                                    .build();

                            jobList.add(job);
                        }

                        MyAdapter adapter=new MyAdapter(JobListActivity.this,jobList);
                        listView=(ListView)findViewById(R.id.list);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener()
                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Sending items to the Job view...can't put it in the
                                        // onStop function because of rest api links

                                        Job job=(Job)listView.getItemAtPosition(position);
                                        JSONObject jobJSON= JSONConverter.convertJobToJSON(job);

                                        SharedPreferences settings=getSharedPreferences(PREFS_NAME,0);
                                        String staffObject=settings.getString("userObject","");

                                        SharedPreferences.Editor editor=settings.edit();
                                        editor.putString("jobItem",jobJSON.toString());
                                        editor.putString("userObject",staffObject);
                                        editor.apply();

                                        Intent i=new Intent(JobListActivity.this,JobActivity.class);
                                        JobListActivity.this.startActivity(i);
                                    }
                                }
                        );
                    }
                }

                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            else
                Toast.makeText(getBaseContext(), "Sorry an error occurred", Toast.LENGTH_LONG).show();
        }
    }

    public String GET(String url)
    {
        InputStream inputStream;
        String result="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();

            HttpResponse httpResponse=httpClient.execute(new HttpGet(url));

            inputStream=httpResponse.getEntity().getContent();

            if(inputStream!=null)
            {
                result=convertInputStreamToString(inputStream);
            }
            else
            {
                result="Did not work..";
            }
        }

        catch(Exception e)
        {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
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
