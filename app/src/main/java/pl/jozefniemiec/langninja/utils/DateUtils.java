package pl.jozefniemiec.langninja.utils;

import android.content.Context;

import pl.jozefniemiec.langninja.R;

public class DateUtils {

    private static final int SECONDS_DENOMINATOR = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int ONE_MINUTE = 1;
    private static final int FIVES_MINUTES = 5;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int MINUTES_IN_TWO_HOURS = 120;
    private static final int MINUTES_IN_FIVES_HOURS = 300;
    private static final int MINUTES_IN_DAY = 1440;
    private static final int MINUTES_IN_TWO_DAYS = 2880;
    private static final int MINUTES_IN_MONTH = 43200;
    private static final int MINUTES_IN_TWO_MONTHS = 86400;
    private static final int MINUTES_IN_FIVE_MONTHS = 172800;
    private static final int MINUTES_IN_ONE_YEAR = 525600;
    private static final int MINUTES_IN_TWO_YEARS = 1051200;
    private static final int MINUTES_IN_FIVE_YEARS = 2102400;

    public static String generateTimePeriodDescription(Long timestamp, Context context) {

        int timeDistanceInMinutes = getTimeDistanceInMinutes(timestamp);

        if (timeDistanceInMinutes <= 0) {
            return context.getResources().getString(R.string.date_util_now);
        } else if (timeDistanceInMinutes == ONE_MINUTE) {
            return context.getResources().getString(R.string.date_util_unit_minute);
        } else if (timeDistanceInMinutes < FIVES_MINUTES) {
            return timeDistanceInMinutes + " "
                    + context.getResources().getString(R.string.date_util_unit_minutes_to_4);
        } else if (timeDistanceInMinutes < MINUTES_IN_HOUR) {
            return timeDistanceInMinutes + " "
                    + context.getResources().getString(R.string.date_util_unit_minutes_from_5);
        } else if (timeDistanceInMinutes < MINUTES_IN_TWO_HOURS) {
            return context.getResources().getString(R.string.date_util_unit_hour);
        } else if (timeDistanceInMinutes < MINUTES_IN_FIVES_HOURS) {
            return (Math.round(timeDistanceInMinutes / MINUTES_IN_HOUR)) + " "
                    + context.getResources().getString(R.string.date_util_unit_hours_to_4);
        } else if (timeDistanceInMinutes < MINUTES_IN_DAY) {
            return (Math.round(timeDistanceInMinutes / MINUTES_IN_HOUR)) + " "
                    + context.getResources().getString(R.string.date_util_unit_hours_from_5);
        } else if (timeDistanceInMinutes < MINUTES_IN_TWO_DAYS) {
            return context.getResources().getString(R.string.date_util_unit_day);
        } else if (timeDistanceInMinutes < MINUTES_IN_MONTH) {
            return (Math.round(timeDistanceInMinutes / MINUTES_IN_DAY)) + " "
                    + context.getResources().getString(R.string.date_util_unit_days);
        } else if (timeDistanceInMinutes < MINUTES_IN_TWO_MONTHS) {
            return context.getResources().getString(R.string.date_util_unit_month);
        } else if (timeDistanceInMinutes < MINUTES_IN_FIVE_MONTHS) {
            return (Math.round(timeDistanceInMinutes / MINUTES_IN_MONTH)) + " "
                    + context.getResources().getString(R.string.date_util_unit_months_to_4);
        } else if (timeDistanceInMinutes < MINUTES_IN_ONE_YEAR) {
            return (Math.round(timeDistanceInMinutes / MINUTES_IN_MONTH)) + " "
                    + context.getResources().getString(R.string.date_util_unit_months_from_5);
        } else if (timeDistanceInMinutes < MINUTES_IN_TWO_YEARS) {
            return context.getResources().getString(R.string.date_util_unit_year);
        } else if (timeDistanceInMinutes <= MINUTES_IN_FIVE_YEARS) {
            return (Math.round(timeDistanceInMinutes / MINUTES_IN_ONE_YEAR)) + " "
                    + context.getResources().getString(R.string.date_util_unit_years_to_4);
        } else {
            return (Math.round(timeDistanceInMinutes / MINUTES_IN_ONE_YEAR)) + " "
                    + context.getResources().getString(R.string.date_util_unit_years_from_5);
        }
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = System.currentTimeMillis() - time;
        return Math.round((Math.abs(timeDistance) / SECONDS_DENOMINATOR) / SECONDS_IN_MINUTE);
    }
}
