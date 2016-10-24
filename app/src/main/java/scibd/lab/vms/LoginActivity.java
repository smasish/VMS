package scibd.lab.vms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import scibd.lab.vms.utils.AppConstants;
import scibd.lab.vms.utils.AppUtils;
import scibd.lab.vms.utils.SharedPreferencesHelper;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    String p;
    String k;
    private EditText username,password;
    Button login;
   // TextView text;
    String user="",pass="";
    private Context con;
    private boolean flag = false;
    private static ProgressDialog pd;
    int increment;
    Handler handler;

    private static final String REGISTER_URL = "http://119.148.43.34/mamoni/survey/api/login";

    public static final String KEY_USERNAME = "loginName";
    public static final String KEY_PASSWORD = "loginPass";

    private String userDistrict;
    private String userType;

    String name="";



    //http://vmsservice.scibd.info/vmsservice.asmx/GetRequestAll?staffID=743
    //http://vmsservice.scibd.info/vmsservice.asmx/CheckSignIn?loginName=asr&loginPass=hjh

//wxyz789ABC
    //shyamal.mondal


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        con = this;
        flag = false;

        username=(EditText)findViewById(R.id.user);
        password=(EditText)findViewById(R.id.password);
        login =(Button)findViewById(R.id.login);
        login.setOnClickListener(this);


    }



    private class BackOperation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

               // flag = true;
               // loginUser();


           //     AppUtils.setUserName(LoginActivity.this,user);
           //     AppUtils.setUserPassword(LoginActivity.this,pass);
                //  final String email = editTextEmail.getText().toString().trim();
                Log.d("response---", "********" );
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.LOGIN_API,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response---", "********" + response.toString());


                                JSONArray mArray;
                                try {
                                    mArray = new JSONArray(response.toString());
                                    for (int i = 0; i < mArray.length(); i++) {
                                        JSONObject mJsonObject = mArray.getJSONObject(i);
                                        Log.d("OutPut---", mJsonObject.getString("StaffID"));
                                        Log.d("OutPut---", mJsonObject.getString("FullName"));
                                        name = mJsonObject.getString("StaffID");

                                      SharedPreferencesHelper.setStaff(con,name);
                                        SharedPreferencesHelper.setName(con,mJsonObject.getString("FullName"));
                                        flag = true;

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("response--flag-", ""+flag );
                                Toast.makeText(LoginActivity.this, "Welcome "+ name, Toast.LENGTH_SHORT).show();

                               // if(flag) {
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.putExtra("staffid",name);
                                    startActivity(i);
                             //   }
                                AppUtils.setUserType(LoginActivity.this,"");
                                pd.dismiss();
                                // AppUtils.setFullName(LoginActivity.this,tokens[1]);
                                // AppUtils.setUserType(LoginActivity.this,userType);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                                NetworkResponse networkResponse = error.networkResponse;
                                if (networkResponse != null) {
                                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                                }
                                Toast.makeText(LoginActivity.this, "Invalid response", Toast.LENGTH_SHORT).show();
                                if (error instanceof TimeoutError) {
                                    Log.e("Volley", "TimeoutError");
                                }else if(error instanceof NoConnectionError){
                                    Log.e("Volley", "NoConnectionError");
                                }
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put(KEY_USERNAME,user);
                        params.put(KEY_PASSWORD,pass);

                        try {
                            JSONObject data = new JSONObject();
                            data.put("loginName", user);
                            data.put("loginPass", pass);
                            Log.e("request",data.toString());
                            params.put("data login-------", data.toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //params.put("data", "{'username':'"+username+"','password':'"+password+"'}");
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);
                Log.d("====22====","----"+flag);

            } catch (Exception e) {
                e.printStackTrace();

            }
            return "";
        }


        @Override
        protected void onPostExecute(String result) {

           // lv.setAdapter(adapter);
            Log.d("========","----"+flag);
//            if(flag) {
//                Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(i);
//            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    public void onClick(View v) {
        if(v == login){
            String title = "loading";
            String message = "Checking username \nPlease wait...";



           // makeHttpRequest();
            user= username.getText().toString().trim();
            pass= password.getText().toString().trim();

            user = "shyamal.mondal";
            pass = "wxyz789ABC";

            if (user.equalsIgnoreCase("")) {
                AlertMessage.showMessage(LoginActivity.this, "Please insert User name.",
                        "");

            }
            else if (pass.equalsIgnoreCase("")) {
                AlertMessage.showMessage(LoginActivity.this, "Please insert Password.",
                        "");

            }
            else {
                pd = ProgressDialog.show(con, title, message, true, true);
                new BackOperation().execute();
            }
//            Intent i = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(i);

        }
    }

    private void makeHttpRequest() {

        RequestQueue mVolleyQueue = Volley.newRequestQueue(con);
        String url = AppConstants.API_URL;

        JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
               // Utils.LoadingControl(false, "loading");
              //  listView.setAdapter(new OptionAdapter(context,response,2));
                Log.d("response==========",response.toString());

                JSONArray mArray;
                try {
                    mArray = new JSONArray(response.toString());
                    for (int i = 0; i < mArray.length(); i++) {
                        JSONObject mJsonObject = mArray.getJSONObject(i);
                        Log.d("OutPut---", mJsonObject.getString("PassengerName"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //sToast(error.toString());
                //Log.e("Exception",error.toString());
            }
        });

        jReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mVolleyQueue.add(jReq);
    }

    private void loginUser(){
        user= username.getText().toString().trim();
        pass = password.getText().toString().trim();
       // AppUtils.setUserName(LoginActivity.this,user);
       // AppUtils.setUserPassword(LoginActivity.this,pass);
        //  final String email = editTextEmail.getText().toString().trim();
        user = "shyamal.mondal";
        pass = "wxyz789ABC";
        Log.d("response---", "********" );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.LOGIN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response---", "********" + response.toString());


                        JSONArray mArray;
                        try {
                            mArray = new JSONArray(response.toString());
                            for (int i = 0; i < mArray.length(); i++) {
                                JSONObject mJsonObject = mArray.getJSONObject(i);
                                Log.d("OutPut---", mJsonObject.getString("StaffID"));
                                Log.d("OutPut---", mJsonObject.getString("FullName"));
                                name = mJsonObject.getString("FullName");

                                flag = true;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response--flag-", ""+flag );
                        Toast.makeText(LoginActivity.this, "Welcome "+ name, Toast.LENGTH_SHORT).show();


                            AppUtils.setUserType(LoginActivity.this,"");
                        pd.dismiss();
                           // AppUtils.setFullName(LoginActivity.this,tokens[1]);
                           // AppUtils.setUserType(LoginActivity.this,userType);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                        }
                        Toast.makeText(LoginActivity.this, "Invalid response", Toast.LENGTH_SHORT).show();
                        if (error instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        }else if(error instanceof NoConnectionError){
                            Log.e("Volley", "NoConnectionError");
                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,user);
                params.put(KEY_PASSWORD,pass);

                try {
                    JSONObject data = new JSONObject();
                    data.put("loginName", user);
                    data.put("loginPass", pass);
                    Log.e("request",data.toString());
                    params.put("data login-------", data.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                //params.put("data", "{'username':'"+username+"','password':'"+password+"'}");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void finishActivity(){

        finish();
    }




}
