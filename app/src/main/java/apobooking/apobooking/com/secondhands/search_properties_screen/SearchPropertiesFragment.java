package apobooking.apobooking.com.secondhands.search_properties_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import apobooking.apobooking.com.secondhands.MainActivity;
import apobooking.apobooking.com.secondhands.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchPropertiesFragment extends MvpAppCompatFragment implements SearchPropertiesView {

    @InjectPresenter
    SearchPropertiesPresenter searchPropertiesPresenter;

    private Unbinder unbinder;

    public static SearchPropertiesFragment newInstance() {
        return new SearchPropertiesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_properties, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        searchPropertiesPresenter.clear();
    }

    @OnClick(R.id.applyButton)
    public void onApplyClicked()
    {
        ((MainActivity)getActivity()).openMap();
    }
}
