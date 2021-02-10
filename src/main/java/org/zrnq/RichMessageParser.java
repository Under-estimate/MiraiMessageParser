package org.zrnq;

import net.mamoe.mirai.message.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zrnq.data.RichMessageTemplate;
import java.io.File;
import java.io.FileWriter;

public class RichMessageParser {
    private final Logger logger= LoggerFactory.getLogger(RichMessageParser.class);
    private final File logDestination;
    /**
     * @param logDestination The place where messages that are failed being parsed are stored for diagnosis.
     *                       Set to null if you do not want sending feedback.
     * */
    public RichMessageParser(File logDestination){
        this.logDestination=logDestination;
    }
    /**
     * Parse the given rich message.
     * @return Parsed rich message. You should check whether the parse has failed.
     * @see ParsedRichMessage#isParseFailed
     * */
    public ParsedRichMessage parseRichMessage(RichMessage message){
        ParsedRichMessage result;
        try{
            result=RichMessageTypes.getRichMessageType(RichMessageTemplate.class).parseMessage(message);
        }catch (Exception e){
            Class<? extends RichMessageTemplate> knownType=findLastKnownType(e);
            String knownTypeName=knownType==null?"Unknown":RichMessageTypes.getRichMessageType(knownType).getTypeName();
            logger.warn(String.format(Utils.parseFail,knownTypeName));
            writeLog(message.getContent(),knownType,e);
            return new ParsedRichMessage(knownType==null?RichMessageTemplate.class:knownType);
        }
        if(result.isParseFailed){
            logger.warn(String.format(Utils.unrecognizedMessage,result.getMessageTypeName()));
            writeLog(message.getContent(),result.messageType,null);
        }
        return result;
    }
    private void writeLog(String messageContent, Class<? extends RichMessageTemplate> knownType, Throwable cause){
        if(logDestination==null){
            logger.warn(Utils.NoLog);
            return;
        }
        logger.warn(String.format(Utils.Log,logDestination.getAbsolutePath()));
        try{
            FileWriter logger=new FileWriter(logDestination,true);
            logger.write("\r\n-----[Known Type: <");
            logger.write(knownType==null?"Unknown":RichMessageTypes.getRichMessageType(knownType).getTypeName());
            logger.write("> Class: <");
            logger.write(knownType==null?"Unknown":knownType.getCanonicalName());
            logger.write("> Time: <");
            logger.write(Utils.getTimeString());
            logger.write(">]-----\r\n");
            logger.write("[Content :]\r\n");
            logger.write(messageContent);
            if(cause!=null){
                logger.write("\r\n[Cause :]\r\n");
                logger.write(Utils.getStackTrace(cause));
            }
            logger.write("\r\n--------------------------------------------------\r\n");
            logger.flush();
            logger.close();
        }catch (Exception e){
            logger.warn("Failed to write message content that has failed being parsed.",e);
        }
    }
    @SuppressWarnings("unchecked")
    private Class<? extends RichMessageTemplate> findLastKnownType(Throwable cause){
        StackTraceElement[] list=cause.getStackTrace();
        try{
            for (StackTraceElement element:list){
                Class<?> clazz;
                if(RichMessageTemplate.class.isAssignableFrom(clazz=Class.forName(element.getClassName())))
                    return (Class<? extends RichMessageTemplate>)clazz;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }
}
