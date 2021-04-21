package org.zrnq.data;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message as a recommendation of a user.
 * */
@RichMessageType(typeName = "XML", fullName = "RichMessage/XML/ContactCard", acceptedFeatureValues = "14")
public class ContactCard extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root = parseDocument(message);
        NodeList list = root.getElementsByTagName("item");
        Element header = (Element) list.item(0);
        Element content = (Element) list.item(1);
        header = (Element) header.getElementsByTagName("summary").item(0);
        Element name = (Element) content.getElementsByTagName("title").item(0);
        Element desc = (Element) content.getElementsByTagName("summary").item(0);
        return new ParsedRichMessage(
                header.getTextContent() + " " + name.getTextContent()
                        + "(" + desc.getTextContent() + ")" ,
                this.getClass());
    }
}
