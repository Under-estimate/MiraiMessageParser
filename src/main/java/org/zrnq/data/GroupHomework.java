package org.zrnq.data;

import net.mamoe.mirai.message.data.RichMessage;
import org.w3c.dom.Element;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Group homework message. The content of the homework may not be complete if being too long.
 * */
@RichMessageType(typeName = "Homework", fullName = "RichMessage/XML/Homework", acceptedFeatureValues = "60")
public final class GroupHomework extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        Element root=parseDocument(message.getContent());
        return new ParsedRichMessage("["+root.getElementsByTagName("title").item(0).getTextContent()+"]"
                +root.getElementsByTagName("summary").item(0).getTextContent(),this.getClass());
    }
}
