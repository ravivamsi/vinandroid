package com.example.abdulsamad.vehicleidentifictionapp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;
public class Report extends AppCompatActivity {
    public static JSONObject json;
    TextView make,model,modelyear,fuelprimary,plantcity,plantcompanyname,plantcountry,plantstate,manufacturer,bodyclass,displacCC,vehicletype,VIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        make=(TextView)findViewById(R.id.make);
        model=(TextView)findViewById(R.id.model);
        modelyear=(TextView)findViewById(R.id.modelyear);
        fuelprimary=(TextView)findViewById(R.id.fueltypeprimary);
        plantcity=(TextView)findViewById(R.id.plantcity);
        plantcompanyname=(TextView)findViewById(R.id.plantcompanyname);
        plantcountry=(TextView)findViewById(R.id.plantcountry);
        plantstate=(TextView)findViewById(R.id.plantstate);
        manufacturer=(TextView)findViewById(R.id.manufacturer);
        bodyclass=(TextView)findViewById(R.id.bodyclass);
        vehicletype=(TextView)findViewById(R.id.vehicletype);
        displacCC=(TextView)findViewById(R.id.displacementcc);
        VIN=(TextView)findViewById(R.id.vin);
        setupData();
    }
    private void setupData()
    {
        try
        {
            if (json.getString("Message").equalsIgnoreCase("Results returned successfully"))
            {
                JSONArray results=json.getJSONArray("Results");
                JSONObject obj=results.getJSONObject(0);
                if (obj.getString("Make").isEmpty())
                    make.setText("Not Data found");
                else
                    make.setText(obj.getString("Make"));
                if (obj.getString("Model").isEmpty())
                    model.setText("No Data found");
                else
                    model.setText(obj.getString("Model"));
                if (obj.getString("ModelYear").isEmpty())
                    modelyear.setText("No Data found");
                else
                    modelyear.setText(obj.getString("ModelYear"));
                if (obj.getString("FuelTypePrimary").isEmpty())
                    fuelprimary.setText("No Data found");
                else
                    fuelprimary.setText(obj.getString("FuelTypePrimary"));
                if (obj.getString("PlantCity").isEmpty())
                    plantcity.setText("No Data found");
                else
                    plantcity.setText(obj.getString("PlantCity"));
                if (obj.getString("PlantCompanyName").isEmpty())
                    plantcompanyname.setText("No Data found");
                else
                    plantcompanyname.setText(obj.getString("PlantCompanyName"));
                if (obj.getString("PlantCountry").isEmpty())
                    plantcountry.setText("No Data found");
                else
                    plantcountry.setText(obj.getString("PlantCountry"));
                if (obj.getString("PlantState").isEmpty())
                    plantstate.setText("No Data found");
                else
                    plantstate.setText(obj.getString("PlantState"));
                if (obj.getString("Manufacturer").isEmpty())
                    manufacturer.setText("No Data found");
                else
                    manufacturer.setText(obj.getString("Manufacturer"));
                if (obj.getString("BodyClass").isEmpty())
                    bodyclass.setText("No Data found");
                else
                    bodyclass.setText(obj.getString("BodyClass"));
                if (obj.getString("DisplacementCC").isEmpty())
                    displacCC.setText("No Data found");
                else
                    displacCC.setText(obj.getString("DisplacementCC"));
                if (obj.getString("VIN").isEmpty())
                    VIN.setText("No Data found");
                else
                    VIN.setText(obj.getString("VIN"));
                if (obj.getString("VehicleType").isEmpty())
                    vehicletype.setText("No Data found");
                else
                    vehicletype.setText(obj.getString("VehicleType"));
            }
        }catch (Exception ex)
        {
            Log.d("error", "setupData: ");
        }
    }
    public void gobacktohome(View view)
    {
        finish();
        startActivity(new Intent(this,Home.class));
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,Home.class));
    }
}