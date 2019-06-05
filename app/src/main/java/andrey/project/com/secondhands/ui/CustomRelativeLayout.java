package andrey.project.com.secondhands.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by procreationsmac on 24/07/2018.
 */

public class CustomRelativeLayout extends RelativeLayout {

    private boolean touchDisabled = true;

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    public void disableTouch(boolean b) {
        touchDisabled = b;
    }
}
