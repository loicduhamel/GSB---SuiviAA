package fr.obiedzynski.suivaa;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        int heure = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(),heure,minute,android.text.format.DateFormat.is24HourFormat(getActivity()));
    }


}