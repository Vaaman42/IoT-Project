package com.aureliendekesel.iot_project.Models;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.InetAddress;

public class Connection {

    private InetAddress m_IpAddress;
    private int m_DeviceID;
    private String m_DeviceName;

    public Connection(InetAddress ip, int deviceId, String deviceName ) {
        m_IpAddress = ip;
        m_DeviceID = deviceId;
        m_DeviceName = deviceName;
    }

    public int getDeviceID() {
        return m_DeviceID;
    }

    public InetAddress getIpAddress() {
        return m_IpAddress;
    }

    public String getDeviceName() {
        return m_DeviceName;
    }

    public View getView(final Context context) {
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        TextView DeviceName = new TextView(context);
        DeviceName.setText(m_DeviceName);
        DeviceName.setTextSize(20);
        view.addView(DeviceName);
        TextView IpAddress = new TextView(context);
        IpAddress.setText(m_IpAddress.toString());
        IpAddress.setTextSize(12);
        view.addView(IpAddress);
        view.setPadding(0,5,0,10);
        return view;
    }
}
