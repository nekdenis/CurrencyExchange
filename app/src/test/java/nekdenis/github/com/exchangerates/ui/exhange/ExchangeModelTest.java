package nekdenis.github.com.exchangerates.ui.exhange;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import nekdenis.github.com.exchangerates.api.Api;
import nekdenis.github.com.exchangerates.data.ExchangeRates;
import nekdenis.github.com.exchangerates.data.response.exchangerates.Cube;
import nekdenis.github.com.exchangerates.data.response.exchangerates.Cube1;
import nekdenis.github.com.exchangerates.data.response.exchangerates.Cube2;
import nekdenis.github.com.exchangerates.data.response.exchangerates.ExchangeRatesResponse;
import nekdenis.github.com.exchangerates.di.Injector;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Injector.class)
public class ExchangeModelTest {

    @Test
    public void testDoGetRatesRequest() throws Exception {
        PowerMockito.mockStatic(Injector.class);
        Mockito.when(Injector.provideIoScheduler()).thenReturn(Schedulers.immediate());
        Mockito.when(Injector.provideUIScheduler()).thenReturn(Schedulers.immediate());
        Mockito.when(Injector.provideService()).thenReturn(new Api() {
            @Override
            public Observable<ExchangeRatesResponse> getLatestRates() {
                return Observable.just(prepareExchangeRatesResponse());
            }
        });

        ExchangeModel model = new ExchangeModel(null);

        Observable<ExchangeRates> exchangeRatesObservable = model.getExchangeRatesObservable();
        TestSubscriber<ExchangeRates> testSubscriber = new TestSubscriber<>();

        exchangeRatesObservable.subscribe(testSubscriber);

        testSubscriber.assertNoErrors();

        List<ExchangeRates> onNextEvents = testSubscriber.getOnNextEvents();
        Assert.assertFalse(onNextEvents.isEmpty());
    }

    @NonNull
    private ExchangeRatesResponse prepareExchangeRatesResponse() {
        ExchangeRatesResponse ratesResponse = new ExchangeRatesResponse();
        ratesResponse.cube2 = new Cube2();
        ratesResponse.cube2.cube1 = new Cube1();
        ratesResponse.cube2.cube1.ratesList = new ArrayList<>();
        ratesResponse.cube2.cube1.time = "2012-01-01";
        Cube currency = new Cube();
        currency.currency = "Rub";
        currency.rate = 0.01d;
        ratesResponse.cube2.cube1.ratesList.add(currency);
        return ratesResponse;
    }
}