package scibd.lab.vms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scibd.lab.vms.utils.AppConstants;
import scibd.lab.vms.utils.SharedPreferencesHelper;


public class ChecklistActivity extends AppCompatActivity {

	MyCustomAdapter dataAdapter = null;
	private Context con;
	List<String> categories;
	private Spinner spinner;
	private EditText comment;
	String name="";
	ArrayAdapter<String> spinAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check);
		con = this;

		//Generate list View from ArrayList
		displayListView();

		checkButtonClick();

		spinner = (Spinner) findViewById(R.id.car_id);
		comment = (EditText)findViewById(R.id.remark_id);
		comment.requestFocus();
		// Spinner click listener
		//spinner.setOnItemSelectedListener(this);

		// Spinner Drop down elements
		categories = new ArrayList<String>();
		categories.add("Select Car");


		// Creating adapter for spinner
		spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(spinAdapter);
		loadCars();

	}

	private void displayListView() {

		//Array list of countries
		ArrayList<ChecklistData> countryList = new ArrayList<ChecklistData>();
		ChecklistData country = new ChecklistData("Fuel (at least half tank)",true);
		countryList.add(country);
		country = new ChecklistData("Radiator Water + Cap",true);
		countryList.add(country);
		country = new ChecklistData("All fluid Level",true);
		countryList.add(country);
		country = new ChecklistData("Tire Pressure",true);
		countryList.add(country);
		country = new ChecklistData("Battary condition",true);
		countryList.add(country);
		country = new ChecklistData("Starting Condition",true);
		countryList.add(country);
		country = new ChecklistData("Cigaret Lighter",true);
		countryList.add(country);
		country = new ChecklistData("Brake System",true);
		countryList.add(country);
		country = new ChecklistData("AC System",true);
		countryList.add(country);
		country = new ChecklistData("Seat Belt",true);
		countryList.add(country);
		country = new ChecklistData("All lights system",true);
		countryList.add(country);
		country = new ChecklistData("Overall Body Condition",true);
		countryList.add(country);
		country = new ChecklistData("Windshield + Wiper + Knob",true);
		countryList.add(country);
		country = new ChecklistData("Horn",true);
		countryList.add(country);
		country = new ChecklistData("Fire Extinguisher",true);
		countryList.add(country);
		country = new ChecklistData("First Aid Box(17 itemes)",true);
		countryList.add(country);
		country = new ChecklistData("Mineral Water",true);
		countryList.add(country);
		country = new ChecklistData("Vehicle Clean(In & Out)",true);
		countryList.add(country);
		country = new ChecklistData("Other necessary Items",true);
		countryList.add(country);





		//create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this,
				R.layout.checkinfo, countryList);
		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// When clicked, show a toast with the TextView text
				ChecklistData country = (ChecklistData) parent.getItemAtPosition(position);
//				Toast.makeText(getApplicationContext(),
//						"Clicked on Row: " + country.getName(),
//						Toast.LENGTH_LONG).show();
			}
		});

	}

	public void back(View v){
		this.finish();
	}

	private void postAllTrip3(){

		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Posting data to server.Please wait...");
		progressDialog.show();

		String URL = AppConstants.checklist_API;
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

//							datasource.deleteComment(tripData);
						// adapter.remove(tripData);
						//lv.invalidate();
						//	}else{
						Toast.makeText(ChecklistActivity.this,response,Toast.LENGTH_LONG).show();
						//	}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(ChecklistActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
					}
				}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> data = new HashMap<String,String>();

				String carno = spinner.getSelectedItem().toString();
				data.put("CarNo", carno);
				String today  =  getToday("yyyy-MM-dd");
				data.put("ChecklistDate", today);
				//data.put("Author", "BNK");
				data.put("StaffID", SharedPreferencesHelper.getStaff(con));
				data.put("Remarks", comment.getText().toString());

				ArrayList<ChecklistData> countryList = dataAdapter.countryList;
				ChecklistData country = countryList.get(0);
				for(int i=0;i<countryList.size();i++) {
					 country = countryList.get(i);
					 if(country.isSelected()){

					 }
				}
				country = countryList.get(0);
				if(country.isSelected()){
					data.put("FuelCheck", "1");
				}else
					data.put("FuelCheck", "0");

				country = countryList.get(1);
				if(country.isSelected()){
					data.put("RadiatorWaterCheck", "1");
				}else
					data.put("RadiatorWaterCheck", "0");

				country = countryList.get(2);
				if(country.isSelected()){
					data.put("FluidLevelCheck", "1");
				}else
					data.put("FluidLevelCheck", "0");
				country = countryList.get(3);
				if(country.isSelected()){
					data.put("TirePressureCheck", "1");
				}else
					data.put("TirePressureCheck", "0");
				country = countryList.get(4);
				if(country.isSelected()){
					data.put("BattaryConditionCheck", "1");
				}else
					data.put("BattaryConditionCheck", "0");
				country = countryList.get(5);
				if(country.isSelected()){
					data.put("StartingConditionCheck", "1");
				}else
					data.put("StartingConditionCheck", "0");
				country = countryList.get(6);
				if(country.isSelected()){
					data.put("CigaretLighterCheck", "1");
				}else
					data.put("CigaretLighterCheck", "0");
				country = countryList.get(7);
				if(country.isSelected()){
					data.put("BrakeSystemCheck", "1");
				}else
					data.put("BrakeSystemCheck", "0");
				country = countryList.get(8);
				if(country.isSelected()){
					data.put("AcSystemCheck", "1");
				}else
					data.put("AcSystemCheck", "0");
				country = countryList.get(9);
				if(country.isSelected()){
					data.put("SeatBeltCheck", "1");
				}else
					data.put("SeatBeltCheck", "0");
				country = countryList.get(10);
				if(country.isSelected()){
					data.put("AllLightSystemCheck", "1");
				}else
					data.put("AllLightSystemCheck", "0");
				country = countryList.get(11);
				if(country.isSelected()){
					data.put("OverAllBodyConditionCheck", "1");
				}else
					data.put("OverAllBodyConditionCheck", "0");
				country = countryList.get(12);
				if(country.isSelected()){
					data.put("WindshieldWiperKnobCheck", "1");
				}else
					data.put("WindshieldWiperKnobCheck", "0");
				country = countryList.get(13);
				if(country.isSelected()){
					data.put("HornCheck", "1");
				}else
					data.put("HornCheck", "0");
				country = countryList.get(14);
				if(country.isSelected()){
					data.put("FireExitinguisherCheck", "1");
				}else
					data.put("FireExitinguisherCheck", "0");
				country = countryList.get(15);
				if(country.isSelected()){
					data.put("FirstAidBoxCheck", "1");
				}else
					data.put("FirstAidBoxCheck", "0");
				country = countryList.get(16);
				if(country.isSelected()){
					data.put("MineralWaterCheck", "1");
				}else
					data.put("MineralWaterCheck", "0");
				country = countryList.get(17);
				if(country.isSelected()){
					data.put("VehicleCleanCheck", "1");
				}else
					data.put("VehicleCleanCheck", "0");
				country = countryList.get(18);
				if(country.isSelected()){
					data.put("OtherItemsCheck", "1");
				}else
					data.put("OtherItemsCheck", "0");



				//data.put("StaffID", start_place);
//				//data.put("CarNo", start_place);
//				data.put("StartPoint", t.getStartplace().toString());
//				data.put("StartTime", t.getStartdate().toString());
//				data.put("StartKM", t.getStartkm().toString());
//				data.put("EndPoint", t.getEndplace().toString());
//				data.put("EndTime", t.getEnddate().toString());
//				data.put("EndKM", t.getEndKM().toString());

				Log.d(".response--.", ".."+data);
				return data;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}



	public void submit(View v){
		if(spinner.getSelectedItemPosition()==0){
			AlertMessage.showMessage(con,"Sorry, Car is not selected.","Please select Car");
		}

//		else if(comment.getText().toString().length()<1){
//			//Toast.makeText(con,"No Remarks.","Submitting without remarks.").show();
//		}
		else
			postAllTrip3();


	}

	private class MyCustomAdapter extends ArrayAdapter<ChecklistData> {

		private ArrayList<ChecklistData> countryList;

		public MyCustomAdapter(Context context, int textViewResourceId,
							   ArrayList<ChecklistData> countryList) {
			super(context, textViewResourceId, countryList);
			this.countryList = new ArrayList<ChecklistData>();
			this.countryList.addAll(countryList);
		}

		private class ViewHolder {
			//TextView code;
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.checkinfo, null);

				holder = new ViewHolder();
				//holder.code = (TextView) convertView.findViewById(R.id.code);
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
				convertView.setTag(holder);

				holder.name.setOnClickListener( new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v ;
						ChecklistData country = (ChecklistData) cb.getTag();
						Toast.makeText(getApplicationContext(),
								"Clicked on Checkbox: " + cb.getText() +
										" is " + cb.isChecked(),
								Toast.LENGTH_LONG).show();
						country.setSelected(cb.isChecked());
					}
				});
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}

			ChecklistData country = countryList.get(position);
			//holder.code.setText(" (" +  country.getCode() + ")");
			holder.name.setText(country.getName());
			holder.name.setChecked(country.isSelected());
			holder.name.setTag(country);

			return convertView;

		}

	}

	public static String getToday(String format){
		Date date = new Date();
		return new SimpleDateFormat(format).format(date);
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
						//flag = true;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				spinner.setAdapter(spinAdapter);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressDialog.dismiss();
				Toast.makeText(ChecklistActivity.this,"No Cars found",Toast.LENGTH_SHORT).show();
			}
		}){

		};
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(request);
	}

	private void checkButtonClick() {


//		Button myButton = (Button) findViewById(R.id.findSelected);
//		myButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				StringBuffer responseText = new StringBuffer();
//				responseText.append("The following were selected...\n");
//
//				ArrayList<ChecklistData> countryList = dataAdapter.countryList;
//				for(int i=0;i<countryList.size();i++){
//					ChecklistData country = countryList.get(i);
//					if(country.isSelected()){
//						responseText.append("\n" + country.getName());
//					}
//				}
//
//				Toast.makeText(getApplicationContext(),
//						responseText, Toast.LENGTH_LONG).show();
//
//			}
//		});

	}
}
