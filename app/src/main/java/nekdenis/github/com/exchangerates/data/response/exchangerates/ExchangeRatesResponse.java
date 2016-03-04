package nekdenis.github.com.exchangerates.data.response.exchangerates;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "gesmes:Envelope", strict=false)
public class ExchangeRatesResponse {

    @Element(name = "Cube")
    public Cube2 cube2;

}


