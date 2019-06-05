package andrey.project.com.secondhands.mainFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatFragment;

import java.util.Arrays;

import andrey.project.com.secondhands.R;
import andrey.project.com.secondhands.ui.CustomSpinnerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainFragment extends MvpAppCompatFragment {

    private Unbinder unbinder;
    private CustomSpinnerAdapter updateDaySpinnerAdapter;

    @BindView(R.id.citySpinner)
    Spinner sCity;

    @BindView(R.id.shopNameSpinner)
    Spinner sShopName;

    @BindView(R.id.updateDaySpinner)
    Spinner sUpdateDay;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        String[] updateDayList = getResources().getStringArray(R.array.days_of_week);
        updateDaySpinnerAdapter = new CustomSpinnerAdapter(getContext(), Arrays.asList(updateDayList));
        sUpdateDay.setAdapter(updateDaySpinnerAdapter);
    }




}
