package com.techndevs.trackingapp.Services;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techndevs.trackingapp.R;

import java.util.HashMap;

public class TrackerService extends Service
{

    DatabaseReference dbRef;
    String currentUID;

    
    private static final String TAG = TrackerService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbRef = FirebaseDatabase.getInstance().getReference("LocationUpdates");
        currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        buildNotification();
        requestLocationUpdates();
        //loginToFirebase();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void buildNotification()
    {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager;
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelId);
            notificationChannel.setSound(null, null);

            // Create the persistent notification
            notificationManager.createNotificationChannel(notificationChannel);
            Notification notification = new Notification.Builder(this, channelId)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Tracking")
                    .setOngoing(true)
                    .setContentIntent(broadcastIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .build();
            startForeground(1, notification);
        }else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.app_name))
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Tracking")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(1,builder.build());
        }

    }


    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        // FirebaseApp.initializeApp(this);

        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>()
        {

            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "firebase auth success");
                    requestLocationUpdates();
                } else {
                    Log.d(TAG, "firebase auth failed");
                }
            }
        });
    }


    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(2000);
        request.setFastestInterval(500);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        //final String path = getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        //final String path = "https://tracking-app-81674.firebaseio.com/";
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (permission == PackageManager.PERMISSION_GRANTED)
        {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "Location Update " + location);
                        HashMap<String,String> hashMap = new HashMap<>();

                        String latitude = String.valueOf(location.getLatitude());
                        String longitude = String.valueOf(location.getLongitude());
                        String accuracy = String.valueOf(location.getAccuracy());
                        String altitude = String.valueOf(location.getAltitude());
                        String extras = String.valueOf(location.getExtras());
                        String speed = String.valueOf(location.getSpeed());

                        hashMap.put("latitude",latitude);
                        hashMap.put("longitude",longitude);
                        hashMap.put("accuracy",accuracy);
                        hashMap.put("altitude",altitude);
                        hashMap.put("extras",extras);
                        hashMap.put("speed",speed);

                        if(currentUID!=null){
                            dbRef.child(currentUID).setValue(hashMap);
                        }

                    }
                }
            }, null);
        }
    }


    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy()");
        super.onDestroy();
    }
}
