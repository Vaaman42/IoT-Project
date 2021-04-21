package com.aureliendekesel.iot_project.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.InputStream;
import java.io.Serializable;

public class ActionButton implements Serializable {

    // The ID sent to the Desktop App
    private byte[] m_Image;
    // = -1 if not assigned
    private int m_LinkedId;

    //The RGB Values for the Color
    private int m_ColorRed;
    private int m_ColorGreen;
    private int m_ColorBlue;

    //Button Location
    private int m_Page;
    private int m_Row;
    private int m_Column;

    public ActionButton(int page, int row, int column) {
        m_LinkedId = -1;

        m_ColorRed = 255;
        m_ColorGreen = 255;
        m_ColorBlue = 255;

        m_Page = page;
        m_Row = row;
        m_Column = column;
    }

    public void setLinkedId(int newId) {
        m_LinkedId = newId;
    }

    public int getLinkedId() {
        return m_LinkedId;
    }

    public void setColor(int red, int green, int blue) {
        m_ColorRed = red > 255? 255 : Math.max(red, 0);
        m_ColorGreen = green > 255? 255 : Math.max(green, 0);
        m_ColorBlue = blue > 255? 255 : Math.max(blue, 0);
    }

    public void setRed(int red) {
        setColor(red, m_ColorGreen, m_ColorBlue);
    }
    public void setGreen(int green) {
        setColor(m_ColorRed, green, m_ColorBlue);
    }
    public void setBlue(int blue) {
        setColor(m_ColorRed, m_ColorGreen, blue);
    }

    // Returns a Color int.
    // Use red(int), green(int), blue(int) and alpha(int) to get the ARGB Values
    public int getColor() {
        return Color.rgb(m_ColorRed, m_ColorGreen, m_ColorBlue);
    }

    public int getPage() {  return m_Page;  }
    public int getRow() {  return m_Row;  }
    public int getColumn() {  return m_Column;  }

    public Bitmap getImage() {
        if (m_Image != null)
            return BitmapFactory.decodeByteArray(m_Image, 0, m_Image.length);
        return null;
    }
    public void setImage(byte[] image) {
        m_Image = image;
    }
}
