package com.example.muhammadashfaq.eatit.Pakage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.muhammadashfaq.eatit.AddResturant;
import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Model.AddResturantModel;
import com.example.muhammadashfaq.eatit.R;
import com.example.muhammadashfaq.eatit.RecentlyAddedResActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenResturant  extends Service implements ChildEventListener
{
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    public void onCreate() {
        super.onCreate();
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Resturants");

    }

    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        AddResturantModel request=dataSnapshot.getValue(AddResturantModel.class);
        showNotification(dataSnapshot.getKey(),request);

    }

    private void showNotification(String key, AddResturantModel request) {
        Intent intent=new Intent(getBaseContext(), RecentlyAddedResActivity.class);
        //intent.putExtra("userPhone",request.getPhone());
        PendingIntent pendingIntent= PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //NotificatinN
        NotificationCompat.Builder nBuilder=new NotificationCompat.Builder(getBaseContext());
        nBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis())
                .setTicker("ChaiePaani").setContentTitle("ChaiePaani").setContentText("Your Add Resturant Request is "+
                Common.convertResturatCode(" "+request.getStatus()+" ")+"").setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher);


        NotificationManager notificationManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,nBuilder.build());


    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
