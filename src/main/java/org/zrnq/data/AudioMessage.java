package org.zrnq.data;

import org.w3c.dom.Element;
import org.zrnq.ParsedMessage;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.CollectData;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains an audio link.
 * */
@CollectData
@RichMessageType(typeName = "Audio", fullName = "RichMessage/XML/Audio", acceptedFeatureValues = "2")
public final class AudioMessage extends XmlMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        Element root=parseDocument(message);
        ParsedRichMessage prm=new ParsedRichMessage(root.getElementsByTagName("summary").item(0).getTextContent(),this.getClass());
        prm.addLink(root.getAttribute("url"),
                root.getElementsByTagName("title").item(0).getTextContent(),
                ParsedMessage.LinkMediaType.Audio);
        return prm;
    }
}
