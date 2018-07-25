package apobooking.apobooking.com.secondhands.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by procreationsmac on 24/07/2018.
 */

public class CustomCoordinatorLayout extends CoordinatorLayout {

    private boolean touchDisabled = true;

    public CustomCoordinatorLayout(Context context) {
        super(context);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return touchDisabled;
    }

    public void disableTouch(boolean b) {
        touchDisabled = b;
    }
}
