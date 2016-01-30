package codepath.com.nytimesfun;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    EditText beginDate;
    Calendar curDate;

    DateFormat dateFormatGmt = DateFormat.getDateInstance(DateFormat.SHORT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        beginDate = (EditText) findViewById(R.id.beginDate);
        curDate = Calendar.getInstance();

        beginDate.setText(dateFormatGmt.format(curDate.getTime()));
    }

    public void showTimePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "timePicker");

/*        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timepicker");*/
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        curDate.set(Calendar.YEAR, year);
        curDate.set(Calendar.MONTH, monthOfYear);
        curDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        beginDate.setText(dateFormatGmt.format(curDate.getTime()));
    }

}
