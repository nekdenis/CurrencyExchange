package nekdenis.github.com.exchangerates.data.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;
import org.simpleframework.xml.stream.Verbosity;

import java.io.IOException;
import java.io.InputStream;

import nekdenis.github.com.exchangerates.data.response.exchangerates.ExchangeRatesResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;


public class SimpleXmlConverterFactoryTest {
    interface Service {
        @GET("/")
        Call<ExchangeRatesResponse> get();
    }

    @Rule
    public final MockWebServer server = new MockWebServer();

    private Service service;

    @Before
    public void setUp() {
        Format format = new Format(0, null, new HyphenStyle(), Verbosity.HIGH);
        Persister persister = new Persister(format);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(SimpleXmlConverterFactory.create(persister))
                .build();
        service = retrofit.create(Service.class);
    }

    @Test
    public void testExchangeRatesResponseParser() throws IOException {
        server.enqueue(new MockResponse().setBody(getStringFromAssets("rate_test_data.xml")));

        Call<ExchangeRatesResponse> call = service.get();
        Response<ExchangeRatesResponse> response = call.execute();
        ExchangeRatesResponse body = response.body();
        Assert.assertEquals("2016-02-22", body.cube2.cube1.time);
        Assert.assertEquals("USD", body.cube2.cube1.ratesList.get(0).currency);
        Assert.assertEquals(1.1026d, body.cube2.cube1.ratesList.get(0).rate, 0.1);
        Assert.assertEquals(31, body.cube2.cube1.ratesList.size());
    }

    private String getStringFromAssets(String filePath) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(filePath);
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        is.close();
        return result;
    }

}
