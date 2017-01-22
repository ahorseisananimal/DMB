package com.fuagra.dmb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
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
    private SimpleDateFormat simpleDateFormat;
    private boolean isActive;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        handler = new Handler();

        image = (ImageView) findViewById(R.id.soldier);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.animate().scaleX(1.3f).setInterpolator(new AccelerateDecelerateInterpolator()).scaleY(1.3f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        image.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(1).scaleY(1).start();
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    private void update() {
        if (!isActive){
            Log.d("DMB", "Update has been stopped");
            return;
        }
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
        textViewRemainingDays.setText((remainingDaysCount - 1) + " days left");

        ProgressBar progressBarYear = (ProgressBar) findViewById(R.id.yearProgressLabel);
        progressBarYear.setIndeterminate(false);
        progressBarYear.setMax(365);
        progressBarYear.setProgress(365 - remainingDaysCount);

        int percentageYear = Math.round(100 - (remainingDaysCount / 365f) * 100);
        TextView textViewYearPercentage = (TextView) findViewById(R.id.yearPercentageLabel);
        textViewYearPercentage.setText(percentageYear + "%");


        int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
        TextView textViewRemainingHours = (TextView) findViewById(R.id.remainingHoursLabel);
        textViewRemainingHours.setText((23 - currentHours) + " hours left");

        ProgressBar progressBarDay = (ProgressBar) findViewById(R.id.dayProgressLabel);
        progressBarDay.setIndeterminate(false);
        progressBarDay.setMax(24);
        progressBarDay.setProgress(currentHours);

        int percentageDay = Math.round(currentHours / 24f * 100);
        TextView textViewDayPercentage = (TextView) findViewById(R.id.dayPercentageLabel);
        textViewDayPercentage.setText(percentageDay + "%");


        int currentMinutes = calendar.get(Calendar.MINUTE);
        TextView textViewRemainingMinutes = (TextView) findViewById(R.id.remainingMinutesLabel);
        textViewRemainingMinutes.setText((59 - currentMinutes) + " minutes left");

        ProgressBar progressBarHour = (ProgressBar) findViewById(R.id.hourProgressLabel);
        progressBarHour.setIndeterminate(false);
        progressBarHour.setMax(60);
        progressBarHour.setProgress(currentMinutes);

        int percentageHour = Math.round(currentMinutes / 60f * 100);
        TextView textViewHourPercentage = (TextView) findViewById(R.id.hourPercentageLabel);
        textViewHourPercentage.setText(percentageHour + "%");

        int currentSeconds = calendar.get(Calendar.SECOND);
        TextView textViewRemainingSeconds = (TextView) findViewById(R.id.remainingSecondsLabel);
        textViewRemainingSeconds.setText((60 - currentSeconds) + " seconds left");

        ProgressBar progressBarMinute = (ProgressBar) findViewById(R.id.minuteProgressLabel);
        progressBarMinute.setIndeterminate(false);
        progressBarMinute.setMax(60);
        progressBarMinute.setProgress(currentSeconds);

        int percentageMinute = Math.round(currentSeconds / 60f * 100);
        TextView textViewMinutePercentage = (TextView) findViewById(R.id.minutePercentageLabel);
        textViewMinutePercentage.setText(percentageMinute + "%");


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }, 200);
    }


}

