package eu.virtusdevelops.holoextension.utils;

import java.text.DecimalFormat;

public class TextUtils {


    public static String formatDecimals(Double number){
        DecimalFormat dformater = new DecimalFormat("###.##");

        String formated = dformater.format(number);

        return formated;

    }

    public static String formatNumbers(Double number){
        DecimalFormat dformater = new DecimalFormat("###,###,###,###,###.###");
        return dformater.format(number);
    }

    public static String formatNames(Double number){
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E', 'Z', 'Y'};
        double numValue = number;
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,###.##").format(numValue);
        }
    }

    public static String formatValue(int format, double value){

        switch(format){
            case 1: return formatDecimals(value);
            case 2: return formatNumbers(value); // STOP
            case 3: return formatNames(value);
            default: return ""+value;
        }
    }

    public static String formatTime(double time) {
        final int days = (int)(time / 86400L);
        time -= 86400 * days;
        final int hours = (int)(time / 3600L);
        time -= 3600 * hours;
        final int minutes = (int)(time / 60L);
        time -= 60 * minutes;
        final int seconds = (int)time;
        final StringBuilder sb = new StringBuilder();

        if (days != 0) {
            sb.append(days).append("d ");
        }
        if (hours != 0) {
            sb.append(hours).append("h ");
        }
        if (minutes != 0) {
            sb.append(minutes).append("m ");
        }
        sb.append(seconds).append("s ");


        return sb.toString().trim();
    }
}
