package com.aureliendekesel.iot_project.Models;

import android.view.View;
import android.widget.Toast;

import com.aureliendekesel.iot_project.Controllers.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class OnConnectionClickListener implements View.OnClickListener {

    private Connection m_Connection;
    private ConnectionActivity m_Activity;

    public OnConnectionClickListener(Connection connection, ConnectionActivity activity) {
        m_Connection = connection;
        m_Activity = activity;
    }

    @Override
    public void onClick(View v) {
        MainActivity.Instance.Connection = m_Connection;
        String toastText = "Successfully connected!";
        try {
            DatagramSocket ds = new DatagramSocket(MainActivity.Instance.Port, m_Connection.getIpAddress());
            byte[] message = new String("SPOK-" + m_Connection.getDeviceName() + "-" + 1 + "-" + m_Connection.getDeviceID()).getBytes();
            DatagramPacket packet = new DatagramPacket(message, message.length);
            ds.send(packet);
        } catch (Exception e) {
            toastText = "Error connecting to the selected device...";
        } finally {
            m_Activity.finish();
            Toast.makeText(MainActivity.Instance.getApplicationContext(), toastText, Toast.LENGTH_LONG);
        }
    }


}
