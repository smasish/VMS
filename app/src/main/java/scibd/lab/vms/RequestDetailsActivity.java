package scibd.lab.vms;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import scibd.lab.vms.utils.AppConstants;
import scibd.lab.vms.utils.AppUtils;
import scibd.lab.vms.utils.SharedPreferencesHelper;

public class RequestDetailsActivity extends AppCompatActivity {

	private TextView contact_no,ab,req_no,passenger_name,txt_startplace,txt_startKM,
			txt_startdate,txt_startTime,txt_endplace,txt_endKM,txt_enddate,txt_endtime;
	//,from_date,todate, start_point, end_point;
	String str="";
	private EditText startpoint,startkm,endpoint,endkm, date,time,staff_id;
	private int mYear, mMonth, mDay, mHour, mMinute,ampm;
	String ar="";

	private TextView start_journey,end_journey;

	private Context con;
	private boolean flag = false;
	private static ProgressDialog pd;
	public static final String KEY_USERNAME = "loginName";
	public static final String KEY_PASSWORD = "loginPass";

	private String userDistrict;
	private String userType,carno="";

	String name="",start_time="",start_km="",end_time="",end_km="",start_place="",end_place="";
	private QueryDataSource datasource;
	TripAdapter adapter;
	ListView lv;

	private boolean tripflag=true;
	private Spinner spinner;
	ArrayAdapter<String> dataAdapter;
	String[] nevg_array;
	List<String> categories;
	String request_trip = "",confirm_id="";

	private boolean submitflag = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_details);

//		ActionBar bar = getActionBar();
//for color
//		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00C4CD")));
//for image
		//bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.settings_icon));


		con = this;
		flag = false;
		submitflag = false;

		str = getIntent().getStringExtra("request");

		req_no = (TextView)findViewById(R.id.request_id);
		passenger_name = (TextView)findViewById(R.id.Passenger_id);
		contact_no = (TextView)findViewById(R.id.contact_id);
//		from_date = (TextView)findViewById(R.id.from_date_id);
//		todate = (TextView)findViewById(R.id.txt_todate);
		//start_point = (TextView)findViewById(R.id.startplace_id);
//		end_point = (TextView)findViewById(R.id.endpoint_id);

		start_journey = (TextView) findViewById(R.id.start_journey_id);
		end_journey = (TextView) findViewById(R.id.end_journey_id);
		tripflag=false;

		String a = "01911612673.";
		Log.d("Login oncreate-", "start===="+str);
		req_no.setText(""+SharedPreferencesHelper.getStaff(con));

		startpoint = (EditText) findViewById(R.id.startplace_id);
		startkm = (EditText) findViewById(R.id.startkm_id);
		date = (EditText) findViewById(R.id.datetext_id);
		time = (EditText) findViewById(R.id.time_text_id);



		String today=   getToday("hh:mm a");  // getToday("yyyy-MM-dd hh:mm");
		time.setText(today);

		today=   getToday("yyyy-MM-dd");
		date.setText(today);

		staff_id = (EditText) findViewById(R.id.edit_ConfirmId);

		txt_startplace = (TextView) findViewById(R.id.tx_startplace);
		txt_startKM = (TextView) findViewById(R.id.tx_startkm);
		txt_startdate = (TextView) findViewById(R.id.date_id);
		txt_startTime = (TextView) findViewById(R.id.time_id);

//		txt_endplace = (TextView) findViewById(R.id.tx_startplace);
//		txt_endKM = (TextView) findViewById(R.id.tx_startplace);
//		txt_enddate = (TextView) findViewById(R.id.tx_startplace);
//		txt_endtime = (TextView) findViewById(R.id.tx_startplace);
		Log.d("-----", "mobile===="+SharedPreferencesHelper.getMobile(con));
		String staff = "";//getIntent().getStringExtra("staffid");
		staff = SharedPreferencesHelper.getStaff(con);
		req_no.setText(""+staff);
		passenger_name.setText(""+SharedPreferencesHelper.getName(con));
		contact_no.setText(""+SharedPreferencesHelper.getMobile(con));
//		from_date.setText(""+a);
//		todate.setText(""+a);
//		start_point.setText(""+a);
//		end_point.setText(""+a);


		 spinner = (Spinner) findViewById(R.id.car_id);

		// Spinner click listener
		//spinner.setOnItemSelectedListener(this);

		// Spinner Drop down elements
		 categories = new ArrayList<String>();
		categories.add("Select Car");


		// Creating adapter for spinner
		 dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);
		loadCars();

		lv = (ListView) findViewById(R.id.list);


		datasource = new QueryDataSource(this);
		datasource.open();
		datasource.getAllComments();

		adapter = new TripAdapter(this);
		lv.setAdapter(adapter);

			if(SharedPreferencesHelper.getJourney(con)){

				end_journey.setVisibility(View.VISIBLE);
				start_journey.setVisibility(View.GONE);
				//tripflag = true;

				txt_startplace.setText("End Place: ");
				txt_startKM.setText("End KM: ");
				txt_startdate.setText("End Date: ");
				txt_startTime.setText("End Time: ");
				tripflag=false;
			}else{
				start_journey.setVisibility(View.VISIBLE);
				end_journey.setVisibility(View.GONE);
				txt_startplace.setText("Start Place: ");
				txt_startKM.setText("Start KM: ");
				txt_startdate.setText("Start Date: ");
				txt_startTime.setText("Start Time: ");
				tripflag=true;
			}

	}


	public void save(View v){
		start_place = startpoint.getText().toString();
		Log.d("start_place--", ""+start_place);
		start_km = startkm.getText().toString();

		start_time = date.getText().toString()+" "+time.getText().toString();;
		TripData tripData = null;

if(start_place.length()>1 && start_km.length()>=1) {


	if (tripflag) {
		end_journey.setVisibility(View.VISIBLE);
		tripflag = false;
		start_journey.setVisibility(View.GONE);
		SharedPreferencesHelper.setJourney(con, true);

		tripData = datasource.createComment(start_place, start_km, start_time, "-", "-", "-");

		adapter.add(tripData);
		lv.setAdapter(adapter);
		changed();

	} else {


		int index = datasource.getAllComments().size() - 1;


		//String id = ""+datasource.getAllComments().get(index).getId();

		//int k = Integer.parseInt(id);
//			if(k>0)
//				k = k-1;

		//http://stackoverflow.com/questions/14214713/how-to-compare-two-date-and-time-in-android
//			String tem = datasource.getAllComments().get(k).getStartdate().toString();
//			try{
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				Date date1 = sdf.parse("2009-12-31");
//				Date date2 = sdf.parse(date.getText().toString());
//
//				if(date1.before(date2)) {
//					date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//				}
//			}catch(ParseException ex){
//				ex.printStackTrace();
//			}


		long d = Long.parseLong(datasource.getAllComments().get(index).getStartkm());
		String d1 = datasource.getAllComments().get(index).getStartdate();
		long now = Long.parseLong(start_km);

		Log.d("====k====" + d1, "...id..>>" + start_time);
		if (d < now) {


			if (compare(d1, start_time)) {
				String id = "" + datasource.getAllComments().get(index).getId();
				datasource.updateOrderItems(id, start_place, start_km, start_time);
				end_journey.setVisibility(View.GONE);
				tripflag = true;
				start_journey.setVisibility(View.VISIBLE);
				SharedPreferencesHelper.setJourney(con, false);
				changed();
			} else {
				AlertMessage.showMessage(con, "Sorry", "Invalied End Date/Time.");
			}

		} else {
			AlertMessage.showMessage(con, "Sorry", "End KM is lower than Start KM.");
			tripflag = false;
			SharedPreferencesHelper.setJourney(con, true);
		}


	}

}else{
	AlertMessage.showMessage(con,"Sorry","Please enter place name and Kilometer correctly.");
}



	}
	private void changed(){
		adapter.notifyDataSetChanged();
		Intent refresh = new Intent(this, RequestDetailsActivity.class);
		startActivity(refresh);
		finish();
	}


	public boolean compare(String before,String after){
		//SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		Date d=null;
		Date d1=null;
		//String today=   getToday("MMM-dd-yyyy hh:mm:ss a");
		//String today=   getToday(after);
		//Log.d("*****today*********. ", "--v--"+today);
		try {
			//System.out.println("expdate>> "+date);
			//System.out.println("today>> "+today+"\n\n");
			sdf.setLenient(false);
			d = sdf.parse(before);
			d1 = sdf.parse(after);
			Log.d("---start-. ", "----");
			Log.d("---start-. ", "----"+d1.compareTo(d));
			Log.d("---dddd-. "+d.getTime() , "----"+d1.getTime());
			if(d.compareTo(d1) <0){ // not expired
				Log.d("---not expired--. ", "----");
				return true;
			}else if(d.compareTo(d1)==0){ // both date are same
				Log.d("---same-. ", "----");
				if(d.getTime() < d1.getTime()){// not expired
					return true;
				}else if(d.getTime() == d1.getTime()){//expired
					Log.d("-- expired--. ", "----");
					return false;
				}else{//expired
					Log.d("-- expired--true. ", "----");
					return true;
				}
			}else{//expired
				Log.d("-- expired--false. ", "----");
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getToday(String format){
		Date date = new Date();
		return new SimpleDateFormat(format).format(date);
	}

	private void postAllTrip3(final int x){

		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Posting data to server.Please wait...");
		progressDialog.show();

		String URL = "http://vmsservice.scibd.info/vmsservice.asmx/PostRequest?";
		StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						TripData tripData = null;
						Log.d(".response--.", ".."+response.toString());
						progressDialog.dismiss();
					//	if(response.trim().equals("Successful")){
							//openProfile();
//							tripData = (TripData) datasource.getAllComments().remove(0); //(TripData) getListAdapter().getItem(0);
							submitflag = true;
						Log.d("====x=", "..xx..>>" + x);
						int size = datasource.getAllComments().size();
						if(x == datasource.getAllComments().size()-1) {
							for (int i = 0; i < size; i++) {
								tripData = (TripData) datasource.getAllComments().remove(0); //(TripData) getListAdapter().getItem(0);
								Log.d("====id="+tripData.getId(), "..i..>>" + i+"--"+tripData.getEndKM());
								datasource.deleteComment(tripData);
							}
							adapter = new TripAdapter(RequestDetailsActivity.this);
							lv.setAdapter(adapter);
							//lv.invalidate();
						}
//							datasource.deleteComment(tripData);
						   // adapter.remove(tripData);
							//lv.invalidate();
					//	}else{
							Toast.makeText(RequestDetailsActivity.this,response,Toast.LENGTH_LONG).show();
					//	}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(RequestDetailsActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
					}
				}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> data = new HashMap<String,String>();
				TripData t = datasource.getAllComments().get(x);
				carno = spinner.getSelectedItem().toString();
				Log.d("@@@@ car no ---"+datasource.getAllComments().size(), ".."+carno);
				//data.put("Title", "Android Volley Demo");
				//data.put("Author", "BNK");
			data.put("AllocationId", SharedPreferencesHelper.getAllocationid(con));
			//data.put("StaffID", SharedPreferencesHelper.getStaff(con));
			data.put("CarNo", carno);
			//data.put("StaffID", start_place);
			//data.put("CarNo", start_place);
			data.put("StartPoint", t.getStartplace().toString());
			data.put("StartTime", t.getStartdate().toString());
			data.put("StartKM", t.getStartkm().toString());
			data.put("EndPoint", t.getEndplace().toString());
			data.put("EndTime", t.getEnddate().toString());
			data.put("EndKM", t.getEndKM().toString());

				Log.d(".response--.", ".."+data);
				return data;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	public void confirm(View v){

		confirm_id = staff_id.getText().toString();

		if(confirm_id.length()>=2 && confirm_id.length()<=5){
			confirmtrip();
		}else{
			AlertMessage.showMessage(con,"Sorry","Please give correct Staff ID.");
		}

	}
	private void confirmtrip(){
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Posting data to server.Please wait...");
		progressDialog.show();

		String URL = AppConstants.CONFIRM_API;
		StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						Log.d(".response--.", ".."+response.toString());
						progressDialog.dismiss();
						if(response.trim().equals("Successful")){
							//openProfile();

						}else{
							Toast.makeText(RequestDetailsActivity.this,response,Toast.LENGTH_LONG).show();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(RequestDetailsActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
					}
				}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> data = new HashMap<String,String>();

				Log.d(".confirm id--."+SharedPreferencesHelper.getStaff(con), ".."+confirm_id);
				Log.d("@@@@ car no ---"+datasource.getAllComments().size(), "..");
				//data.put("Title", "Android Volley Demo");
				//data.put("Author", "BNK");
				data.put("AllocationId", SharedPreferencesHelper.getAllocationid(con));
				data.put("StaffID", confirm_id);
				//data.put("StaffID", SharedPreferencesHelper.getStaff(con));


				Log.d(".response--.", ".."+data);
				return data;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}

	private void postAllTrip(){
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Posting data to server.Please wait...");
		progressDialog.show();
		StringRequest request = new StringRequest(Request.Method.POST, AppConstants.SUBMIT_API, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				progressDialog.dismiss();

				// Log.d("------------>>>>" + response, "||");
				// Log.d("------------>>>>" , "||");
				//response = response.replaceAll("\"","");
				//  String tokens[] = response.split("\\n");
				//    Log.d("-------len----->>>>" , "||"+tokens.length);


				String temp = response.toString();
				//temp = temp.replace("\\n","#");
				//String tokens[] = temp.split("#");
				Log.d("---response-->>>>"+response.toString(),"---"+temp);
				Log.d("----test--->>>>" ,"");
//				if(temp.length > 0){
//
//				}
//				else{
//					Toast.makeText(RequestDetailsActivity.this,"No user found",Toast.LENGTH_SHORT).show();
//				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressDialog.dismiss();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				//params.put("username",AppUtils.getUserName(RequestDetailsActivity.this));
				//params.put("password",AppUtils.getPassword(RequestDetailsActivity.this));

				try {
					JSONObject data = new JSONObject();

					carno = spinner.getSelectedItem().toString();
					Log.d("@@@@ car no ---"+datasource.getAllComments().size(), ".."+carno);

					for(int i =0;i<datasource.getAllComments().size();i++) {
						TripData t = datasource.getAllComments().get(i);

						Log.d("====>"+t.getEndKM().toString(), "==>"+t.getEnddate().toString());
						data.put("AllocationId", "1111");
						//data.put("StaffID", SharedPreferencesHelper.getStaff(con));
						data.put("CarNo", carno);
						//data.put("StaffID", start_place);
						//data.put("CarNo", start_place);
						data.put("StartPoint", t.getStartplace().toString());
						data.put("StartTime", t.getStartdate().toString());
						data.put("StartKM", t.getStartkm().toString());
						data.put("EndPoint", t.getEndplace().toString());
						data.put("EndTime", t.getEnddate().toString());
						data.put("EndKM", t.getEndKM().toString());
						Log.d("------data------>>>>" + data, "||");
						params.put("data", data.toString());
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				//params.put("data", "{'username':'"+username+"','password':'"+password+"'}");
				return params;
			}
		};

	//	request.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// A
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(request);
	}

	public void submit(View v){


		//Log.d("..", ".."+start_place);
	//	Log.d("Login e------", "start===="+start_place);
		//Log.d("Login Response------", "start===="+start_place);

		String title = "loading";
		String message = "Checking username \nPlease wait...";


	//	pd = ProgressDialog.show(con, title, message, true, true);
		//new BackOperation().execute();

		if(spinner.getSelectedItemPosition()==0){
			AlertMessage.showMessage(con,"Sorry, Car is not selected.","Please select Car");
		}
		else if(tripflag){
			for(int i = 0;i<datasource.getAllComments().size();i++){
				//int ind = datasource.getAllComments().get(i).getId();
				Log.d(".ind.---", ".."+i);
				postAllTrip3(i);
			}

		}else{
			AlertMessage.showMessage(con,"Sorry,Can't submit now.","You have to complete Trip");
		}

		lv.invalidate();

	}


	private class BackOperation extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			try {

				//  final String email = editTextEmail.getText().toString().trim();
				Log.d("response---", "********" );
				StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.POSTDATA_API,
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
								Toast.makeText(RequestDetailsActivity.this, "Welcome "+ name, Toast.LENGTH_SHORT).show();

								// if(flag) {
								Intent i = new Intent(RequestDetailsActivity.this, MainActivity.class);
								startActivity(i);
								//   }
								AppUtils.setUserType(RequestDetailsActivity.this,"");
								pd.dismiss();
								// AppUtils.setFullName(LoginActivity.this,tokens[1]);
								// AppUtils.setUserType(LoginActivity.this,userType);

							}
						},
						new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								pd.dismiss();
								Toast.makeText(RequestDetailsActivity.this,error.toString(),Toast.LENGTH_LONG).show();

								NetworkResponse networkResponse = error.networkResponse;
								if (networkResponse != null) {
									Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
								}
								Toast.makeText(RequestDetailsActivity.this, "Invalid response", Toast.LENGTH_SHORT).show();
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
						params.put(KEY_USERNAME,start_place);
						params.put(KEY_PASSWORD,start_km);


						try {
							JSONObject data = new JSONObject();
							data.put("StaffID", start_place);
							data.put("CarNo", start_place);
							data.put("StartPoint", start_place);
							data.put("StartTime", start_place);
							data.put("StartKM", start_place);
							data.put("EndPoint", start_place);
							data.put("EndTime", start_place);
							data.put("EndKM", start_place);

							Log.e("request",data.toString());
							params.put("data login-------", data.toString());
						}catch (Exception e){
							e.printStackTrace();
						}
						//params.put("data", "{'username':'"+username+"','password':'"+password+"'}");
						return params;
					}

				};

				RequestQueue requestQueue = Volley.newRequestQueue(RequestDetailsActivity.this);
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

	private class TripAdapter extends ArrayAdapter<TripData> {
		// StateListActivty context;
		private Context con;

		public TripAdapter(final Context c) {
			super(c, R.layout.triprow, datasource.getAllComments());// locallist

			con = c;
		}

		@Override
		public View getView(final int position, final View convertView,
							final ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.triprow, null);
				//Log.d("**************. ", "--v--");
			}
			///Log.d("**************. ", "out----" + position);



			//Log.d("******check********. ", "===" +datasource.getAllComments());
			//String place = datasource.getAllComments().get(position).getStartplace();
			//Log.d("******ss***datr*****. ", "===" +place);
			String s_place = datasource.getAllComments().get(position).getStartplace().toString();

			//Log.d("******im*******. ", "===" +im);
			final TextView textView = (TextView) v.findViewById(R.id.offertext);
			textView.setText(s_place);

			String s_km = datasource.getAllComments().get(position).getStartkm().toString();
			final TextView startkm = (TextView) v.findViewById(R.id.s_km_id);
			startkm.setText(s_km);

			String s_time = datasource.getAllComments().get(position).getStartdate().toString();
			final TextView timeid = (TextView) v.findViewById(R.id.time_id);
			timeid.setText(s_time);

			String end_place = datasource.getAllComments().get(position).getEndplace().toString();
			final TextView endplace = (TextView) v.findViewById(R.id.endplace_id);
			endplace.setText(end_place);

			String end_km = datasource.getAllComments().get(position).getEndKM().toString();
			final TextView endkm = (TextView) v.findViewById(R.id.endkm_id);
			endkm.setText(end_km);

			String end_date = datasource.getAllComments().get(position).getEnddate().toString();
			final TextView enddate = (TextView) v.findViewById(R.id.endtime_id);
			enddate.setText(end_date);


			return v;

		}
	}

	public void dateshow(View v){
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);


		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {

						//date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
						String da = ""+year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
						String a="",b="",c="";
						monthOfYear = monthOfYear+1;

						if(monthOfYear<10)
							b = "0"+monthOfYear;
						else
							b = ""+monthOfYear;

						if(dayOfMonth<10)
							c = "0"+dayOfMonth;
						else
							c = ""+dayOfMonth;
//						try{
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//						Date date1 = sdf.parse("2009-12-31");
//						Date date2 = sdf.parse(da);
//
//							if(date1.before(date2)) {
//								date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//							}
//						}catch(ParseException ex){
//							ex.printStackTrace();
//						}

							date.setText(year + "-" + b + "-" + c);


					}
				}, mYear, mMonth, mDay);
		datePickerDialog.show();
	}

	public void timeshow(View v){
		// Get Current Time
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		ampm = c.get(Calendar.AM_PM);
		int a = c.get(Calendar.AM_PM);

		if(a == 1)
			ar = "PM";
		else
			ar = "AM";

		// Launch Time Picker Dialog
		TimePickerDialog timePickerDialog = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
										  int minute) {
						Log.d("***ampm*****. ", "===" +ampm);
						if(hourOfDay<12)
							ar = "AM";
						if(hourOfDay>12)
							hourOfDay = hourOfDay-12;
						if(minute<10 && hourOfDay<10)
						 time.setText("0"+hourOfDay + ":" + "0"+minute+" "+ar);
						else if(hourOfDay<10 && minute>9)
							time.setText("0"+hourOfDay + ":" + minute+" "+ar);
						else if(hourOfDay>9 && minute<10)
							time.setText(hourOfDay + ":" + "0"+minute+" "+ar);
						else
							time.setText(hourOfDay + ":" + minute+" "+ar);
					}
				}, mHour, mMinute, false);
		timePickerDialog.show();
	}

	public void delete(View v){
		int size = datasource.getAllComments().size();
		TripData tripData;
		if(datasource.getAllComments().size()>0) {
			for (int i = 0; i < size; i++) {
				tripData = (TripData) datasource.getAllComments().remove(0); //(TripData) getListAdapter().getItem(0);
				//Log.d("====id=" + tripData.getId(), "..i..>>" + i + "--" + tripData.getEndKM());
				datasource.deleteComment(tripData);
			}
			adapter = new TripAdapter(RequestDetailsActivity.this);
			lv.setAdapter(adapter);
			Toast.makeText(con,"All entry deleted.",Toast.LENGTH_LONG).show();
			SharedPreferencesHelper.setJourney(con,false);
			Intent refresh = new Intent(this, RequestDetailsActivity.class);
			startActivity(refresh);
			finish();
		}
	}

	public void close(View v){
		this.finish();
	}

	private void loadCars(){
		final ProgressDialog progressDialog = new ProgressDialog(this);


		progressDialog.setMessage("Fetching Car list...");
		progressDialog.show();
		StringRequest request = new StringRequest(Request.Method.POST, "http://vmsservice.scibd.info/vmsservice.asmx/GetCarNo", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				progressDialog.dismiss();

				//
				// Log.d("------------>>>>" , "||");
				//response = response.replaceAll("\"","");
				Log.d("--car->>>>" + response, "||");
				//  String tokens[] = response.split("\\n");
				//    Log.d("-------len----->>>>" , "||"+tokens.length);



					JSONArray mArray;
					try {
						//nevg_array  = new String[response.length()];
						mArray = new JSONArray(response.toString());
						for (int i = 0; i < mArray.length(); i++) {
							JSONObject mJsonObject = mArray.getJSONObject(i);
							//Log.d("OutPut---", mJsonObject.getString("CarNo"));

							name = mJsonObject.getString("CarNo");
							//nevg_array[i] = name;
							categories.add(name);
							flag = true;

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					spinner.setAdapter(dataAdapter);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressDialog.dismiss();
				Toast.makeText(RequestDetailsActivity.this,"No Cars found",Toast.LENGTH_SHORT).show();
			}
		}){

		};
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(request);
	}


}
