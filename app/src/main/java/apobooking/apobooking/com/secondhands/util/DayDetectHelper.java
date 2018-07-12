package apobooking.apobooking.com.secondhands.util;

import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.SecondHandApplication;

/**
 * Created by procreationsmac on 03/07/2018.
 */

public class DayDetectHelper {

    public static int detectDay(int day)
    {
        switch(day)
        {
            case 0:
                return R.string.monday;
            case 1:
                return R.string.tuesday;
            case 2:
                return R.string.wednesday;
            case 3:
                return R.string.thursday;
            case 4:
                return R.string.friday;
            case 5:
                return R.string.saturday;
            case 6:
                return R.string.sunday;
            default:
                return R.string.sunday;
        }
    }

    public static int detectDay(String day)
    {
        switch(day)
        {
            case "Monday":
                return 0;
            case "Tuesday":
                return 1;
            case "Wednesday":
                return 2;
            case "Thursday":
                return 3;
            case "Friday":
                return 4;
            case "Saturday":
                return 5;
            case "Sunday":
                return 6;
            default:
                return 7;
        }
    }
}
