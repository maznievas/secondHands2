package apobooking.apobooking.com.secondhands.mainFragment;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import apobooking.apobooking.com.secondhands.SecondHandApplication;
import io.reactivex.disposables.CompositeDisposable;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainFragmentView> {

    private CompositeDisposable compositeDisposable;

    public MainPresenter() {
        compositeDisposable = new CompositeDisposable();
        SecondHandApplication.getAppComponent().inject(this);

    }
}
