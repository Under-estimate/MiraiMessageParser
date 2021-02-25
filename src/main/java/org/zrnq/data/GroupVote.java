package org.zrnq.data;

import org.w3c.dom.Element;
import org.zrnq.ParsedMessage;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Group vote message. The text only contains the title of the vote.
 * There are two kinds of group vote. One is a xml rich message, the other is a mini app.
 * */
@RichMessageType(typeName = "GroupVote", fullName = "RichMessage/XML/GroupVote", acceptedFeatureValues = "23")
public final class GroupVote extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root=parseDocument(message);
        return new ParsedRichMessage(root.getElementsByTagName("title").item(0).getTextContent(),this.getClass());
    }
}
