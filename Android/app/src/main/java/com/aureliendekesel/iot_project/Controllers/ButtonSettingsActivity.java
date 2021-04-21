package com.aureliendekesel.iot_project.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aureliendekesel.iot_project.Models.ActionButton;
import com.aureliendekesel.iot_project.Models.ScaledBitmap;
import com.aureliendekesel.iot_project.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

public class ButtonSettingsActivity extends AppCompatActivity {
    public static final String ACTION = "ACTION";
    public static final int SETTINGS = 0;
    public static final int RESULT_LOAD_IMG = 1;
    private ActionButton m_ActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_settings);

        m_ActionButton = (ActionButton) getIntent().getSerializableExtra(ACTION);
        System.out.println("Linked Id: " + m_ActionButton.getLinkedId() + " Color: R=" + red(m_ActionButton.getColor())+ " G=" + green(m_ActionButton.getColor()) + " B=" + blue(m_ActionButton.getColor()));
        if (m_ActionButton.getLinkedId() != -1)
            ((EditText)findViewById(R.id.actionID)).setText(m_ActionButton.getLinkedId() + "");

        ((EditText)findViewById(R.id.actionID)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String val = ((EditText)findViewById(R.id.actionID)).getText().toString();
                if (!val.isEmpty())
                    m_ActionButton.setLinkedId(Integer.parseInt(val));
                else
                    m_ActionButton.setLinkedId(-1);
            }
        });

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(ACTION, m_ActionButton);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        ((SeekBar)findViewById(R.id.barRed)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_ActionButton.setRed(progress);
                ((TextView)findViewById(R.id.textRed)).setText(progress+"");
                ((ImageButton)findViewById(R.id.buttonPreview)).setBackgroundColor(m_ActionButton.getColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ((SeekBar)findViewById(R.id.barGreen)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_ActionButton.setGreen(progress);
                ((TextView)findViewById(R.id.textGreen)).setText(progress+"");
                ((ImageButton)findViewById(R.id.buttonPreview)).setBackgroundColor(m_ActionButton.getColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ((SeekBar)findViewById(R.id.barBlue)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_ActionButton.setBlue(progress);
                ((TextView)findViewById(R.id.textBlue)).setText(progress+"");
                ((ImageButton)findViewById(R.id.buttonPreview)).setBackgroundColor(m_ActionButton.getColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((SeekBar)findViewById(R.id.barRed)).setProgress(red(m_ActionButton.getColor()));
        ((SeekBar)findViewById(R.id.barGreen)).setProgress(green(m_ActionButton.getColor()));
        ((SeekBar)findViewById(R.id.barBlue)).setProgress(blue(m_ActionButton.getColor()));

        (findViewById(R.id.buttonPreview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        if (m_ActionButton.getImage() != null)
            ((ImageButton)findViewById(R.id.buttonPreview)).setImageBitmap(ScaledBitmap.scale(m_ActionButton.getImage(), 300,300));
        ((ImageButton)findViewById(R.id.buttonPreview)).setBackgroundColor(m_ActionButton.getColor());

        findViewById(R.id.buttonRemoveImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageButton)findViewById(R.id.buttonPreview)).setImageResource(0);
                m_ActionButton.setImage(null);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap scaledImage = ScaledBitmap.scale(selectedImage,300, 300);
                ((ImageButton)findViewById(R.id.buttonPreview)).setImageBitmap(scaledImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                scaledImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                m_ActionButton.setImage(image);
            } catch (FileNotFoundException e) {

            }
        }
    }
}