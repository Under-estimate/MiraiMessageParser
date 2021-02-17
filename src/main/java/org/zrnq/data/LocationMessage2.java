package org.zrnq.data;

import org.w3c.dom.Element;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains a map for the specified location.
 * @see LocationMessage
 * */
@RichMessageType(typeName = "Location", fullName = "RichMessage/XML/Location", acceptedFeatureValues = "32")
public final class LocationMessage2 extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root=parseDocument(message);
        return new ParsedRichMessage(root.getElementsByTagName("summary").item(0).getTextContent(),this.getClass());
    }
}
