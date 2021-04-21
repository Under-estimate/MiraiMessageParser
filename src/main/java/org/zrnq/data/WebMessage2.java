package org.zrnq.data;

import org.w3c.dom.Element;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains a link to a website.
 * */
@RichMessageType(typeName = "XML", fullName = "RichMessage/XML/Web", acceptedFeatureValues = "146")
public class WebMessage2 extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root = parseDocument(message);
        Element item = (Element) root.getElementsByTagName("item").item(0);
        Element title = (Element) item.getElementsByTagName("title").item(0);
        Element picture = (Element) item.getElementsByTagName("picture").item(0);
        Element summary = (Element) item.getElementsByTagName("summary").item(0);
        ParsedRichMessage prm = new ParsedRichMessage(
                 root.getAttribute("brief") + "\r\n" + summary.getTextContent(),
                this.getClass());
        prm.addImage(picture.getAttribute("cover"));
        prm.addLink(root.getAttribute("url"), title.getTextContent());
        return prm;
    }
}
