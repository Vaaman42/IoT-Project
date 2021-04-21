package com.aureliendekesel.iot_project.Models;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.aureliendekesel.iot_project.Controllers.MainActivity;
import com.aureliendekesel.iot_project.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class MyAsyncTask extends AsyncTask <Void,Void,Void>{

    private static final int MAX_UDP_DATAGRAM_LEN = 1500;

    private ConnectionActivity m_Instance;

    public MyAsyncTask(ConnectionActivity instance) {
        m_Instance = instance;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        byte[] message = new byte[MAX_UDP_DATAGRAM_LEN];
        try {
            DatagramPacket packet = new DatagramPacket(message, message.length);
            DatagramSocket ds = new DatagramSocket(MainActivity.Instance.Port);
            ds.receive(packet);
            String[] SPDI = new String(message).split("-");

            if (SPDI[0].contains("SPDI")) {
                boolean skip = false;
                Connection detectedConnection = new Connection(InetAddress.getByName(SPDI[1]), Integer.parseInt(SPDI[3]), SPDI[5]);
                for (Connection c : m_Instance.AvailableConnections) {
                    if (detectedConnection.getDeviceID() == c.getDeviceID()) {
                        skip = true;
                        break;
                    }
                }
                if (!skip) {
                    m_Instance.addView(detectedConnection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        m_Instance.onPostExecute();
    }
}
