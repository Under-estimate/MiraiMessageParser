package org.zrnq.data;

import net.mamoe.mirai.message.data.RichMessage;
import org.w3c.dom.Element;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains a link to a video.
 * */
@RichMessageType(typeName = "Video", fullName = "RichMessage/XML/Video", acceptedFeatureValues = "140")
public final class VideoMessage extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        Element root=parseDocument(message.getContent());
        ParsedRichMessage prm=new ParsedRichMessage("[Video]",this.getClass());
        prm.addLink(root.getAttribute("url"),root.getElementsByTagName("title").item(0).getTextContent());
        prm.addImage(((Element)root.getElementsByTagName("video").item(0)).getAttribute("cover"));
        return prm;
    }
}
