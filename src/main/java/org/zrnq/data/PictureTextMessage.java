package org.zrnq.data;

import org.w3c.dom.Element;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains a picture and description.
 * */
@RichMessageType(typeName = "PictureText", fullName = "RichMessage/XML/PictureText", acceptedFeatureValues = {"1","33"})
public final class PictureTextMessage extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root=parseDocument(message);
        Element picture=(Element)root.getElementsByTagName("picture").item(0);
        ParsedRichMessage prm=new ParsedRichMessage(root.getElementsByTagName("summary").item(0).getTextContent(),this.getClass());
        prm.addLink(root.getAttribute("url"),root.getElementsByTagName("title").item(0).getTextContent());
        prm.addImage(picture.getAttribute("cover"));
        return prm;
    }
}
