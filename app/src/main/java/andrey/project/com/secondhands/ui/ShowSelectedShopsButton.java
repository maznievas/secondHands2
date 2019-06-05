package andrey.project.com.secondhands.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import andrey.project.com.secondhands.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by procreationsmac on 04/07/2018.
 */

public class ShowSelectedShopsButton extends RelativeLayout {


    @BindView(R.id.arrowDownImage)
    ImageView arrowDown;

    @BindView(R.id.titleTextView)
    TextView titleTextView;

    private boolean down;
    private Animation animUp, animDown, animFadeIn, animFadeOut;
    private Context context;

    public ShowSelectedShopsButton(Context context) {
        super(context);
        init(context);
    }

    public ShowSelectedShopsButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShowSelectedShopsButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context)
    {
        this.context = context;

        View view = inflate(context, R.layout.show_selected_shops_button, this);
        ButterKnife.bind(this, view);

        down = false;
        animUp = AnimationUtils.loadAnimation(getContext(), R.anim.arrow_anim_up);
        animUp.setFillAfter(true);
        animDown = AnimationUtils.loadAnimation(getContext(), R.anim.arrow_anim_down);
        animDown.setFillAfter(true);
        animFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        animFadeIn.setFillAfter(true);
        animFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        animFadeOut.setFillAfter(true);
    }

    public void updateView()
    {
        titleTextView.startAnimation(animFadeIn);
        if(down) {
            arrowDown.startAnimation(animUp);
            titleTextView.setText(context.getString(R.string.hide_selected_shops));
        }
        else {
            arrowDown.startAnimation(animDown);
            titleTextView.setText(context.getString(R.string.show_all_shops));
        }
        titleTextView.startAnimation(animFadeOut);
    }

    public void changeState() {
        down = !down;
        updateView();
    }

    public void setInactive()
    {
        if(down) {
            down = false;
            updateView();
        }
    }

    public boolean getArrowDown()
    {
        return down;
    }
}
