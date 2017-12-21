package tfn.android.lollipops.contract;

import tfn.lollipops.library.presenter.BasePresenter;
import tfn.lollipops.library.view.BaseView;

/**
 * Created by tangfunan on 2017/12/21.
 */

public interface SplashPageContract {

    interface View extends BaseView<Presenter>{
        void loginOrMain();
    }

    interface Presenter extends BasePresenter{

    }


}
