package nekdenis.github.com.exchangerates.data.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;
import org.simpleframework.xml.stream.Verbosity;

import java.io.FileNotFoundException;
import java.io.IOException;

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
        server.enqueue(new MockResponse().setBody(getStringFromAssets("")));

        Call<ExchangeRatesResponse> call = service.get();
        Response<ExchangeRatesResponse> response = call.execute();
        ExchangeRatesResponse body = response.body();
        Assert.assertEquals("2016-02-22", body.cube2.cube1.time);
        Assert.assertEquals("USD", body.cube2.cube1.ratesList.get(0).currency);
        Assert.assertEquals(1.1026d, body.cube2.cube1.ratesList.get(0).rate, 0.1);
        Assert.assertEquals(31, body.cube2.cube1.ratesList.size());
    }

    private String getStringFromAssets(String filePath) throws FileNotFoundException {
        return new String("<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
                "    <Cube>\n" +
                "        <Cube time=\"2016-02-22\">\n" +
                "            <Cube currency=\"USD\" rate=\"1.1026\"/>\n" +
                "            <Cube currency=\"JPY\" rate=\"124.85\"/>\n" +
                "            <Cube currency=\"BGN\" rate=\"1.9558\"/>\n" +
                "            <Cube currency=\"CZK\" rate=\"27.022\"/>\n" +
                "            <Cube currency=\"DKK\" rate=\"7.4620\"/>\n" +
                "            <Cube currency=\"GBP\" rate=\"0.78243\"/>\n" +
                "            <Cube currency=\"HUF\" rate=\"307.84\"/>\n" +
                "            <Cube currency=\"PLN\" rate=\"4.3646\"/>\n" +
                "            <Cube currency=\"RON\" rate=\"4.4767\"/>\n" +
                "            <Cube currency=\"SEK\" rate=\"9.3572\"/>\n" +
                "            <Cube currency=\"CHF\" rate=\"1.1008\"/>\n" +
                "            <Cube currency=\"NOK\" rate=\"9.4930\"/>\n" +
                "            <Cube currency=\"HRK\" rate=\"7.6300\"/>\n" +
                "            <Cube currency=\"RUB\" rate=\"83.4916\"/>\n" +
                "            <Cube currency=\"TRY\" rate=\"3.2512\"/>\n" +
                "            <Cube currency=\"AUD\" rate=\"1.5322\"/>\n" +
                "            <Cube currency=\"BRL\" rate=\"4.3755\"/>\n" +
                "            <Cube currency=\"CAD\" rate=\"1.5128\"/>\n" +
                "            <Cube currency=\"CNY\" rate=\"7.1909\"/>\n" +
                "            <Cube currency=\"HKD\" rate=\"8.5679\"/>\n" +
                "            <Cube currency=\"IDR\" rate=\"14824.67\"/>\n" +
                "            <Cube currency=\"ILS\" rate=\"4.3086\"/>\n" +
                "            <Cube currency=\"INR\" rate=\"75.5948\"/>\n" +
                "            <Cube currency=\"KRW\" rate=\"1351.19\"/>\n" +
                "            <Cube currency=\"MXN\" rate=\"19.9350\"/>\n" +
                "            <Cube currency=\"MYR\" rate=\"4.6171\"/>\n" +
                "            <Cube currency=\"NZD\" rate=\"1.6521\"/>\n" +
                "            <Cube currency=\"PHP\" rate=\"52.517\"/>\n" +
                "            <Cube currency=\"SGD\" rate=\"1.5469\"/>\n" +
                "            <Cube currency=\"THB\" rate=\"39.418\"/>\n" +
                "            <Cube currency=\"ZAR\" rate=\"16.8486\"/>\n" +
                "        </Cube>\n" +
                "    </Cube>\n" +
                "</gesmes:Envelope>");
//        return this.getClass().getClassLoader().getResourceAsStream(filePath);
    }

}
