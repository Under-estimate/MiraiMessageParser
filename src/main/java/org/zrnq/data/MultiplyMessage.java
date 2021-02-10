package org.zrnq.data;

import net.mamoe.mirai.message.data.RichMessage;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Multiple messages that are forwarded together.
 * The text will only display the first few messages.
 * */
@RichMessageType(typeName = "Multiply", fullName = "RichMessage/XML/Multiply", acceptedFeatureValues = "35")
public final class MultiplyMessage extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        Element root=parseDocument(message.getContent());
        NodeList list=root.getElementsByTagName("title");
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<list.getLength();i++)
            sb.append(list.item(i).getTextContent()).append("\r\n");
        Element summary=(Element)root.getElementsByTagName("summary").item(0);
        sb.append(summary.getTextContent());
        return new ParsedRichMessage(sb.toString(),this.getClass());
    }
}
