package org.zrnq.data;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message as a recommendation of a group.
 * */
@RichMessageType(typeName = "XML", fullName = "RichMessage/XML/GroupCard", acceptedFeatureValues = "15")
public class GroupCard extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root = parseDocument(message);
        NodeList list = root.getElementsByTagName("item");
        Element header = (Element) list.item(0);
        Element content = (Element) list.item(1);
        header = (Element) header.getElementsByTagName("summary").item(0);
        Element picture = (Element) content.getElementsByTagName("picture").item(0);
        Element name = (Element) content.getElementsByTagName("title").item(0);
        Element desc = (Element) content.getElementsByTagName("summary").item(0);
        ParsedRichMessage prm = new ParsedRichMessage(
                header.getTextContent() + " " + name.getTextContent()
                    + "(" +root.getAttribute("actionData") + ")" + "\r\n" + desc.getTextContent(),
                this.getClass());
        prm.addImage(picture.getAttribute("cover"));
        prm.addLink(root.getAttribute("url"), "Invitation Link");
        return prm;
    }
}
