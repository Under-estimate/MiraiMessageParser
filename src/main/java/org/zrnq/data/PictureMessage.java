package org.zrnq.data;

import org.w3c.dom.Element;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that shows a picture in extraordinary size.
 * However only the md5 value instead of the url of the picture exists in the message.
 * */
@RichMessageType(typeName = "Picture", fullName = "RichMessage/XML/Picture", acceptedFeatureValues = "5")
public final class PictureMessage extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root=parseDocument(message);
        Element image = (Element) root.getElementsByTagName("image").item(0);
        ParsedRichMessage prm = new ParsedRichMessage(root.getAttribute("brief"),this.getClass());
        prm.addImage(String.format("https://gchat.qpic.cn/gchatpic_new/0/0-0-%s/0", image.getAttribute("md5")));
        return prm;
    }
}
