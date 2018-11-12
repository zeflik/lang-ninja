package pl.jozefniemiec.langninja.ui.main.send;

public class SendPresenterImpl implements SendPresenter {

    SendFragmentView view;

    public SendPresenterImpl(SendFragmentView view) {
        this.view = view;
    }

    @Override
    public void onViewCreated() {
        view.showData();
    }

    @Override
    public void onViewVisible() {
        view.listenForNewData();
    }

    @Override
    public void onViewInvisible() {
        view.stopListenForNewData();
    }
}
