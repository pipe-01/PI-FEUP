package pt.feup.cosn.monitoring.Utils;

import java.time.LocalDateTime;

public class DateTime {
    private static DateTime DateTimeInstance = null;

    public static DateTime getInstance()
    {
        if (DateTimeInstance == null)
            DateTimeInstance = new DateTime();

        return DateTimeInstance;
    }

    public static LocalDateTime stringToLocalDateTime(String timestamp){
        String[] split = timestamp.trim().split("_");

        if(split.length != 6){
            return null;
        }

        int yr = Integer.parseInt(split[0]);
        int mon = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        int hr = Integer.parseInt(split[3]);
        int min = Integer.parseInt(split[4]);
        int sec = Integer.parseInt(split[5]);

        return LocalDateTime.of(yr, mon, day, hr, min, sec);
    }
}
