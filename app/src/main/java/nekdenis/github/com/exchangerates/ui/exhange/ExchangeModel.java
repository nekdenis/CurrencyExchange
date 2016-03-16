package nekdenis.github.com.exchangerates.ui.exhange;

import android.util.Log;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import nekdenis.github.com.exchangerates.api.Api;
import nekdenis.github.com.exchangerates.data.ExchangeRates;
import nekdenis.github.com.exchangerates.data.ExchangeRatesConverter;
import nekdenis.github.com.exchangerates.data.response.exchangerates.ExchangeRatesResponse;
import nekdenis.github.com.exchangerates.di.Injector;
import nekdenis.github.com.exchangerates.util.Constants;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class ExchangeModel implements ExchangeInterfaces.ExchangeModelInterface {

    protected Api service;

    private Subscription exchangeRateSubscription;

    private ExchangeInterfaces.ExchangePresenterCallback presenterCallback;

    public ExchangeModel(ExchangeInterfaces.ExchangePresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
        service = Injector.provideService();
    }

    @Override
    public void detach() {
        if (exchangeRateSubscription != null) {
            exchangeRateSubscription.unsubscribe();
        }
    }

    @Override
    public void doGetRatesRequest() {
        exchangeRateSubscription = getExchangeRatesObservable()
                .subscribe(new Subscriber<ExchangeRates>() {
                    @Override
                    public void onCompleted() {
                        Log.d("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenterCallback.onRatesLoadError(e);
                    }

                    @Override
                    public void onNext(ExchangeRates response) {
                        presenterCallback.onRatesLoaded(response);
                    }
                });
    }

    Observable<ExchangeRates> getExchangeRatesObservable() {
        return service.getLatestRates()
                .subscribeOn(Injector.provideIoScheduler())
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.delay(Constants.RATES_REPEAT_INTERVAL, TimeUnit.SECONDS);
                    }
                })
                .observeOn(Injector.provideUIScheduler())
                .flatMap(new Func1<ExchangeRatesResponse, Observable<ExchangeRates>>() {
                    @Override
                    public Observable<ExchangeRates> call(ExchangeRatesResponse exchangeRatesResponse) {
                        try {
                            return Observable.just(ExchangeRatesConverter.convert(exchangeRatesResponse));
                        } catch (ParseException e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                });
    }
}
