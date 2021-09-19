package org.zrnq.data;

import org.w3c.dom.Element;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains a link to a video.
 * */
@RichMessageType(typeName = "Video", fullName = "RichMessage/XML/Video", acceptedFeatureValues = "140")
public final class VideoMessage extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root=parseDocument(message);
        String desc = root.getElementsByTagName("title").item(0).getTextContent();
        ParsedRichMessage prm=new ParsedRichMessage(desc,this.getClass());
        prm.addLink(root.getAttribute("url"),desc);
        prm.addImage(((Element)root.getElementsByTagName("video").item(0)).getAttribute("cover"));
        return prm;
    }
}
