package com.fuagra.dmb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_NAME = "Default";
    public static final String START_DATE_KEY = "StartDate";
    private static final String END_DATE_KEY = "EndDate";
    private SharedPreferences preferences;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        handler = new Handler();

        update(simpleDateFormat);


    }

    private void update(final SimpleDateFormat simpleDateFormat) {
        preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        long startDate = preferences.getLong(START_DATE_KEY, 0);
        long endDate = preferences.getLong(END_DATE_KEY, 0);

        if (startDate == 0 || endDate == 0) {
            //TODO DatePickerDialog

            try {
                startDate = simpleDateFormat.parse("23/11/2016").getTime();
                endDate = simpleDateFormat.parse("23/11/2017").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            preferences.edit().putLong(START_DATE_KEY, startDate).apply();
            preferences.edit().putLong(END_DATE_KEY, endDate).apply();
        }

        TextView textViewDates = (TextView) findViewById(R.id.datesLabel);
        textViewDates.setText("Start date: "
                + simpleDateFormat.format(new Date(startDate)) + "\n" +
                "End date: " + simpleDateFormat.format(new Date(endDate)));

        long currentDate = System.currentTimeMillis();
        int remainingDaysCount = (int) ((endDate - currentDate) / (3600 * 24 * 1000));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentDate));

        TextView textViewRemainingDays = (TextView) findViewById(R.id.remainingDaysLabel);
        textViewRemainingDays.setText(remainingDaysCount + " days left");

        ProgressBar progressBarDays = (ProgressBar) findViewById(R.id.dayProgressLabel);
        progressBarDays.setIndeterminate(false);
        progressBarDays.setMax(365);
        progressBarDays.setProgress(365 - remainingDaysCount);

        int percentage = Math.round(100 - (remainingDaysCount / 365f) * 100);
        TextView textViewDayPercentage = (TextView) findViewById(R.id.dayPercentageLabel);
        textViewDayPercentage.setText(percentage + "%");


        int progressMinute = calendar.get(Calendar.SECOND);
        TextView textViewRemainingSeconds = (TextView) findViewById(R.id.remainingSecondsLabel);
        textViewRemainingSeconds.setText((60-progressMinute) + " seconds left");

        ProgressBar progressBarMinutes = (ProgressBar) findViewById(R.id.minuteProgressLabel);
        progressBarMinutes.setIndeterminate(false);
        progressBarMinutes.setMax(60);
        progressBarMinutes.setProgress(progressMinute);

        int percentageMinute = Math.round(progressMinute / 60f * 100);
        TextView textViewMinutePercentage = (TextView) findViewById(R.id.minutePercentageLabel);
        textViewMinutePercentage.setText(percentageMinute + "%");


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                update(simpleDateFormat);
            }
        }, 200);
    }


}

