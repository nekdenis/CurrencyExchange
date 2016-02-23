package nekdenis.github.com.exchangerates.data.parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public interface Parser<T> {
    T parse(InputStream in) throws XmlPullParserException, ParseException, IOException;
}
