package com.fuagra.dmb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_NAME = "Default";
    public static final String START_DATE_KEY = "StartDate";
    private static final String END_DATE_KEY = "EndDate";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        long startDate = preferences.getLong(START_DATE_KEY, 0);
        long endDate = preferences.getLong(END_DATE_KEY, 0);

        if (startDate == 0 || endDate == 0) {
            //TODO DatePickerDialog

            try {
                startDate = simpleDateFormat.parse("23/11/2016").getTime();
                endDate =  simpleDateFormat.parse("23/11/2017").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            preferences.edit().putLong(START_DATE_KEY, startDate).apply();
            preferences.edit().putLong(END_DATE_KEY, endDate).apply();
        }

        long currentDate = System.currentTimeMillis();
        int remainingDaysCount = (int) ((endDate - currentDate) / (3600 * 24 * 1000));
        TextView textView = (TextView) findViewById(R.id.dayLabel);
        textView.setText(remainingDaysCount + " days left");
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.dayProgress);
        progressBar.setIndeterminate(false);
        progressBar.setMax(365);
        progressBar.setProgress(365 - remainingDaysCount);

        textView = (TextView) findViewById(R.id.datesLabel);
        textView.setText("Start date: "
                + simpleDateFormat.format(new Date(startDate)) + "\n" +
                "End date: " + simpleDateFormat.format(new Date(endDate)));

        int percentage  = Math.round(100-(remainingDaysCount/365f)*100);
        textView = (TextView) findViewById(R.id.dayPercentageLabel);
        textView.setText(percentage + "%");
    }
}
