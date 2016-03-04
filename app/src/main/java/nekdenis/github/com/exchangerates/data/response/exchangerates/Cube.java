package nekdenis.github.com.exchangerates.data.response.exchangerates;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "Cube")
public class Cube {
    @Attribute(name = "currency")
    public String currency;

    @Attribute(name = "rate")
    public double rate;
}
