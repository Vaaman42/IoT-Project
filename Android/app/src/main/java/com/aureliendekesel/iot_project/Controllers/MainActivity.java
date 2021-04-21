package com.aureliendekesel.iot_project.Controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aureliendekesel.iot_project.Models.ActionButton;
import com.aureliendekesel.iot_project.Models.Connection;
import com.aureliendekesel.iot_project.Models.ConnectionActivity;
import com.aureliendekesel.iot_project.Models.OnActionButtonClickListener;
import com.aureliendekesel.iot_project.Models.OnActionButtonLongClickListener;
import com.aureliendekesel.iot_project.Models.ScaledBitmap;
import com.aureliendekesel.iot_project.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    public static MainActivity Instance;
    public Connection Connection;
    public int Port = 17017;
    private int m_CurrentPage;
    private File m_SaveFile;

    private final int NUMBER_OF_PAGES = 10;
    private final int NUMBER_OF_ROWS = 5;
    private final int NUMBER_OF_COLUMNS = 4;

    // m_ActionButtons[Page][Row][Column]
    private ActionButton[][][] m_ActionButtons;

    private TableLayout m_RootTableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Instance == null) Instance = this;

        m_SaveFile = new File(getApplicationContext().getFilesDir(), "savefile.txt");
        if (m_SaveFile.exists()) {
            try {
                LoadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            m_ActionButtons = new ActionButton[NUMBER_OF_PAGES][NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
            for (int page = 0; page < NUMBER_OF_PAGES; page++) {
                for (int row = 0; row < NUMBER_OF_ROWS; row++) {
                    for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
                        m_ActionButtons[page][row][column] = new ActionButton(page, row, column);
                    }
                }
            }
            try {
                m_SaveFile.createNewFile();
                SaveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        findViewById(R.id.button_arrow_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePage(m_CurrentPage+1);
            }
        });

        findViewById(R.id.button_arrow_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePage(m_CurrentPage-1);
            }
        });

        findViewById(R.id.button_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(Instance, ConnectionActivity.class);
                 Instance.startActivity(intent);
            }
        });

        m_RootTableLayout = findViewById(R.id.root_table_layout);
        ChangePage(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RESULT_OK == " + (resultCode == Activity.RESULT_OK));

        if (resultCode == Activity.RESULT_OK) {
            ActionButton actionButton = (ActionButton) data.getSerializableExtra(ButtonSettingsActivity.ACTION);
            m_ActionButtons[actionButton.getPage()][actionButton.getRow()][actionButton.getColumn()] = actionButton;
            ChangePage(m_CurrentPage);
            try {
                SaveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ChangePage(int newPage) {
        m_CurrentPage = newPage == NUMBER_OF_PAGES ? 0 : newPage == -1? NUMBER_OF_PAGES-1 : newPage;
        ((TextView)findViewById(R.id.text_page)).setText((m_CurrentPage+1) + "/" + NUMBER_OF_PAGES);
        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            TableRow currentRow = (TableRow) m_RootTableLayout.getChildAt(row);
            for (int column = 0; column < NUMBER_OF_COLUMNS; column++) {
                UpdateButton((ImageButton) currentRow.getChildAt(column), m_ActionButtons[m_CurrentPage][row][column]);
            }
        }
    }

    private void UpdateButton(ImageButton btn, ActionButton action) {
        btn.setOnClickListener(new OnActionButtonClickListener(action));
        btn.setOnLongClickListener(new OnActionButtonLongClickListener(action));
        btn.setBackgroundColor(action.getColor());
        if (action.getImage() != null) {
            btn.setImageBitmap(ScaledBitmap.scale(action.getImage(), 150,150));
        }
        else {
            btn.setImageResource(0);
        }
    }

    private void LoadFile() throws IOException {
        ObjectInputStream objStream = new ObjectInputStream(new FileInputStream(m_SaveFile.getPath()));
        try {
            m_ActionButtons = (ActionButton[][][]) objStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void SaveFile() throws IOException {
        ObjectOutputStream objStream = new ObjectOutputStream(new FileOutputStream(m_SaveFile.getPath()));
        objStream.writeObject(m_ActionButtons);
        objStream.close();
    }
}