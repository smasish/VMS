package scibd.lab.vms;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class FuelActivity extends AppCompatActivity implements View.OnClickListener {


    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private Button reqBtn,checkBtn,travelBtn,maintainBtn;

    private Context con;


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



//        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_monthlayout, getData());
//        gridView.setAdapter(gridAdapter);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
//
//                //Create intent
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
////                intent.putExtra("title", item.getTitle());

////
//                int mon = position+1;
//                intent.putExtra("pos",mon);
//                // intent.putExtra("pos",position);
//                //Log.d("====monthview====", "...month position..>>" + mon);
//
//                //Start details activity
//                startActivity(intent);
//            }
//        });



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
