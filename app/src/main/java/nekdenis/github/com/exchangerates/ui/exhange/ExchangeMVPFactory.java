package nekdenis.github.com.exchangerates.ui.exhange;

public class ExchangeMVPFactory {

    public static ExchangeInterfaces.ExchangePresenterInterface initPresenter() {
        return new ExchangePresenter();
    }

    public static ExchangeInterfaces.ExchangeModelInterface initModel(ExchangeInterfaces.ExchangePresenterCallback presenterCallback) {
        return new ExchangeModel(presenterCallback);
    }
}
