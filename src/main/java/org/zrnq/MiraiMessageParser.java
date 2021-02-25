package org.zrnq;

import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiraiMessageParser {

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
        if(event instanceof GroupMessageEvent){
            sb.append(String.format(groupMessageTemplate,
                    ((GroupMessageEvent) event).getGroup().getName(),
                    event.getSenderName()));
        }else if(event instanceof FriendMessageEvent){
            sb.append(String.format(friendMessageTemplate,
                    getSenderName(event)));
        }else if(event instanceof GroupTempMessageEvent){
            sb.append(String.format(groupTempMessageTemplate,
                    ((GroupTempMessageEvent) event).getGroup().getName(),
                    event.getSenderName()));
        }else if(event instanceof StrangerMessageEvent){
            sb.append(String.format(strangerMessageTemplate,
                    event.getSenderName()));
        }else if(event instanceof GroupMessageSyncEvent){
            sb.append(String.format(messageSyncTemplate,
                    ((GroupMessageSyncEvent) event).getGroup().getName()));
        }else if(event instanceof FriendMessageSyncEvent){
            sb.append(String.format(messageSyncTemplate,
                    getSenderName(event)));
        }else if(event instanceof GroupTempMessageSyncEvent){
            sb.append(String.format(messageSyncTemplate,
                    event.getSenderName()));
        }else if(event instanceof StrangerMessageSyncEvent){
            sb.append(String.format(messageSyncTemplate,
                    event.getSenderName()));
        }else{
            sb.append(String.format(unknownTemplate,
                    event.getSenderName()));
        }
        ParsedMessage pm = new ParsedMessage();
        for(SingleMessage single : event.getMessage()){
            if(single instanceof Image){
                sb.append("[图片]");
                pm.addImage(Image.queryUrl((Image)single));
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
    private static String getSenderName(MessageEvent event){
        if(event.getSender().getRemark().equalsIgnoreCase(""))
            return event.getSender().getNick();
        else
            return event.getSender().getRemark();
    }
}
