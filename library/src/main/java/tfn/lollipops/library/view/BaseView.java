package tfn.lollipops.library.view;

/**
 * MVP V层基类，所有View都需要实现此接口
 *
 * Created by tangfunan on 2017/12/19.
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

}
