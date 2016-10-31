package com.example.ethon.car_service_station;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ethon.car_service_station.domain.JSONConverter;
import com.example.ethon.car_service_station.domain.PasswordEncryptor;
import com.example.ethon.car_service_station.domain.Staff;

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

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME="MyPrefsFile";
    private View oldView;
    private EditText usrName;
    private EditText password;
    Button loginBtn;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usrName=(EditText)findViewById(R.id.userEmail);
        password=(EditText)findViewById(R.id.userPassword);

        loginBtn=(Button)findViewById(R.id.login_btn);
        registerBtn=(Button)findViewById(R.id.reg_btn);

        registerBtn.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Intent i=new Intent(v.getContext(),RegisterActivity.class);
                        LoginActivity.this.startActivity(i);
                    }
                }
        );

        loginBtn.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        String msg=validate();
                        if(!msg.equals(""))
                        {
                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //Validate the User etc....
                            String mail=usrName.getText().toString();
                            oldView=v;
                            new HttpASyncTask().execute("http://servicecenter-paulie.rhcloud.com/staff/search/" + mail);
                        }
                    }
                }
        );
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
            Log.d("InputStream",e.getLocalizedMessage());
        }
        return result;
    }

    private class HttpASyncTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params){
            return GET(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                if(s!=null&&!s.equals("")&&!s.isEmpty())
                {
                    ArrayList<String> listData=new ArrayList<>();
                    JSONArray jsonArray=new JSONArray(s);

                    if(!jsonArray.isNull(0))
                    {
                        for(int x=0;x<jsonArray.length();x++)
                        {
                            listData.add(jsonArray.get(x).toString());
                        }

                        JSONObject jsonObject=new JSONObject(listData.get(0));
                        Staff staff=JSONConverter.convertJSONtoStaff(jsonObject);


                        if(staff.getPassword()!=null)
                        {
                            String md5_password;
                            md5_password= PasswordEncryptor.convertPasswordToMD5(password.getText().toString());
                            if(staff.getPassword().equals(md5_password))
                            {
                                SharedPreferences settings=getSharedPreferences(PREFS_NAME,0);
                                SharedPreferences.Editor editor=settings.edit();

                                JSONObject staffJSON= JSONConverter.convertStaffToJSON(staff);
                                editor.putString("userObject",staffJSON.toString());
                                editor.apply();

                                String msg="Welcome to Service Center\n";
                                msg+=staff.getFirstName()+" "+staff.getLastName()+"!!";
                                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

                                Intent i=new Intent(oldView.getContext(),DashBoard.class);
                                LoginActivity.this.startActivity(i);
                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(),"WRONG\nPASSWORD.",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Credentials.\nUser not found.",Toast.LENGTH_LONG).show();
                }

                usrName.setText("");
                password.setText("");
            }

            catch(Exception ex)
            {
                ex.printStackTrace();
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

    public String validate()
    {
        String name=usrName.getText().toString();
        String pass=password.getText().toString();
        String errMsg="";

        if(name.equals("")||name.isEmpty())
        {
            errMsg+=" e-mail,";
        }

        else if(pass.equals("")||pass.isEmpty())
        {
            errMsg+=" password,";
        }

        if(!errMsg.equals(""))
        {
            errMsg+="\n Required!!";
        }

        return errMsg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
