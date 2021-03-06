package com.example.anton.subscribetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    int count = 0;
    ArrayList<String> checkBoxesTextTemp;
    ArrayList<String> checkBoxesEmailTemp;
    boolean[] checkBoxesStateTemp;
    ArrayList<String> checkBoxesText = new ArrayList<>();
    ArrayList<String> checkBoxesEmail= new ArrayList<>();
    boolean[] checkBoxesState = new boolean[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void BtnSubscribeClick(View v) {
        LinearLayout chb_layout = (LinearLayout) findViewById(R.id.chb_layout);
        Editable checkBoxText = ((EditText) findViewById(R.id.name_entry)).getText();
        Editable checkBoxEmail = ((EditText) findViewById(R.id.email_entry)).getText();
        if (!"".equals(checkBoxText.toString()) && !"".equals(checkBoxEmail.toString())) {
            ((EditText) findViewById(R.id.email_entry)).setText("");
            ((EditText) findViewById(R.id.name_entry)).setText("");
            CreateCheckBoxes(chb_layout, count, checkBoxText, checkBoxEmail, false);
            count++;
            Toast.makeText(this, String.format("E-mail %s successfully subscribed!", checkBoxEmail),
                    Toast.LENGTH_SHORT).show();
        } else {
            if ("".equals(checkBoxText.toString())) {
                Toast.makeText(this, "Incorrect name!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect email!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void CreateCheckBoxes(LinearLayout chb_layout, int id, Editable checkBoxText,
                                 Editable checkBoxEmail, boolean state) {
        CheckBox checkBox = new CheckBox(getApplicationContext());
        registerForContextMenu(checkBox);
        checkBox.setText(checkBoxText);
        checkBox.setChecked(state);
        checkBox.setId(id);
        checkBoxesEmail.add(checkBoxEmail.toString());
        checkBoxesText.add(checkBoxText.toString());
        checkBoxesState = Arrays.copyOf(checkBoxesState, checkBoxesState.length + 1);
        checkBoxesState[id] = checkBox.isChecked();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxesState[view.getId()]) {
                    checkBoxesState[view.getId()] = Boolean.FALSE;
                } else {
                    checkBoxesState[view.getId()] = Boolean.TRUE;
                }
            }
        });
        chb_layout.addView(checkBox);
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("count", count);
        outState.putStringArrayList("chb_text", checkBoxesText);
        outState.putBooleanArray("chb_state", checkBoxesState);
        outState.putStringArrayList("chb_email", checkBoxesEmail);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);
        count = saveInstanceState.getInt("count");
        checkBoxesEmail = saveInstanceState.getStringArrayList("chb_email");
        checkBoxesState = saveInstanceState.getBooleanArray("chb_state");
        checkBoxesText = saveInstanceState.getStringArrayList("chb_text");
        if (checkBoxesText != null) {
            checkBoxesTextTemp = checkBoxesText;
        }
        if (checkBoxesState != null) {
            checkBoxesStateTemp = checkBoxesState;
        }
        if (checkBoxesEmail != null) {
            checkBoxesEmailTemp = checkBoxesEmail;
        }

        checkBoxesEmail = new ArrayList<>();
        checkBoxesState = new boolean[0];
        checkBoxesText = new ArrayList<>();

        for (int i = 0 ; i < count; i++) {
            CreateCheckBoxes((LinearLayout) findViewById(R.id.chb_layout), i,
                    new SpannableStringBuilder(checkBoxesTextTemp.get(i)),
                    new SpannableStringBuilder(checkBoxesEmailTemp.get(i)), checkBoxesStateTemp[i]);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {

        LinearLayout chb_layout = (LinearLayout) findViewById(R.id.chb_layout);
        chb_layout.removeAllViews();
        boolean[] temp = new boolean[0];
        ArrayList<String> temp_text = new ArrayList<>();
        ArrayList<String> temp_email = new ArrayList<>();
        ArrayList<String> unsubscribed_emails = new ArrayList<>();

        for (int i = 0; i < checkBoxesState.length; i++) {
            if (checkBoxesState[i]) {
                count--;
                unsubscribed_emails.add(checkBoxesEmail.get(i));
            } else {
                temp = Arrays.copyOf(temp, temp.length + 1);
                temp[temp.length - 1] = checkBoxesState[i];
                temp_text.add(checkBoxesText.get(i));
                temp_email.add(checkBoxesEmail.get(i));
            }
        }

        checkBoxesState = temp;
        checkBoxesText = temp_text;
        checkBoxesEmail = temp_email;

        checkBoxesTextTemp = checkBoxesText;
        checkBoxesStateTemp = checkBoxesState;
        checkBoxesEmailTemp = checkBoxesEmail;

        checkBoxesState = new boolean[0];
        checkBoxesText = new ArrayList<>();
        checkBoxesEmail = new ArrayList<>();

        for (int i = 0; i < checkBoxesStateTemp.length; i++) {
            CreateCheckBoxes(chb_layout, i, new SpannableStringBuilder(checkBoxesTextTemp.get(i)),
                    new SpannableStringBuilder(checkBoxesEmailTemp.get(i)), checkBoxesStateTemp[i]);
        }

        if (unsubscribed_emails.size() == 1) {
            Toast.makeText(this, String.format("E-mail %s unsubscribed successfully successfully!",
                    unsubscribed_emails.get(0)), Toast.LENGTH_SHORT).show();
        } else if (unsubscribed_emails.size() > 1) {
            Toast.makeText(this, "E-mails unsubscribed successfully successfully!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nothing selected!", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

}
