package nekdenis.github.com.exchangerates.data.parser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import nekdenis.github.com.exchangerates.data.ExchangeRates;


public class RateParser implements Parser<ExchangeRates> {

    private SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String ns = null;

    @Override
    public ExchangeRates parse(InputStream in) throws XmlPullParserException, ParseException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return null;
    }

    private ExchangeRates readFeed(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        ExchangeRates rates = null;

        parser.require(XmlPullParser.START_TAG, ns, "gesmes:Envelope");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
                break;
            }
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Cube")) {
                parser.next();
                parser.next();
                parser.getName();

                parser.require(XmlPullParser.START_TAG, ns, "Cube");
                String time = parser.getAttributeValue(null, "time");
                rates = new ExchangeRates(timestampFormat.parse(time).getTime());
                parser.next();//en
                parser.next();
                while (parser.getAttributeCount() > 0) {
                    String attrName = parser.getName();
                    if (attrName.equals("Cube")) {
                        readRate(parser, rates);
                    }
                    parser.next();//end tag
                    parser.next();
                    parser.next();
                }
                parser.next();
                parser.next();
                parser.require(XmlPullParser.END_TAG, ns, "Cube");
                parser.next();
                parser.next();
            } else {
                skip(parser);
            }
        }
        return rates;
    }

    private void readRate(XmlPullParser parser, ExchangeRates rates) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Cube");
        String currency = null;
        Float rate = null;
        currency = parser.getAttributeValue(null, "currency");
        rate = Float.valueOf(parser.getAttributeValue(null, "rate"));
        if (currency != null) {
            rates.addRate(currency, rate);
        }
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
