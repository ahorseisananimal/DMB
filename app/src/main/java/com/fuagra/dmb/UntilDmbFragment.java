package com.fuagra.dmb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class UntilDmbFragment extends Fragment {
    public static final String SHARED_PREFERENCES_NAME = "Default";
    public static final String START_DATE_KEY = "StartDate";
    private static final String END_DATE_KEY = "EndDate";
    private SharedPreferences preferences;
    private Handler handler;
    private SimpleDateFormat simpleDateFormat;
    private boolean isActive;
    private ImageView image;
    private ImageView imageSpeaking;


    public static Fragment newInstance() {
        return new UntilDmbFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.until_f, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        handler = new Handler();

        image = (ImageView) view.findViewById(R.id.soldier);
        imageSpeaking = (ImageView) view.findViewById(R.id.soldierSpeaking);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSpeaking.animate().scaleX(1.6f).setInterpolator(new AccelerateDecelerateInterpolator()).scaleY(1.6f).alpha(1).start();
                image.animate().scaleX(1.3f).setInterpolator(new AccelerateDecelerateInterpolator()).scaleY(1.3f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        image.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(1).scaleY(1).start();
                        imageSpeaking.animate().scaleX(1).setInterpolator(new AccelerateDecelerateInterpolator()).scaleY(1).alpha(0).start();
                    }
                }).start();

            }
        });

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                image.animate().rotation(15).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        image.animate().rotation(-15).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                image.animate().rotation(0).start();
                            }
                        }).start();
                    }
                }).start();
                return true;
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
        update();
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }

    private void update() {
        if (!isActive) {
            Log.d("DMB", "Update has been stopped");
            return;
        }
        preferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
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

        TextView textViewDates = (TextView) getView().findViewById(R.id.datesLabel);
        textViewDates.setText("Start date: "
                + simpleDateFormat.format(new Date(startDate)) + "\n" +
                "End date: " + simpleDateFormat.format(new Date(endDate)));

        long currentDate = System.currentTimeMillis();
        int remainingDaysCount = (int) ((endDate - currentDate) / (3600 * 24 * 1000));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentDate));

        TextView textViewRemainingDays = (TextView) getView().findViewById(R.id.remainingDaysLabel);
        textViewRemainingDays.setText((remainingDaysCount - 1) + " days left");

        ProgressBar progressBarYear = (ProgressBar) getView().findViewById(R.id.yearProgressLabel);
        progressBarYear.setIndeterminate(false);
        progressBarYear.setMax(365);
        progressBarYear.setProgress(365 - remainingDaysCount);

        int percentageYear = Math.round(100 - (remainingDaysCount / 365f) * 100);
        TextView textViewYearPercentage = (TextView) getView().findViewById(R.id.yearPercentageLabel);
        textViewYearPercentage.setText(percentageYear + "%");


        int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);
        int currentSeconds = calendar.get(Calendar.SECOND);

        TextView textViewRemainingHours = (TextView) getView().findViewById(R.id.remainingHoursLabel);
        textViewRemainingHours.setText((23 - currentHours) + " hours left");

        ProgressBar progressBarDay = (ProgressBar) getView().findViewById(R.id.dayProgressLabel);
        progressBarDay.setIndeterminate(false);
        progressBarDay.setMax(24 * 3600 * 1000);
        progressBarDay.setProgress((currentHours * 3600 + currentMinutes * 60 + currentSeconds) * 1000);

        int percentageDay = Math.round((currentHours * 3600 + currentMinutes * 60 + currentSeconds) / (24f * 3600) * 100);
        TextView textViewDayPercentage = (TextView) getView().findViewById(R.id.dayPercentageLabel);
        textViewDayPercentage.setText(percentageDay + "%");


        TextView textViewRemainingMinutes = (TextView) getView().findViewById(R.id.remainingMinutesLabel);
        textViewRemainingMinutes.setText((59 - currentMinutes) + " minutes left");

        ProgressBar progressBarHour = (ProgressBar) getView().findViewById(R.id.hourProgressLabel);
        progressBarHour.setIndeterminate(false);
        progressBarHour.setMax(3600 * 1000);
        progressBarHour.setProgress((currentMinutes * 60 + currentSeconds) * 1000);

        int percentageHour = Math.round((currentMinutes * 60 + currentSeconds) / 3600f * 100);
        TextView textViewHourPercentage = (TextView) getView().findViewById(R.id.hourPercentageLabel);
        textViewHourPercentage.setText(percentageHour + "%");

        TextView textViewRemainingSeconds = (TextView) getView().findViewById(R.id.remainingSecondsLabel);
        textViewRemainingSeconds.setText((60 - currentSeconds) + " seconds left");

        ProgressBar progressBarMinute = (ProgressBar) getView().findViewById(R.id.minuteProgressLabel);
        progressBarMinute.setIndeterminate(false);
        progressBarMinute.setMax(60 * 1000);
        progressBarMinute.setProgress(currentSeconds * 1000);

        int percentageMinute = Math.round(currentSeconds / 60f * 100);
        TextView textViewMinutePercentage = (TextView) getView().findViewById(R.id.minutePercentageLabel);
        textViewMinutePercentage.setText(percentageMinute + "%");


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }, 200);
    }

}
