package com.example.muhammadashfaq.eatit.Pakage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Model.Request;
import com.example.muhammadashfaq.eatit.Order;
import com.example.muhammadashfaq.eatit.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOrder extends Service implements ChildEventListener
{
    FirebaseDatabase database;
    DatabaseReference requests;

    public ListenOrder() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

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
        Request request=dataSnapshot.getValue(Request.class);
        showNotification(dataSnapshot.getKey(),request);

    }

    private void showNotification(String key, Request request) {
        Intent intent=new Intent(getBaseContext(), Order.class);
        intent.putExtra("userPhone",request.getPhone());
        PendingIntent pendingIntent= PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //NotificatinN
        NotificationCompat.Builder nBuilder=new NotificationCompat.Builder(getBaseContext());
        nBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis())
                .setTicker("EatIt").setContentTitle("EatIt").setContentText("Order status updated to "+
                Common.convertCodeToStatus(" "+request.getStatus())+" ").setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher);


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
