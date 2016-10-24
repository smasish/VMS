package scibd.lab.vms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import scibd.lab.vms.utils.AppConstants;

public class AboutActivity extends Activity {

	EditText user, pass;
	Button submit;
	// Creating JSON Parser object

	String username="",password="";

	private Context con;
	JSONObject json;
	private static String url_login = "http://10.0.2.2:8080/AndroidLogin/login_servlet";
	//JSONArray incoming_msg = null;

	HashMap<String, String> params;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		con = this;
		user=(EditText)findViewById(R.id.user);
		pass=(EditText)findViewById(R.id.password);
		submit = (Button)findViewById(R.id.login) ;

		username = user.getText().toString();
		password = pass.getText().toString();

		Log.d("Login oncreate-", "start====");

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// execute method invokes doInBackground() where we open a Http URL connection using the given Servlet URL
				//and get output response from InputStream and return it.
				new SendOperation().execute();

				// Define the web service URL
				//final String URL = "http://www.someurl.com";


// Add the request object to the queue to be executed
			//	ApplicationController.getInstance().addToRequestQueue(req);

			}
		});
	}


	public void data(View v){

		Log.d("Login e------", "start====");
		Log.d("Login Response------", "start===="+username);
		new SendOperation().execute();
	}

	private class SendOperation extends AsyncTask<String, Void, String> {
		String url = "";

		@Override
		protected String doInBackground(String... params) {


			try{
				// POST params to be sent to the server
				HashMap<String, String> params2 = new HashMap<String, String>();
				params2.put("loginName", username);

				params2.put("loginPass", password);

// Define the POST request
				JsonObjectRequest req = new JsonObjectRequest(AppConstants.LOGIN_API, new JSONObject(params2),
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									Log.d("Login Response------", response.toString());
									VolleyLog.v("Response:%n %s=====", response.toString(4));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
					}
				});

// Add the request object to the queue to be executed
				//	ApplicationController.getInstance().addToRequestQueue(req);
			}catch (Exception e){

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			Log.d("done------", "start===="+password);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}


}
