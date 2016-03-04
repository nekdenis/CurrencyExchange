package nekdenis.github.com.exchangerates.data.response.exchangerates;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

public class Cube1 {

    @Attribute(name = "time")
    public String time;

    @ElementList(inline = true)
    public List<Cube> ratesList;
}
