package com.example.ethon.car_service_station;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ethon.car_service_station.domain.ContactDetails;
import com.example.ethon.car_service_station.domain.JSONConverter;
import com.example.ethon.car_service_station.domain.PasswordEncryptor;
import com.example.ethon.car_service_station.domain.Staff;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class RegisterActivity extends AppCompatActivity {
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register=(Button)findViewById(R.id.register);

        register.setOnClickListener(
                new Button.OnClickListener()
                {
                    public void onClick(View v) {
                        String msg=getErrorMsg();
                        if(!msg.equals(""))
                        {
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        }

                        else
                        {
                            //Register this assistant....
                            new HttpASyncTask().execute("http://servicecenter-paulie.rhcloud.com/staff/create");
                            view=v;
                        }
                    }
                }
        );

    }

    public Staff buildStaff() throws Exception {
        EditText nameValue=(EditText)findViewById(R.id.nameValue);
        EditText surnameValue=(EditText)findViewById(R.id.surnameValue);
        EditText ageValue=(EditText)findViewById(R.id.ageValue);
        EditText addressValue=(EditText)findViewById(R.id.addressValue);
        EditText telValue=(EditText)findViewById(R.id.telValue);
        EditText passValue=(EditText)findViewById(R.id.passValue);
        //EditText passConfirmValue=(EditText)findViewById(R.id.passConfirmValue);
        EditText emailValue=(EditText)findViewById(R.id.mailValue);

        String name=nameValue.getText().toString();
        String surname=surnameValue.getText().toString();
        String age=ageValue.getText().toString();
        String address=addressValue.getText().toString();
        String tel=telValue.getText().toString();
        String password= PasswordEncryptor.convertPasswordToMD5(passValue.getText().toString());
        //String passConfirm=passConfirmValue.getText().toString();
        String email=emailValue.getText().toString();

        ContactDetails contact=new ContactDetails
                .Builder(tel)
                .address(address)
                .build();

        Staff staff=new Staff
                .Builder(surname)
                .firstName(name)
                .age(Integer.valueOf(age))
                .eMail(email)
                .password(password)
                .address(contact)
                .build();
        return staff;
    }

    public String getErrorMsg()
    {
        String errMsg="";
        EditText nameValue=(EditText)findViewById(R.id.nameValue);
        EditText surnameValue=(EditText)findViewById(R.id.surnameValue);
        EditText ageValue=(EditText)findViewById(R.id.ageValue);
        EditText addressValue=(EditText)findViewById(R.id.addressValue);
        EditText telValue=(EditText)findViewById(R.id.telValue);
        EditText passValue=(EditText)findViewById(R.id.passValue);
        EditText passConfirmValue=(EditText)findViewById(R.id.passConfirmValue);
        EditText emailValue=(EditText)findViewById(R.id.mailValue);

        String name=nameValue.getText().toString();
        String surname=surnameValue.getText().toString();
        String age=ageValue.getText().toString();
        String address=addressValue.getText().toString();
        String tel=telValue.getText().toString();
        String password=passValue.getText().toString();
        String passConfirm=passConfirmValue.getText().toString();
        String email=emailValue.getText().toString();

        if(name.equals("")||name.isEmpty())
        {
            errMsg+=" Name";
        }

        if(surname.equals("")||surname.isEmpty())
        {
            errMsg+=" Surname";
        }

        if(age.equals("")||surname.isEmpty())
        {
            errMsg+=" Age";
        }

        if(address.equals("")||address.isEmpty())
        {
            errMsg+=" Address";
        }

        if(tel.equals("")||tel.isEmpty())
        {
            errMsg+=" Tel";
        }

        if(password.equals("")||password.isEmpty())
        {
            errMsg+=" Password";
        }

        if(passConfirm.equals("")||passConfirm.isEmpty())
        {
            errMsg+=" Confirmed Password";
        }

        else if(email.equals("")||email.isEmpty())
        {
            errMsg+=" E-mail";
        }

        else if(!password.equals(passConfirm))
        {
            errMsg+=" Matching Passwords";
        }

        //Check if the passwords match....
        //and Display the errors found in a toast
        if(!errMsg.equals(""))
        {
            errMsg+="\n Required!!";
        }

        return errMsg;
    }

    public String POST(String url,Staff staff)
    {
        InputStream inputStream;
        String result="";

        try
        {
            HttpClient httpClient=new DefaultHttpClient();

            HttpPost httpPost=new HttpPost(url);
            String json;

            json= JSONConverter.convertStaffToJSON(staff).toString();
            StringEntity se=new StringEntity(json);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse=httpClient.execute(httpPost);
            inputStream=httpResponse.getEntity().getContent();

            if(inputStream!=null)
            {
                result=convertInputStreamToString(inputStream);
            }
            else
                result="Error Occurred.";
        }

        catch(Exception ex)
        {
            Log.d("InputStream", ex.getLocalizedMessage());
        }
        return result;
    }

    private class HttpASyncTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params){
            Staff staff=null;
            try {
                staff=buildStaff();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return POST(params[0],staff);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {
                try
                {
                    JSONObject jsonObject=new JSONObject(s);

                    Staff staff=JSONConverter.convertJSONtoStaff(jsonObject);

                    if(staff.getId()==null)
                    {
                        Toast.makeText(getBaseContext(), "Sorry an error occurred", Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(),staff.getFirstName()+" "+staff.getLastName()+
                                " Registered!",Toast.LENGTH_LONG).show();

                        Intent i=new Intent(view.getContext(),DashBoard.class);
                        RegisterActivity.this.startActivity(i);
                    }
                }

                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
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
