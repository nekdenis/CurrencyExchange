package nekdenis.github.com.exchangerates.data.response.exchangerates;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This object and his children only for automatic parser.
 * //TODO: for release app with huge parsing much better to use manual parsers than annotation based.
 */
@Root(name = "gesmes:Envelope", strict=false)
public class ExchangeRatesResponse {

    @Element(name = "Cube")
    public Cube2 cube2;

}


