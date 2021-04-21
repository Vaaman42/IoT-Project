package com.aureliendekesel.iot_project.Models;

import android.content.Intent;
import android.view.View;

import com.aureliendekesel.iot_project.Controllers.ButtonSettingsActivity;
import com.aureliendekesel.iot_project.Controllers.MainActivity;

public class OnActionButtonLongClickListener implements View.OnLongClickListener {

    private ActionButton actionButton;

    public OnActionButtonLongClickListener(ActionButton actionButton) {
        this.actionButton = actionButton;
    }

    @Override
    public boolean onLongClick(View v) {
        System.out.println("onLongClick");
        Intent intent = new Intent(MainActivity.Instance, ButtonSettingsActivity.class);
        intent.putExtra(ButtonSettingsActivity.ACTION, actionButton);
        MainActivity.Instance.startActivityForResult(intent, ButtonSettingsActivity.SETTINGS);
        return true;
    }


}
