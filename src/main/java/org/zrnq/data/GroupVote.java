package org.zrnq.data;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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
        StringBuilder sb=new StringBuilder(root.getElementsByTagName("title").item(0).getTextContent());
        Element checklist = (Element) root.getElementsByTagName("checklist").item(0);
        NodeList list = checklist.getElementsByTagName("r");
        for (int i = 0; i < list.getLength(); i++) {
            String content = list.item(i).getTextContent();
            if(content.trim().equals(""))
                continue;
            sb.append("\r\n[").append(i+1).append(']').append(content);
        }
        return new ParsedRichMessage(sb.toString(),this.getClass());
    }
}
