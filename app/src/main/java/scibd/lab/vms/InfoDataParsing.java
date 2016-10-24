package scibd.lab.vms;

import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import scibd.lab.vms.utils.AppConstants;

public class InfoDataParsing {

private static Context context;



	public static boolean JSONParse(Context con, final String api) {
		// TODO Auto-generated method stub

		InfoData infoData = null;
		AllInfoData.removeAllData();

			/*
			 * initialize state object
			 */


		RequestQueue mVolleyQueue = Volley.newRequestQueue(con);
		String url = api;
		infoData = new InfoData();
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
						Log.d("OutPu---t", mJsonObject.getString("PassengerName"));
						//infoData.setDescription(mJsonObject.getString("PassengerName"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}



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



		return true;

	}
}
