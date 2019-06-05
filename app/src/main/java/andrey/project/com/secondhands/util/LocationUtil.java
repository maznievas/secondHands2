package andrey.project.com.secondhands.util;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocationUtil {

    private final String TAG_DISTANCE = "distance";
    private final String TAG_ANGLE_START = "angle_start";
    private final String TAG_ANGLE_RAD = "angle_rad";
    private final String TAG_ANGLE = "angle_degree";
    private final String TAG_QUATER = "quater";

    @Inject
    public LocationUtil(){}

    public int detectDirection(LatLng cameraCoords, LatLng shopCoords) {
        double baseX = cameraCoords.longitude;
        double baseY = cameraCoords.latitude;
        double secondPointX = shopCoords.longitude;
        double secondPointY = shopCoords.latitude;
        boolean firstQuater = false, secondQuater = false, thirdquater = false,
                fourthQuater = false;

        double distanceWidth = 0, distanceHeight = 0;
        if ((secondPointX >= 0 && baseX >= 0) || (secondPointX <= 0 && baseX <= 0))
            distanceWidth = Math.abs(Math.abs(secondPointX) - Math.abs(baseX));
        //todo if one of coords bigger than 0 and another one - less

        if ((secondPointY >= 0 && baseY >= 0) || (secondPointY <= 0 && baseY <= 0))
            distanceHeight = Math.abs(Math.abs(secondPointY) - Math.abs(baseY));

         Log.d(TAG_DISTANCE, "distanceWidth: " + distanceWidth + " distanceHeight: " + distanceHeight);

        double angleRad = Math.atan(distanceHeight / distanceWidth);
        double angleDegree = Math.toDegrees(angleRad);

        Log.d(TAG_ANGLE_START, "angle degree: " + angleDegree);
        Log.d(TAG_ANGLE_RAD, "angle rad: " + angleRad);

        //detecting the quater of point
        if (secondPointX >= baseX && secondPointY >= baseY)
        {
            firstQuater = true;
            Log.d(TAG_QUATER, "firstQuater");
        }
        else if (secondPointX <= baseX && secondPointY >= baseY) {
            secondQuater = true;
            Log.d(TAG_QUATER, "secondQuater");
        }
        else if (secondPointX <= baseX && secondPointY <= baseY)
        {
            thirdquater = true;
            Log.d(TAG_QUATER, "thirdquater");
        }
        else if (secondPointX >= baseX && secondPointY <= baseY)
        {
            fourthQuater = true;
            Log.d(TAG_QUATER, "fourthQuater");
        }

        double angle = 0;

        //calc angle based on quater
        if(firstQuater){
            angle = angleDegree;
        }
        else if(secondQuater){
            angle = 180 - angleDegree;
        }
        else if (thirdquater)
            angle = 180 + angleDegree;
        else if(fourthQuater)
            angle = 360 - angleDegree;

        Log.d(TAG_ANGLE, "angle degree: " + angle);

        if(angle > 315 && angle < 45)
            return Const.Direction.EAST;
        else if(angle >= 45 && angle <= 135)
            return Const.Direction.NORTH;
        else if(angle > 135 && angle <= 225)
            return Const.Direction.WEST;
        else if(angle > 225 && angle <= 315)
            return Const.Direction.SOUTH;
        else return 1;
    }
}
