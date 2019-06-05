package andrey.project.com.secondhands.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public final class FlingBehavior extends AppBarLayout.Behavior {

    private static final int TOP_CHILD_FLING_THRESHOLD = 3;
    private boolean isPositive;
    private boolean enabled = true;

    public FlingBehavior() {
        setDragCallback();
    }

    public FlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDragCallback();
    }

    private void setDragCallback() {
        setDragCallback(new DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return enabled;
            }
        });
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout,
                                 AppBarLayout child, View target, float
                                         velocityX, float velocityY, boolean
                                         consumed) {
        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
            velocityY = velocityY * -1;
        }
        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        isPositive = dy > 0;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return enabled && super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
