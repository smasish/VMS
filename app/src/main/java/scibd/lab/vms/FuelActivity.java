package scibd.lab.vms;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scibd.lab.vms.utils.AppConstants;
import scibd.lab.vms.utils.SharedPreferencesHelper;

public class FuelActivity extends AppCompatActivity  {


    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private Button reqBtn,checkBtn,travelBtn,maintainBtn;

    private Context con;
    private boolean tripflag=true;
    private Spinner spinner,fuelSpinner;
    private boolean submitflag = false;

    private String userType,carno="";
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private int mYear, mMonth, mDay, mHour, mMinute,ampm;
    private EditText  date;
    private EditText startDate,startkm,fuel_Consumption, staff_Id;
    String name="",fuel_name="",start_date="",start_km="",fuel_consumption="",staff="",fuel_station="",car_no="",radio_id="";

    List<String> categories, fuel_category;

    ArrayAdapter<String> spinAdapter, fuelAdapter;

    int fuel_in = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_in);

        con =this;
        radioGroup = (RadioGroup) findViewById(R.id.radiotype);

        startDate = (EditText) findViewById(R.id.datetext_id);
        spinner = (Spinner) findViewById(R.id.car_id);
        fuelSpinner = (Spinner) findViewById(R.id.fuel_station_id);


        startkm = (EditText) findViewById(R.id.startkm_id);
        fuel_Consumption = (EditText) findViewById(R.id.fuel_id);
        staff_Id = (EditText) findViewById(R.id.staff_id);

        submitflag = false;

        categories = new ArrayList<String>();
        categories.add("Select Car");


        // Creating adapter for spinner
        spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(spinAdapter);

        fuel_category = new ArrayList<String>();
        fuel_category.add("Select Fuel Station");
        fuelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fuel_category);

        // Drop down layout style - list view with radio button
        fuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelSpinner.setAdapter(fuelAdapter);


        loadCars();

        loadFuelStation();

        fuelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
               fuel_in = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void submit(View v){


        int selectedId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(selectedId);

//        Toast.makeText(FuelActivity.this,
//                "....number "+selectedId, Toast.LENGTH_SHORT).show();

        Toast.makeText(FuelActivity.this,
                radioButton.getText(), Toast.LENGTH_SHORT).show();
        carno = spinner.getSelectedItem().toString();

        start_date="";
        start_km="";
        fuel_consumption="";
        staff="";
        fuel_station="";

        radio_id="";

        radio_id = radioButton.getText().toString();
        fuel_station = fuelSpinner.getSelectedItem().toString();

        Log.d(".car no--.", ".."+carno);

        start_date = startDate.getText().toString();
        start_km =  startkm.getText().toString();
        fuel_consumption =  fuel_Consumption.getText().toString();
        staff =  staff_Id.getText().toString();

        Log.d("start_place--", ""+start_date);



       // start_time = date.getText().toString()+" "+start_km.getText().toString();;
      //  TripData tripData = null;

        if(start_date.length()<6 ) {

            AlertMessage.showMessage(con, "Sorry", "Invalid start Date.");
        }
        else if(start_km.length()<3){
            AlertMessage.showMessage(con, "Sorry", "Invalid start KM.");
        }
        else if(fuel_consumption.length()<2){
            AlertMessage.showMessage(con, "Sorry", "Invalid  fuel consumption.");
        }
        else if(staff.length()<3){
            AlertMessage.showMessage(con, "Sorry", "Invalid staff ID.");
        }
        else if(radio_id.length()<2){
            AlertMessage.showMessage(con, "Sorry", "Invalid TransactionMode mode.");
        }
        else if(carno.length()<3){
            AlertMessage.showMessage(con, "Sorry", "Invalid car no.");
        }
        else if(fuel_station.length()<1){
            AlertMessage.showMessage(con, "Sorry", "Invalid fuel station no.");
        }
        else{
            postAllTrip3();
        }

    }




    private void loadFuelStation(){
        final ProgressDialog progressDialog = new ProgressDialog(this);


        progressDialog.setMessage("Fetching Car list...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://vmsservice.scibd.info/vmsservice.asmx/GetFuelStation", new Response.Listener<String>() {
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

                        fuel_name = mJsonObject.getString("FuelStationName");
                        //nevg_array[i] = name;
                        fuel_category.add(fuel_name);
                        //flag = true;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                fuelSpinner.setAdapter(fuelAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(FuelActivity.this,"No Cars found",Toast.LENGTH_SHORT).show();
            }
        }){

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
                Toast.makeText(FuelActivity.this,"No Cars found",Toast.LENGTH_SHORT).show();
            }
        }){

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void close(View v){
        this.finish();
    }





    private void postAllTrip3(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting data to server.Please wait...");
        progressDialog.show();

        String URL = AppConstants.FUEL_API;
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
                        Log.d("====x=", "..xx..>>" );
                     //   int size = datasource.getAllComments().size();
//                        if(x == datasource.getAllComments().size()-1) {
//                            for (int i = 0; i < size; i++) {
//                                tripData = (TripData) datasource.getAllComments().remove(0); //(TripData) getListAdapter().getItem(0);
//                                Log.d("====id="+tripData.getId(), "..i..>>" + i+"--"+tripData.getEndKM());
//                                datasource.deleteComment(tripData);
//                            }
//                          //  adapter = new RequestDetailsActivity.TripAdapter(RequestDetailsActivity.this);
//                         //   lv.setAdapter(adapter);
//                            //lv.invalidate();
//                        }
//							datasource.deleteComment(tripData);
                        // adapter.remove(tripData);
                        //lv.invalidate();
                        //	}else{
                        Toast.makeText(FuelActivity.this,response,Toast.LENGTH_LONG).show();
                        //	}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FuelActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<String,String>();
             //   TripData t = datasource.getAllComments().get(x);
                carno = spinner.getSelectedItem().toString();
               Log.d("@@@@ car no ---","+carno");

             //   data.put("AllocationId", SharedPreferencesHelper.getAllocationid(con));
                //data.put("StaffID", SharedPreferencesHelper.getStaff(con));

                //data.put("StaffID", start_place);
                //data.put("CarNo", start_place);

                data.put("TransactionDate", start_date);
                data.put("CarNo",carno);
                data.put("KM", start_km);
                data.put("FuelConsumpt", fuel_consumption);
                data.put("FuelStationID", ""+fuel_in);
                data.put("TransactionMode", radio_id);
                data.put("StaffID", staff);
//
//                data.put("StaffID", t.getStaff_id().toString());

                Log.d(".response--.", ".."+data);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_month);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
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

                        startDate.setText(year + "-" + b + "-" + c);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
