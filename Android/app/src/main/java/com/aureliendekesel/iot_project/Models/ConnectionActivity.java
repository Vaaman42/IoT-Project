package com.aureliendekesel.iot_project.Models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aureliendekesel.iot_project.Controllers.MainActivity;
import com.aureliendekesel.iot_project.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionActivity extends AppCompatActivity {
    private ConnectionActivity Instance;
    public MyAsyncTask checkConnections;
    public List<Connection> AvailableConnections = new ArrayList<Connection>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        Instance = this;
        checkConnections = new MyAsyncTask(Instance);
        checkConnections.execute();

    }

    public void addView(Connection connection) {
        AvailableConnections.add(connection);
        View view = connection.getView(Instance);
        ((LinearLayout)findViewById(R.id.listDevices)).addView(view);
        view.setOnClickListener(new OnConnectionClickListener(connection, this));
    }

    public void onPostExecute() {
        checkConnections.cancel(true);
        checkConnections = new MyAsyncTask(Instance);
        checkConnections.execute();
    }
    public void Debug(String str) {
        TextView view = new TextView(this);
        view.setText(str);
        ((LinearLayout)findViewById(R.id.listDevices)).addView(view);
    }
}