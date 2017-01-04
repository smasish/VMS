package scibd.lab.vms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import scibd.lab.vms.utils.AppUtils;
import scibd.lab.vms.utils.SharedPreferencesHelper;

public class InfoActivity extends AppCompatActivity {

	ListView lv,lv_history;
	String name = "";
	Boolean flag  = false;

	Context con;
	ArrayAdapter<String> adapter;
	//RadioAdapter adapter;
	public TextView Title, Details;
	int pos = 0;
	private ProgressDialog pDialog;
	String str = "";
	private static ProgressDialog pd;
	String[] nevg_array;
	JSONArray mArray;
	private EditText staff_id;
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		con = this;
		setContentView(R.layout.request);

		Title = (TextView) findViewById(R.id.password);
		//Details = (TextView) findViewById(R.id.startpoint_id);

		lv = (ListView) findViewById(R.id.reqList);

		lv_history = (ListView) findViewById(R.id.historyList);

		//parsingNewsData(AppConstants.API_URL);

		//adapter = new RadioAdapter(this);


		staff_id = (EditText)findViewById(R.id.staffid);

	}


	public void search(View v){
		String id = "";
		id = staff_id.getText().toString();
		String url = AppConstants.API_GETREQUEST+id+"&requestNo="+SharedPreferencesHelper.getReqNo(con);
		Log.d("====url====","----"+url);
		if(id.length()>=1){
			makeHttpRequest(url);
		}
	}


	@Override
	protected void onResume() {
		super.onResume();

		String title = "loading";
		String message = "Checking username \nPlease wait...";
		pd = ProgressDialog.show(con, title, message, true, true);
		new BackOperation().execute("");
		Log.d("==onresume theke===","----");


	}

	private class HistoryList extends AsyncTask<String, String, String> {


		@Override
		protected String doInBackground(String... params) {

			try {


				String url = AppConstants.API_URL+SharedPreferencesHelper.getStaff(con);
				Log.d("==url======","----"+url);
				history_parsing(url);


			} catch (Exception e) {
				e.printStackTrace();

			}
			return null;
		}


		@Override
		protected void onPostExecute(String result) {

			lv.setAdapter(adapter);
		}


		@Override
		protected void onPreExecute() {

		}


		@Override
		protected void onProgressUpdate(String... text) {


		}
	}

	private class BackOperation extends AsyncTask<String, String, String> {


		@Override
		protected String doInBackground(String... params) {

			try {


				String url = AppConstants.API_URL+SharedPreferencesHelper.getStaff(con);
				Log.d("==url======","----"+url);
				makeHttpRequest(url);


			} catch (Exception e) {
				e.printStackTrace();

			}
			return null;
		}


		@Override
		protected void onPostExecute(String result) {

			lv.setAdapter(adapter);
		}


		@Override
		protected void onPreExecute() {

		}


		@Override
		protected void onProgressUpdate(String... text) {


		}
	}


	private void parsingNewsData(final String url) {

		if (AppUtils.isNetConnected(con)) {

			try {
				pDialog = ProgressDialog.show(InfoActivity.this, " Please wait ...",
						"Loading Info data ...", false, false);

				final Thread d = new Thread(new Runnable() {

					public void run() {

						if (InfoDataParsing.JSONParse(InfoActivity.this,url)) {
							// Toast.makeText(mCon, "Ok ", Toast.LENGTH_LONG);
							Log.w(" parsing is complete", "......" + AllInfoData.getAllData().elementAt(0).getPage_title().toString());
							//Title_ok.setText("ok.....");
//				Title.setText(""+AllInfoData.getAllData().elementAt(0).getPage_title().toString());
//				Details.setText(""+AllInfoData.getAllData().elementAt(0).getDescription().toString());

						} else {
							// Toast.makeText(mCon, "error", Toast.LENGTH_LONG);
							Log.w("parsing not completed", "--");
						}

						runOnUiThread(new Runnable() {

							public void run() {
								if (pDialog != null) {
									pDialog.cancel();

								}

								Title.setText("" + AllInfoData.getAllData().elementAt(0).getPage_title().toString());
								Details.setText("" + AllInfoData.getAllData().elementAt(0).getDescription().toString());


							}

						});

					}

				});

				d.start();

			} catch (Exception e) {
				Log.d("network problem:", e.toString());
			}


		} else {
			AlertMessage.showMessage(this, "Info", "No internet connection found.");
		}

	}

	private void history_parsing(String url) {

		RequestQueue mVolleyQueue = Volley.newRequestQueue(con);


		JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				// Utils.LoadingControl(false, "loading");
				//  listView.setAdapter(new OptionAdapter(context,response,2));
				Log.d("response=======",response.toString());



				try {
					nevg_array  = new String[response.length()];
					mArray = new JSONArray(response.toString());
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject mJsonObject = mArray.getJSONObject(i);
						Log.d("OutPut---", mJsonObject.getString("ContactNo"));
						Log.d("OutPut---", mJsonObject.getString("PassengerName"));
						name = "Name: "+mJsonObject.getString("PassengerName")+" \nStart Point: "+mJsonObject.getString("Start") + "\nContact No: "+mJsonObject.getString("ContactNo")
								+ "\nFrom: "+mJsonObject.getString("DateTimeFrom")+" To:  "+mJsonObject.getString("DateTimeTo") ;
						nevg_array[i] = name;
						flag = true;


					}
				} catch (JSONException e) {
					e.printStackTrace();
				}



				adapter = new ArrayAdapter<String>(InfoActivity.this,
						android.R.layout.simple_list_item_1, android.R.id.text1, nevg_array);

				lv_history.setAdapter(adapter);
				pd.dismiss();
				lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					// argument position gives the index of item which is clicked
					public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
					{
						String selectedmovie=nevg_array[position];

						try{
							JSONObject mJsonObject = mArray.getJSONObject(position);
							SharedPreferencesHelper.setMobile(con,""+mJsonObject.getString("ContactNo"));
							SharedPreferencesHelper.setName(con,""+mJsonObject.getString("PassengerName"));
							//SharedPreferencesHelper.setStaff(con,""+mJsonObject.getString("Request"));
							//	SharedPreferencesHelper.setName(con,""+mJsonObject.getString("Request"));
							selectedmovie = mJsonObject.getString("AllocationId");
							SharedPreferencesHelper.setAllocationid(con,""+mJsonObject.getString("AllocationId"));

							Log.d("reg no---", mJsonObject.getString("Request"));
							SharedPreferencesHelper.setReqNo(con,mJsonObject.getString("Request"));

						} catch (JSONException e)

						{
							e.printStackTrace();
						}
						Toast.makeText(getApplicationContext(), "Request Selected : "+selectedmovie,   Toast.LENGTH_LONG).show();
						Intent i = new Intent(InfoActivity.this, RequestDetailsActivity.class);
						i.putExtra("request",selectedmovie);
						startActivity(i);
					}
				});



//				adapter=new ArrayAdapter<String>(
//						MainActivity.this,android.R.layout.simple_list_item_1, nevg_array){

				JSONArray mArray2;
				try {
					mArray2 = new JSONArray(response.toString());
					for (int i = 0; i < mArray2.length(); i++) {
						JSONObject mJsonObject = mArray2.getJSONObject(i);
						Log.d("OutPut---", mJsonObject.getString("PassengerName"));
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


	private void makeHttpRequest(String url) {

		RequestQueue mVolleyQueue = Volley.newRequestQueue(con);


		JsonArrayRequest jReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				// Utils.LoadingControl(false, "loading");
				//  listView.setAdapter(new OptionAdapter(context,response,2));
				Log.d("response=======",response.toString());



				try {
					nevg_array  = new String[response.length()];
					mArray = new JSONArray(response.toString());
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject mJsonObject = mArray.getJSONObject(i);
						Log.d("OutPut---", mJsonObject.getString("ContactNo"));
						Log.d("OutPut---", mJsonObject.getString("PassengerName"));
						name = "Name: "+mJsonObject.getString("PassengerName")+" \nStart Point: "+mJsonObject.getString("Start") + "\nContact No: "+mJsonObject.getString("ContactNo")
								+ "\nFrom: "+mJsonObject.getString("DateTimeFrom")+" To:  "+mJsonObject.getString("DateTimeTo") ;
						nevg_array[i] = name;
						flag = true;


					}
				} catch (JSONException e) {
					e.printStackTrace();
				}


//				if(name.length()<1) {
//					name = "Shyamal Mondal";
//					nevg_array  = new String[1];
//					nevg_array[0] = name;
//				}
//				for(int i = 0;i<response.length();i++){
//					try {
//						//nevg_array[i] = response.getJSONObject(i).getString("PassengerName:").toString();
//
//						//Log.d("OutPut------", ""+nevg_array[i]);
//						//  Toast.makeText(context,"--"+nevg_array[i],Toast.LENGTH_LONG).show();
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
				// adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, android.R.id.text1, nevg_array);


				 adapter = new ArrayAdapter<String>(InfoActivity.this,
						android.R.layout.simple_list_item_1, android.R.id.text1, nevg_array);

				lv.setAdapter(adapter);
				pd.dismiss();
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					// argument position gives the index of item which is clicked
					public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
					{
						String selectedmovie=nevg_array[position];

						try{
						JSONObject mJsonObject = mArray.getJSONObject(position);
							SharedPreferencesHelper.setMobile(con,""+mJsonObject.getString("ContactNo"));
							SharedPreferencesHelper.setName(con,""+mJsonObject.getString("PassengerName"));
							//SharedPreferencesHelper.setStaff(con,""+mJsonObject.getString("Request"));
						//	SharedPreferencesHelper.setName(con,""+mJsonObject.getString("Request"));
							selectedmovie = mJsonObject.getString("AllocationId");
							SharedPreferencesHelper.setAllocationid(con,""+mJsonObject.getString("AllocationId"));

							Log.d("reg no---", mJsonObject.getString("Request"));
							SharedPreferencesHelper.setReqNo(con,mJsonObject.getString("Request"));

					} catch (JSONException e)

					{
						e.printStackTrace();
					}
						Toast.makeText(getApplicationContext(), "Request Selected : "+selectedmovie,   Toast.LENGTH_LONG).show();
						Intent i = new Intent(InfoActivity.this, RequestDetailsActivity.class);
						i.putExtra("request",selectedmovie);
						startActivity(i);
					}
				});



//				adapter=new ArrayAdapter<String>(
//						MainActivity.this,android.R.layout.simple_list_item_1, nevg_array){

				JSONArray mArray2;
				try {
					mArray2 = new JSONArray(response.toString());
					for (int i = 0; i < mArray2.length(); i++) {
						JSONObject mJsonObject = mArray2.getJSONObject(i);
						Log.d("OutPut---", mJsonObject.getString("PassengerName"));
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

	private class RadioAdapter extends ArrayAdapter<InfoData> {
		// StateListActivty context;
		private final Context con;

		public RadioAdapter(final Context cont) {
			//super(c, R.layout.imagerow, AllInfoData.getAllDataBytitle());
			//super(cont, R.layout.imagerow, AllInfoData.getAllDataBytitle());

			super(cont,R.layout.imagerow,AllInfoData.getAllData().size());
			con = cont;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(final int position, final View convertView,
							final ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.imagerow, null);
			}

			Log.d("*******size******====", ""
					+ AllInfoData.getAllDataBytitle().size());
			if (position < AllInfoData.getAllDataBytitle().size()) {
				final InfoData offer = AllInfoData.getData(position);

				Log.d("*************====", "" + offer.getPage_title());

				final TextView textView = (TextView) v
						.findViewById(R.id.feedrowitem);

				final TextView text = (TextView) v
						.findViewById(R.id.address_id);

				final ImageView offerLogo = (ImageView) v
						.findViewById(R.id.lefticon);

				/*
				 * if (offer.getBitmap() != null) {
				 * offerLogo.setImageBitmap(offer.getBitmap()); }
				 */

				textView.setText(offer.getPage_title().toString().trim());

				text.setText(offer.getDescription().toString().trim());
			}

			return v;
		}
	}


}