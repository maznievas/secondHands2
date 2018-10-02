package apobooking.apobooking.com.secondhands.util.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.Behavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by andrey on 02.10.18.
 */

public class DisableableAppBarLayoutBehavior extends Behavior {
    private boolean mEnabled;

    public DisableableAppBarLayoutBehavior() {
        super();
    }

    public DisableableAppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return mEnabled && super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    public boolean isEnabled() {
        return mEnabled;
    }
}
