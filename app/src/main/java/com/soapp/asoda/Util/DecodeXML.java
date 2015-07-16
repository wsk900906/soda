package com.soapp.asoda.Util;

import android.content.Context;
import android.util.Log;

import com.soapp.asoda.Market;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swifty.Wang on 2015/6/30.
 */
public class DecodeXML {
    private static String text;

    public static List<Market> decodeMarket(Context context) {

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            InputStream in_s = context.getAssets().open("marketlist.xml");
            parser.setInput(in_s, null);
            int eventType = parser.getEventType();
            List<Market> markets = new ArrayList<Market>();
            Market currentitem = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (name.equalsIgnoreCase("item")) {
                            currentitem = new Market();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("item")) {
                            markets.add(currentitem);
                        } else if (name.equalsIgnoreCase("itemid")) {
                            currentitem.setId(text);
                        } else if (name.equalsIgnoreCase("itemname")) {
                            currentitem.setName(text);
                        } else if (name.equalsIgnoreCase("itemurl")) {
                            currentitem.setUrl(text);
                        } else if (name.equalsIgnoreCase("itemmethod")) {
                            currentitem.setMethod(text);
                        }
                        break;
                }
                eventType = parser.next();
            }
            return markets;
        } catch (Exception e) {
            Log.e("DECODEXML", e.toString());
            return new ArrayList<Market>();
        }
    }
}
