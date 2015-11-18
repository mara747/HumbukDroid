package cz.borec.kareljeproste.humbukdroid;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AndroidSaxFeedParser extends BaseFeedParser {

    static final String RSS = "rss";
    public AndroidSaxFeedParser(String feedUrl) {
        super(feedUrl);
    }

    public List<Message> parse() {
        final Message currentMessage = new Message();
        RootElement root = new RootElement(RSS);
        final List<Message> messages = new ArrayList<Message>();
        Element channel = root.getChild(BaseFeedParser.CHANNEL);
        Element item = channel.getChild(BaseFeedParser.ITEM);
        item.setEndElementListener(new EndElementListener(){
            public void end() {
                messages.add(currentMessage.copy());
            }
        });
        item.getChild(BaseFeedParser.TITLE).setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
                currentMessage.setTitle(body);
            }
        });
        item.getChild(BaseFeedParser.LINK).setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
                currentMessage.setLink(body);
            }
        });
        item.getChild(BaseFeedParser.DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
                currentMessage.setDescription(body);
            }
        });
        item.getChild(BaseFeedParser.PUB_DATE).setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
                currentMessage.setDate(body);
            }
        });
        try {
            InputStream is = this.getInputStream();
            org.xml.sax.ContentHandler ch = root.getContentHandler();
            Xml.parse(is, Xml.Encoding.ISO_8859_1, ch);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return messages;
    }
}
