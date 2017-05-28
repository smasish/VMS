package scibd.lab.vms;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import scibd.lab.vms.utils.SharedPreferencesHelper;

public class FuelActivity extends AppCompatActivity implements View.OnClickListener {


    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private Button reqBtn,checkBtn,travelBtn,maintainBtn;

    private Context con;
    private boolean tripflag=true;
    private Spinner spinner;
    private boolean submitflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel);

        con =this;

        reqBtn = (Button)findViewById(R.id.btnReg);
        reqBtn.setOnClickListener(this);

        checkBtn = (Button)findViewById(R.id.btnChecklist);
        checkBtn.setOnClickListener(this);
        travelBtn = (Button)findViewById(R.id.btAppTravel);

        submitflag = false;

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
                          //  adapter = new RequestDetailsActivity.TripAdapter(RequestDetailsActivity.this);
                         //   lv.setAdapter(adapter);
                            //lv.invalidate();
                        }
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

                data.put("StaffID", t.getStaff_id().toString());

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReg:
                // TODO Auto-generated method stub

               // Intent req = new Intent(MainActivity.this, RequestActivity.class);
                Intent req = new Intent(FuelActivity.this, InfoActivity.class);

                startActivity(req);


                break;
            case R.id.btnChecklist:
                // TODO Auto-generated method stub
                Intent check = new Intent(FuelActivity.this, ChecklistActivity.class);

                startActivity(check);
                break;
            case R.id.btAppTravel:
                // TODO Auto-generated method stub
                break;
            case R.id.btAppVehicle:
            default:
                // TODO Auto-generated method stub
                break;
        }
    }
}
