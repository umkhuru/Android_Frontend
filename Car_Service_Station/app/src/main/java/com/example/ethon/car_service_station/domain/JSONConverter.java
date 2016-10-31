package com.example.ethon.car_service_station.domain;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
/**
 * Created by Ethon on 2016/10/31.
 */

public class JSONConverter {

    //Convert the Staff object to JSON..
    public static JSONObject convertStaffToJSON(Staff staff)
    {
        JSONObject jsonObject=null;
        try
        {

            JSONObject contactJSON=new JSONObject();
            contactJSON.put("address",staff.getAddress().getAddress());
            contactJSON.put("tel",staff.getAddress().getTel());

            List<Job> jobList;
            JSONArray jobListJSON=null;

            if(staff.getJobList()!=null&&!staff.getJobList().isEmpty())
            {
                jobListJSON=new JSONArray();
                jobList=staff.getJobList();
                for(int x=0;x<jobList.size();x++)
                {
                    jobListJSON.put(JSONConverter.convertJobToJSON(jobList.get(x)));
                }
            }

            jsonObject=new JSONObject();
            jsonObject.put("id",staff.getId());
            jsonObject.put("firstName",staff.getFirstName());
            jsonObject.put("lastName",staff.getLastName());
            jsonObject.put("age",staff.getAge());
            jsonObject.put("eMail",staff.geteMail());
            jsonObject.put("jobList",jobListJSON);
            jsonObject.put("password",staff.getPassword());
            jsonObject.put("address",contactJSON);

        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    //Convert a JSON object to staff..
    public static Staff convertJSONtoStaff(JSONObject jsonObject)
    {
        Staff staff=null;
        try
        {
            ContactDetails address=new ContactDetails
                    .Builder(jsonObject.getJSONObject("address").getString("tel"))
                    .address(jsonObject.getJSONObject("address").getString("address"))
                    .build();

            //find a jobList and run through all the jobs

            staff=new Staff
                    .Builder(jsonObject.getString("lastName"))
                    .firstName(jsonObject.getString("firstName"))
                    .age(jsonObject.getInt("age"))
                    .address(address)
                    .eMail(jsonObject.getString("eMail"))
                    .password(jsonObject.getString("password"))
                    .id(jsonObject.getLong("id"))
                    .build();

        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return staff;
    }

    //Convert a Job object to JSON..
    public static JSONObject convertJobToJSON(Job job)
    {
        JSONObject jsonObject=null;
        try
        {
            jsonObject=new JSONObject();
            jsonObject.put("jobDate",job.getJobDate());
            jsonObject.put("id",job.getId());
            jsonObject.put("description",job.getDescription());
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return jsonObject;
    }

    //Convert JSON object to job..
    public static Job convertJSONtoJob(JSONObject jsonObject)
    {
        Job job=null;
        try
        {
            job=new Job
                    .Builder(jsonObject.getString("jobDate"))
                    .id(jsonObject.getLong("id"))
                    .description(jsonObject.getString("description"))
                    .build();
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return job;
    }
}
