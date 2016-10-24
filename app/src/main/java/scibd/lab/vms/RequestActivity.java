package scibd.lab.vms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scibd.lab.vms.utils.AppConstants;

public class RequestActivity extends Activity {

	private TextView ab;
	private ListView lv;

	final String[] links = new String[] { "Daily Qda","Chantinga","Biogra",
			"gdfgs", "Contact Us" };

	private Context con;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request);

		con = this;
		lv = (ListView)findViewById(R.id.reqList);

		final StateAdapter adapter = new StateAdapter(this);
		lv.setAdapter(adapter);


		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {


				Intent req = new Intent(RequestActivity.this, RequestDetailsActivity.class);
				startActivity(req);
				Toast.makeText(getBaseContext(),"hello", Toast.LENGTH_SHORT).show();
			}
		});

//		ab = (TextView)findViewById(R.id.about);
//
//		ab.setText(" \n" +
//				"Email:\n" +
//				"Phone: +8 \n"+
//				"Facebook: ");

		Log.d("", "");

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
						Log.d("OutPu---t", mJsonObject.getString("PassengerName"));
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
	}

		/*
	 * first adapter for state
	 */

	private class StateAdapter extends ArrayAdapter<String> {
		// StateListActivty context;
		private final Context con;

		public StateAdapter(final Context c) {
			super(c, R.layout.listrow, links);
			con = c;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(final int position, final View convertView,
							final ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listrow, null);
			}
			final TextView textView = (TextView) v
					.findViewById(R.id.feedrowitem);
			textView.setText(links[position]);
			Log.d("--", "---link----"+position);

//			ImageView img = new ImageView(MainListActivity.this);
//			img.setBackgroundResource(imageId[position]);


			//ab.setBackgroundResource(R.drawable.jan1);

			TextView bottom = (TextView) v
					.findViewById(R.id.details);
			bottom.setText(links[position]);






			return v;
		}
	}
}
