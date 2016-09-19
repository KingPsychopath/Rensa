package com.edasaki.rensa.utils;

public class SakiUtils {
    public static String formatMillis(long timeMillis) {
        long timeSeconds = timeMillis / 1000;
        long days = timeSeconds / 60 / 60 / 24;
        long hours = timeSeconds / 60 / 60 % 24;
        long mins = timeSeconds / 60 % 60;
        long seconds = timeSeconds % 60;
        StringBuilder sb = new StringBuilder();
        boolean hasDays = days > 0;
        boolean hasHours = hours > 0;
        boolean hasMins = mins > 0;
        boolean hasSeconds = seconds > 0;
        if (!(hasSeconds || hasMins || hasDays || hasHours))
            return "0 seconds";
        if (hasDays) {
            sb.append(days);
            sb.append(" day");
            if (days != 1)
                sb.append('s');
        }
        if (hasHours) {
            if (hasDays) {
                if (hasMins || hasSeconds)
                    sb.append(", ");
                else
                    sb.append(" and ");
            }
            sb.append(hours);
            sb.append(" hour");
            if (hours != 1)
                sb.append('s');
        }
        if (hasMins) {
            if (hasDays || hasHours) {
                if (hasSeconds)
                    sb.append(", ");
                else
                    sb.append(" and ");
            }
            sb.append(mins);
            sb.append(" minute");
            if (mins != 1)
                sb.append('s');
        }
        if (hasSeconds) {
            if (hasDays || hasHours || hasMins)
                sb.append(" and ");
            sb.append(seconds);
            sb.append(" second");
            if (seconds != 1)
                sb.append('s');
        }
        return sb.toString();
    }

    public static String formatMinutes(long timeMinutes) {
        long days = timeMinutes / 60 / 24;
        long hours = timeMinutes / 60 % 24;
        long mins = timeMinutes % 60;
        StringBuilder sb = new StringBuilder();
        boolean hasDays = days > 0;
        boolean hasHours = hours > 0;
        boolean hasMins = mins > 0;
        if (hasDays) {
            sb.append(days);
            sb.append(" day");
            if (days != 1)
                sb.append('s');
        }
        if (hasHours) {
            if (hasDays) {
                if (hasMins)
                    sb.append(", ");
                else
                    sb.append(" and ");
            }
            sb.append(hours);
            sb.append(" hour");
            if (hours != 1)
                sb.append('s');
        }
        if (hasMins) {
            if (hasDays || hasHours)
                sb.append(" and ");
            sb.append(mins);
            sb.append(" minute");
            if (mins != 1)
                sb.append('s');
        } else {
            if (!(hasMins || hasDays || hasHours))
                return "0 minutes";
        }
        return sb.toString();
    }
}
