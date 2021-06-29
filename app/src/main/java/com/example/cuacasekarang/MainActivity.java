package com.example.cuacasekarang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cuacasekarang.api.ApiService;
import com.example.cuacasekarang.model.ModelCuaca;
import com.example.cuacasekarang.model.ModelWaktu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private final String TAG = "MainActivity";
    private final String APIKEY = "7f8e3b7e1c4ec0e9293195102356009e";
    private final String LANG = "id";
    private final String ImagaeURL = "https://openweathermap.org/img/wn/";

    private List<ModelCuaca.weather> results = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;
    ModelWaktu getDate = new ModelWaktu();
    TextView tvWaktu,tvLokasi,tvMain,tvDescription,tvSuhu,tvHumi;
    ImageView ivIcon;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        tvWaktu = findViewById(R.id.tvWaktu);
        tvLokasi = findViewById(R.id.tvLokasi);
        tvMain = findViewById(R.id.tvMain);
        tvDescription = findViewById(R.id.tvDescription);
        tvSuhu = findViewById(R.id.tvSuhu);
        tvHumi = findViewById(R.id.tvHumi);
        ivIcon = findViewById(R.id.ivIcon);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        tvWaktu.setText(getDate.getDateNow("E, dd MMMM"));

        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this
                ,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            getCurrentLocation();
            showLoading(true);

        }else {
            ActivityCompat.requestPermissions(MainActivity.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                            ,Manifest.permission.ACCESS_COARSE_LOCATION}
                    ,100);
        }


//        getCurrentLocation();


    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {

//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(getApplicationContext(), "Permession denied", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if(location != null){
                        try {
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String lat = String.valueOf(location.getLatitude());
                            String lon = String.valueOf(location.getLongitude());
                            String kecamatan = addresses.get(0).getLocality();
                            getApi(lat, lon);
                            tvLokasi.setText(kecamatan);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                try {
                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    String lat = String.valueOf(location1.getLatitude());
                                    String lon = String.valueOf(location1.getLongitude());
                                    String kecamatan = addresses.get(0).getLocality();
                                    getApi(lat, lon);
                                    tvLokasi.setText(kecamatan);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest
                                , locationCallback, Looper.myLooper());

                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void getApi(String lat, String lon){
        ApiService.endpoint().getData(lat, lon, APIKEY, LANG)
                .enqueue(new Callback<ModelCuaca>() {
                    @Override
                    public void onResponse(Call<ModelCuaca> call, Response<ModelCuaca> response) {
                        Log.d(TAG, response.toString());
                        if(response.isSuccessful()){
                            ArrayList<ModelCuaca.weather> results = response.body().getWeather();
                            ModelCuaca.Main mlcuaca = response.body().getMain();
                            showLoading(false);
                            _weather(results);
                            _mains(mlcuaca);
                            Log.d(TAG, mlcuaca.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelCuaca> call, Throwable t) {

                    }
                });
    }

    private void _weather(List<ModelCuaca.weather> modelCuacas){
        ModelCuaca.weather res = modelCuacas.get(0);
        tvDescription.setText(res.getDescription());
        tvMain.setText(res.getMain());
        Glide.with(this).load(ImagaeURL + res.getIcon() + "@4x.png").into(ivIcon);
    }

    private void _mains(ModelCuaca.Main mlcuaca){

        double suhu = Math.round(mlcuaca.getTemp() - 273.15);
        tvSuhu.setText(String.valueOf(suhu) + "°c");
        tvHumi.setText(String.valueOf(mlcuaca.getHumidity()) + " %");
    }


    private void showLoading(Boolean loading){
        if (loading) {
//            pbLoad.setVisibility(View.VISIBLE);
            progressDialog.setMessage("Mendapatkan lokasi");
            progressDialog.show();
        } else {
            progressDialog.dismiss();
//            pbLoad.setVisibility(View.GONE);
        }
    }

}