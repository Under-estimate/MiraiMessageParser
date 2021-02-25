package org.zrnq;

import org.zrnq.annotation.RichMessageType;
import org.zrnq.data.RichMessageTemplate;

public class ParsedRichMessage extends ParsedMessage{
    /**
     * The type of this rich message. If this message is not a rich message, this field is null.
     * Or the last known type of this message when parsing failed.
     * @see ParsedRichMessage#isParseFailed
     * */
    public Class<? extends RichMessageTemplate> messageType;
    /**
     * Whether a failure has been encountered when parsing this rich message.
     * If this field is true, then messageType will represent the last known type.
     * @see ParsedRichMessage#messageType
     * */
    public boolean isParseFailed;
    /**
     * Using this constructor implies parse failed for this rich message.
     * @see ParsedRichMessage#isParseFailed
     * */
    public ParsedRichMessage(Class<? extends RichMessageTemplate> messageType){
        this.messageType=messageType;
        isParseFailed=true;
        text="[Parse Failed]";
    }
    /**
     * Using this constructor implies parse success for this rich message.
     * @see ParsedRichMessage#isParseFailed
     * */
    public ParsedRichMessage(String text, Class<? extends RichMessageTemplate> messageType){
        this.text=text;
        this.messageType=messageType;
        isParseFailed=false;
    }
    /**
     * Return the type name of this rich message.
     * If this message is not a rich message, return value is "".
     * */
    public String getRichMessageTypeName(){
        if(messageType == null)
            return "";
        else
            return messageType.getAnnotation(RichMessageType.class).fullName();
    }
    @Override
    public String toString() {
        return "["+getRichMessageTypeName()+"]\r\n"+super.toString();
    }
}
