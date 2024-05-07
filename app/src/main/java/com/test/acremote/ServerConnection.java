package com.test.acremote;

import android.util.Log;

import com.library.terminal.HttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerConnection
{
    private static String url = "https://api.izone.com.au/testsimplelocalcocb";

    static int[] uploadQuery()
    {
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        String msg="";
        try
        {
            JSONObject object1 = new JSONObject();
            object1.put("Type",1);
            object1.put("No",0);
            object1.put("No1",0);
            JSONObject object = new JSONObject();
            object.put("iZoneV2Request",object1.toString());
            msg = object.toString().replace("\"{","{").replace("}\"","}").replace("\\\"","\"");
            Log.d("TAG", "uploadQuery: " + msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        HttpConnection connection = new HttpConnection(url,header,msg,2000,5000);
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                connection.POSTRequest();
            }
        }.start();

        while(connection.isActive())
        {
            Util.Delay(1);
        }

        if(connection.getResponseCode()==200)
        {
            try
            {
                JSONObject object = new JSONObject(connection.getResponseBody());
                int[] output = new int[4];
                output[0] = object.getInt("Setpoint");
                output[1] = object.getInt("SysFan");
                output[2] = object.getInt("SysMode");
                output[3] = object.getInt("SysOn");
                return output;
            }
            catch (JSONException e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    static int uploadTemp(int tmp)
    {
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        String msg="";
        try
        {
            JSONObject object = new JSONObject();
            object.put("SysSetpoint",tmp);
            msg = object.toString();
            Log.d("TAG", "uploadQuery: " + msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        HttpConnection connection = new HttpConnection(url,header,msg,2000,5000);
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                connection.POSTRequest();
            }
        }.start();

        while(connection.isActive())
        {
            Util.Delay(1);
        }

        if(connection.getResponseCode()==200)
        {
            if(connection.getResponseBody().equals("{OK}"))
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    static int uploadFanSpeed(int value)
    {
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        String msg="";
        try
        {
            JSONObject object = new JSONObject();
            object.put("SysFan",value);
            msg = object.toString();
            Log.d("TAG", "uploadQuery: " + msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        HttpConnection connection = new HttpConnection(url,header,msg,2000,5000);
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                connection.POSTRequest();
            }
        }.start();

        while(connection.isActive())
        {
            Util.Delay(1);
        }

        if(connection.getResponseCode()==200)
        {
            if(connection.getResponseBody().equals("{OK}"))
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    static int uploadMode(int value)
    {
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        String msg="";
        try
        {
            JSONObject object = new JSONObject();
            object.put("SysMode",value);
            msg = object.toString();
            Log.d("TAG", "uploadQuery: " + msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        HttpConnection connection = new HttpConnection(url,header,msg,2000,5000);
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                connection.POSTRequest();
            }
        }.start();

        while(connection.isActive())
        {
            Util.Delay(1);
        }

        if(connection.getResponseCode()==200)
        {
            if(connection.getResponseBody().equals("{OK}"))
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    static int uploadPowerStatus(int value)
    {
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        String msg="";
        try
        {
            JSONObject object = new JSONObject();
            object.put("SysOn",value);
            msg = object.toString();
            Log.d("TAG", "uploadQuery: " + msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        HttpConnection connection = new HttpConnection(url,header,msg,2000,5000);
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                connection.POSTRequest();
            }
        }.start();

        while(connection.isActive())
        {
            Util.Delay(1);
        }

        if(connection.getResponseCode()==200)
        {
            if(connection.getResponseBody().equals("{OK}"))
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

}
