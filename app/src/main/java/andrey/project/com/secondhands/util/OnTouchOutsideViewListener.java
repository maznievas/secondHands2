package andrey.project.com.secondhands.util;

/**
 * Created by procreationsmac on 23/07/2018.
 */

import android.view.MotionEvent;
import android.view.View;

/**
 * Interface definition for a callback to be invoked when a touch event has occurred outside a formerly specified
 * view.
 */
public interface OnTouchOutsideViewListener {

    /**
     * Called when a touch event has occurred outside a given view.
     *
     * @param view  The view that has not been touched.
     * @param event The MotionEvent object containing full information about the event.
     */
    public void onTouchOutside(View view, MotionEvent event);
}