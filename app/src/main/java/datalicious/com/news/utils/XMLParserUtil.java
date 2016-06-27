package datalicious.com.news.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nayan on 27/6/16.
 */
public class XMLParserUtil {
//    public List parse(InputStream in) throws XmlPullParserException, IOException {
//        try {
//            XmlPullParser parser = Xml.newPullParser();
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//            parser.setInput(in, null);
//            parser.nextTag();
//            return readFeed(parser);
//        } finally {
//            in.close();
//        }
//    }
//
//    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
//        List entries = new ArrayList();
//
//        parser.require(XmlPullParser.START_TAG, ns, "feed");
//        while (parser.next() != XmlPullParser.END_TAG) {
//            if (parser.getEventType() != XmlPullParser.START_TAG) {
//                continue;
//            }
//            String name = parser.getName();
//            // Starts by looking for the entry tag
//            if (name.equals("entry")) {
//                entries.add(readEntry(parser));
//            } else {
//                skip(parser);
//            }
//        }
//        return entries;
//    }
//

}
