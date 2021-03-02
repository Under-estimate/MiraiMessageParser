package org.zrnq;

import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiraiMessageParser {
    private static final Logger logger = LoggerFactory.getLogger(MiraiMessageParser.class);

    private static final String friendMessageTemplate = "[%s]:";
    private static final String groupMessageTemplate = "[%s>%s]:";
    private static final String groupTempMessageTemplate = "[Temp|%s>%s]:";
    private static final String strangerMessageTemplate = "[Stranger|%s]:";
    private static final String messageSyncTemplate = "[Sync>%s]:";
    private static final String unknownTemplate = "[Unknown|%s]:";

    private final RichMessageParser richMessageParser;
    /**
     * @param logDestination Used for instantiating RichMessageParser
     * @see RichMessageParser#RichMessageParser(File) 
     * */
    public MiraiMessageParser(File logDestination){
        richMessageParser = new RichMessageParser(logDestination);
    }
    public ParsedMessage parseMessage(MessageEvent event){
        StringBuilder sb = new StringBuilder();
        ParsedMessage pm = new ParsedMessage();
        pm.sourcePrompt = getSourcePrompt(event);
        for(SingleMessage single : event.getMessage()){
            if(single instanceof Image){
                sb.append("[图片]");
                pm.addImage(Image.queryUrl((Image)single));
            }else if(single instanceof At){
                if(event instanceof GroupMessageEvent){
                    sb.append(((At)single).getDisplay(((GroupMessageEvent)event).getGroup()));
                }else if(event instanceof GroupMessageSyncEvent){
                    sb.append(((At)single).getDisplay(((GroupMessageSyncEvent)event).getGroup()));
                }else{
                    logger.warn("Invalid MessageEvent type : "+event.getClass().getName()+". MessageEvent contains At but isn't GroupMessageEvent or GroupMessageSyncEvent.");
                    sb.append(single.contentToString());
                }
            }else if(single instanceof FlashImage){
                sb.append("[闪照]");
                pm.addImage(Image.queryUrl(((FlashImage) single).getImage()));
            }else if(single instanceof Voice){
                ParsedSoundMessage psm = new ParsedSoundMessage("[Voice]",((Voice) single).getUrl());
                psm.text = sb.toString()+"[Voice]";
                return psm;
            }else if(single instanceof RichMessage){
                ParsedRichMessage prm = richMessageParser.parseRichMessage(((RichMessage)single).getContent());
                prm.text = Utils.removeControlCharacters(sb.toString() + prm.text);
                return prm;
            }else if(single instanceof MusicShare) {
                //Parsing music share message [RichMessage/JSON/Struct/Music] is supported by mirai officially.
                MusicShare ms = (MusicShare) single;
                ParsedSoundMessage psm = new ParsedSoundMessage("[MusicShare]",ms.getMusicUrl());
                psm.text = sb.toString()+"[MusicShare]"+ms.getTitle();
                psm.addImage(ms.getPictureUrl());
                psm.addLink("Website", ms.getJumpUrl());
                return psm;
            }else if(single instanceof ForwardMessage){
                //Parsing forward message [RichMessage/XML/Multiply] is supported by mirai officially.
                ForwardMessage fm = (ForwardMessage) single;
                sb.append(fm.getTitle()).append("\r\n")
                    .append(fm.getPreview()).append("\r\n")
                    .append(fm.getSummary());
            }else{
                sb.append(single.contentToString());
            }
        }
        pm.text = Utils.removeControlCharacters(sb.toString());
        Pattern pattern = Pattern.compile("([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://|[wW]{3}.|[wW][aA][pP].|[fF][tT][pP].|[fF][iI][lL][eE].)[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(pm.text);
        while(matcher.find())
            pm.addLink(matcher.group(),matcher.group());
        return pm;
    }
    /**
     * Get the best represented name of the event.
     * */
    public static String getUserName(User user){
        if(user.getRemark().equalsIgnoreCase(""))
            return user.getNick();
        else
            return user.getRemark();
    }
    /**
     * Get the prompt describing the source of this message event.
     * */
    public static String getSourcePrompt(MessageEvent event){
        if(event instanceof GroupMessageEvent){
            return String.format(groupMessageTemplate,
                    ((GroupMessageEvent) event).getGroup().getName(),
                    event.getSenderName());
        }else if(event instanceof FriendMessageEvent){
            return String.format(friendMessageTemplate,
                    getUserName(event.getSender()));
        }else if(event instanceof GroupTempMessageEvent){
            return String.format(groupTempMessageTemplate,
                    ((GroupTempMessageEvent) event).getGroup().getName(),
                    event.getSenderName());
        }else if(event instanceof StrangerMessageEvent){
            return String.format(strangerMessageTemplate,
                    event.getSenderName());
        }else if(event instanceof GroupMessageSyncEvent){
            return String.format(messageSyncTemplate,
                    ((GroupMessageSyncEvent) event).getGroup().getName());
        }else if(event instanceof FriendMessageSyncEvent){
            return String.format(messageSyncTemplate,
                    getUserName(event.getSender()));
        }else if(event instanceof GroupTempMessageSyncEvent){
            return String.format(messageSyncTemplate,
                    event.getSenderName());
        }else if(event instanceof StrangerMessageSyncEvent){
            return String.format(messageSyncTemplate,
                    event.getSenderName());
        }else{
            return (String.format(unknownTemplate,
                    event.getSenderName()));
        }
    }
}
