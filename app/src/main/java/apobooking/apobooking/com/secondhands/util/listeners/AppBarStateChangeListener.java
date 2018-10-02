package apobooking.apobooking.com.secondhands.util.listeners;

import android.support.design.widget.AppBarLayout;

/**
 * Created by andrey on 01.10.18.
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE,
        COLLAPSING,
        EXPANDING
    }

    private State mCurrentState = State.IDLE;
    private int previousPosition = 123456789;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            detectDirection(i);
            previousPosition = i;
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    private void detectDirection(int i) {
//        if(previousPosition != ) {
//            if (previousPosition < i)
//                changeStateManually(State.EXPANDING);
//            else
//                changeStateManually(State.COLLAPSING);
//        }
    }

    public abstract void changeStateManually(State state);
    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
