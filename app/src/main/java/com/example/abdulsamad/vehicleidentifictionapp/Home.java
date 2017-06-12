package com.example.abdulsamad.vehicleidentifictionapp;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
public class Home extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    ProgressDialog dialog;
    AlertDialog.Builder alertDialog;
    EditText vin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mScannerView = new ZXingScannerView(this);
        vin=(EditText)findViewById(R.id.vinumber);
        initHttpsProtocolPermission();
        initDialog();
    }
    private void initDialog(){
        alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Internal Error Occur Please try again later");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog=new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please wait.....");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
    ////////////////////////////////////////////////////////////////////
    //networking methods
    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void initHttpsProtocolPermission() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
    public void loadArticles(String vin)
    {
     //   https://vpic.nhtsa.dot.gov/api/vehicles/DecodeVinValues/JM1GJ1W59G1461903?format=json&modelyear="+2016
        dialog.show();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("vpic.nhtsa.dot.gov")
                .appendPath("api")
                .appendPath("vehicles")
                .appendPath("DecodeVinValues")
                .appendPath(vin)
                .appendQueryParameter("format","json")
                .appendQueryParameter("modelyear","2016");
        String url=builder.build().toString();
        System.out.print("url="+url);
        JsonObjectRequest R=new JsonObjectRequest(Request.Method.GET, url, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onResponse:", response.toString());
                dialog.hide();
                parsejson(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(error.toString(), "onErrorResponse: ");
                        dialog.hide();
                        alertDialog.show();
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(R);
    }
    private void parsejson(JSONObject json)
    {
        finish();
        Report.json=json;
        startActivity(new Intent(this,Report.class));
    }
    /////////////////////////////////////////////////////////////////////
    //view methods
    public void scan(View view)
    {
        QrScanner();
    }
    public void search(View view)
    {
        if (vin.getText().length()==0) {
            Toast.makeText(this, "Please Enter or Scan VIN Number", Toast.LENGTH_SHORT).show();
        }else {
            loadArticles(vin.getText().toString());
        }
    }
    //////////////////////////////////////////////////////////////////
    //scanner methods
    public void handleResult(Result rawResult) {
        Toast.makeText(this, rawResult.getText().toString(), Toast.LENGTH_SHORT).show();
        mScannerView.removeAllViews();
        mScannerView.stopCamera();
        setContentView(R.layout.activity_home);
        vin=(EditText)findViewById(R.id.vinumber);
        vin.setText(rawResult.getText().toString());
        // If you would like to resume scanning, call this method below:<br />
        // mScannerView.resumeCameraPreview(this);<br />
    }
    public void QrScanner(){   // Programmatically initialize the scanner view<br />
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.<br />
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
        } else {
            mScannerView.startCamera();
        }
        // Start camera<br />
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mScannerView.startCamera();
            }
        }
    }
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause<br />
    }
}