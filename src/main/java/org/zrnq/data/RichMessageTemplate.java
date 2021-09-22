package org.zrnq.data;

import org.reflections.Reflections;
import org.zrnq.ParsedRichMessage;
import org.zrnq.RichMessageTypes;
import org.zrnq.annotation.RichMessageType;

import java.util.Collection;
import java.util.Set;

/**
 * The top most class of all rich messages.
 * Each rich message type is not required to be instantiated by user.
 * They will have a unique instance in RichMessageTypes.
 * @see RichMessageTypes
 * */
@RichMessageType(typeName = "RichMessage",fullName = "RichMessage")
public class RichMessageTemplate {
    /**
     * Parse the given message.
     * The default action of this method is:
     * 1. If there does not exist child message type, then an UnsupportedOperationException will be thrown.
     * 2. If there exist child message types, then find a child message type that can handle this message
     * by comparing the feature value of this message with feature values that a child message type accepts.
     * Message types that have no child message type should overwrite this method to implement message parsing.
     * @see RichMessageTemplate#getClassifyingFeatureValue(String)
     * @param message The message that need to parse.
     * @return Parsed message.
     * */
    @SuppressWarnings("unchecked")
    public ParsedRichMessage parseMessage(String message){
        RichMessageType typeAnnotation = this.getClass().getAnnotation(RichMessageType.class);
        if(typeAnnotation==null){
            throw new IllegalStateException("Child classes of RichMessageTemplate should be annotated with RichMessageType.");
        }
        Reflections reflections=new Reflections("org.zrnq.data");
        Collection<?> childTypes=reflections.getSubTypesOf(this.getClass());
        if(childTypes.size()<=0)
            throw new UnsupportedOperationException("Rich message type endpoint \""+this.getClass()+"\" not implemented.");
        String featureValue=getClassifyingFeatureValue(message);
        for(Object type:childTypes){
            Class<? extends RichMessageTemplate> clazz=(Class<? extends RichMessageTemplate>)type;
            String[] acceptValues=clazz.getAnnotation(RichMessageType.class).acceptedFeatureValues();
            for(String value:acceptValues){
                if(featureValue.equals(value))
                    return RichMessageTypes.getRichMessageType(clazz).parseMessage(message);
            }
        }
        return new ParsedRichMessage(this.getClass());
    }
    /**
     * Get the classifying feature value of given message.
     * The feature value is used to find appropriate child message type to handle the message.
     * Message types that have child message types should overwrite this method to classify child message types.
     * @see RichMessageTemplate#parseMessage(String)
     * @see RichMessageType#acceptedFeatureValues()
     * @param message The message that need to parse.
     * @return Extracted feature value.
     * */
    public String getClassifyingFeatureValue(String message){
        return message.substring(0,1);
    }
    /**
     * Get the full type name of given message type.
     * */
    public String getTypeName(){
        RichMessageType typeAnnotation = this.getClass().getAnnotation(RichMessageType.class);
        if(typeAnnotation==null){
            throw new IllegalStateException("Child classes of RichMessageTemplate should be annotated with RichMessageType.");
        }
        return typeAnnotation.fullName();
    }
}
