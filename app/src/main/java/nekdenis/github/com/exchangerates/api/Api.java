package nekdenis.github.com.exchangerates.api;

import nekdenis.github.com.exchangerates.data.response.exchangerates.ExchangeRatesResponse;
import retrofit2.http.GET;
import rx.Observable;

public interface Api {

    @GET("stats/eurofxref/eurofxref-daily.xml")
    Observable<ExchangeRatesResponse> getLatestRates();

}
