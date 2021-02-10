package org.zrnq.data;

import net.mamoe.mirai.message.data.RichMessage;
import org.w3c.dom.Element;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Group vote message. The text only contains the title of the vote.
 * There are two kinds of group vote. One is a xml rich message, the other is a mini app.
 * */
@RichMessageType(typeName = "GroupVote", fullName = "RichMessage/XML/GroupVote", acceptedFeatureValues = "23")
public final class GroupVote extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        Element root=parseDocument(message.getContent());
        return new ParsedRichMessage(root.getElementsByTagName("title").item(0).getTextContent(),this.getClass());
    }
}
