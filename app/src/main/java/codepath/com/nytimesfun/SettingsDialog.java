package codepath.com.nytimesfun;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsDialog extends DialogFragment {

    public interface onFinishedListener {
        public void onFinished(@SettingsDialog.SortOrder int sortOrder, @CheckboxOptions.CHOICES int checkboxOptions);
    };

    Calendar curDate;

    @IntDef({NONE, NEWEST, OLDEST})
    public @interface SortOrder {}

    public static final int NONE = 0;
    public static final int NEWEST = 1;
    public static final int OLDEST = 2;

    public static String SORT_ORDER = "sort_order";
    public static String CHECKBOX_OPTIONS = "cb_options";
    public static String BEGIN_DATE = "begin_date";

    @Bind(R.id.spinner)
    Spinner mSortOrder;

    @Bind(R.id.cbArts)
    CheckBox mCbArts;

    @Bind(R.id.cbFashion)
    CheckBox mCbFashion;

    @Bind(R.id.cbSports)
    CheckBox mCbSports;

    @Bind(R.id.btnSave)
    Button save;

    @Bind(R.id.beginDate)
    EditText beginDate;

    public SettingsDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SettingsDialog newInstance() {
        SettingsDialog frag = new SettingsDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_settings, container);
        ButterKnife.bind(this, view);

        return view;

    }

    @OnClick(R.id.beginDate)
    public void showDatePickerFragment(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();

        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }


    public @SortOrder int getSpinnerSelectedValue() {
        int pos = mSortOrder.getSelectedItemPosition();
        switch (pos) {
            case 1:
                return OLDEST;
            case 2:
                return NEWEST;
            default:
                return NONE;
        }
    }

    public @CheckboxOptions.CHOICES int getChoices() {
        int flag = 0;

        if (mCbArts.isSelected()) {
            flag |= CheckboxOptions.ARTS;
        }

        if (mCbFashion.isSelected()) {
            flag |= CheckboxOptions.FASHION;
        }

        if (mCbSports.isSelected()) {
            flag |= CheckboxOptions.SPORTS;
        }
        return flag;
    }

    @OnClick(R.id.btnSave)
    public void onSave() {
        onFinishedListener listener = (onFinishedListener) getActivity();
        listener.onFinished(getSpinnerSelectedValue(), getChoices());
        dismiss();
    }

    public void setDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy  ");
        beginDate.setText(dateFormat.format(date));
    }


}
