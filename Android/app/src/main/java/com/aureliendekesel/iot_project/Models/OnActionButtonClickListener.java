package com.aureliendekesel.iot_project.Models;

import android.view.View;
import android.widget.Toast;

import com.aureliendekesel.iot_project.Controllers.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class OnActionButtonClickListener implements View.OnClickListener {

    private ActionButton m_ActionButton;

    public OnActionButtonClickListener(ActionButton actionButton){
        m_ActionButton = actionButton;
    }

    @Override
    public void onClick(View v) {
        if (m_ActionButton.getLinkedId() == -1) {
            Toast.makeText(MainActivity.Instance.getApplicationContext(), "Button not assigned!\nLong press to enter settings", Toast.LENGTH_LONG).show();
        }
        else try {
            DatagramSocket ds = new DatagramSocket(MainActivity.Instance.Port, MainActivity.Instance.Connection.getIpAddress());
            byte[] message = new String("SPPR-" + m_ActionButton.getLinkedId() + MainActivity.Instance.Connection.getDeviceID()).getBytes();
            DatagramPacket packet = new DatagramPacket(message, message.length);
            ds.send(packet);
        } catch (Exception e) {
            Toast.makeText(MainActivity.Instance.getApplicationContext(), "Connection Error!\nCould not send action", Toast.LENGTH_LONG).show();
        } finally {

        }
    }
}
