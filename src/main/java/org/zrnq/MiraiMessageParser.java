package org.zrnq;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.contact.announcement.Announcement;
import net.mamoe.mirai.contact.announcement.OnlineAnnouncement;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.RemoteFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
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
                sb.append("[Image]");
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
                sb.append("[Flash Image]");
                pm.addImage(Image.queryUrl(((FlashImage) single).getImage()));
            }else if(single instanceof FileMessage){
                FileMessage message = (FileMessage) single;
                RemoteFile file = message.toRemoteFile((Group)event.getSubject());
                if(file == null) {
                    sb.append("[File]Fetch failed.");
                    break; // Since file message cannot combine with other messages.
                }
                RemoteFile.DownloadInfo info = file.getDownloadInfo();
                RemoteFile.FileInfo finfo = file.getInfo();
                if(info == null || finfo == null) {
                    sb.append("[File]Fetch failed.");
                    break;
                }
                sb.append("[File]").append(info.getFilename());
                pm.addLink(info.getUrl(), "[File]" + info.getFilename()
                        + "(" + Utils.getFileSizeRepresentation(finfo.getLength()) + ")",
                        ParsedMessage.LinkMediaType.File, info.getFilename());
                break;
            }else if(single instanceof OnlineAudio){
                String url = ((OnlineAudio)single).getUrlForDownload();
                pm.addLink(url, "[Voice]", ParsedMessage.LinkMediaType.Audio);
                break;
            }else if(single instanceof RichMessage) {
                ParsedRichMessage prm = richMessageParser.parseRichMessage(((RichMessage) single).getContent());
                prm.sourcePrompt = pm.sourcePrompt;
                prm.text = Utils.removeControlCharacters(sb + prm.text);
                return prm;
            }else if(single instanceof OnlineAnnouncement) {
                //Parsing group announcement message [RichMessage/JSON/GroupAnnounce] is supported by mirai officially.
                OnlineAnnouncement announcement = (OnlineAnnouncement) single;
                sb.append("[Announcement]").append(announcement.getContent());
                break;
            }else if(single instanceof MusicShare) {
                //Parsing music share message [RichMessage/JSON/Struct/Music] is supported by mirai officially.
                MusicShare ms = (MusicShare) single;
                sb.append("[Music Share]");
                pm.addImage(ms.getPictureUrl());
                pm.addLink(ms.getMusicUrl(),"[Music]" + ms.getTitle() , ParsedMessage.LinkMediaType.Audio);
                pm.addLink(ms.getJumpUrl(),"Website");
                break;
            }else if(single instanceof ForwardMessage){
                //Parsing forward message [RichMessage/XML/Multiply] is supported by mirai officially.
                ForwardMessage fm = (ForwardMessage) single;
                sb.append(fm.getTitle()).append("\r\n")
                    .append(fm.getPreview()).append("\r\n")
                    .append(fm.getSummary());
                break;
            }else{
                sb.append(single.contentToString());
            }
        }
        pm.text = Utils.removeControlCharacters(sb.toString());
        Pattern pattern = Pattern.compile("(http(s)?://.)?(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&/=]*)");
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
        String result;
        if(event instanceof GroupMessageEvent){
            result = String.format(groupMessageTemplate,
                    ((GroupMessageEvent) event).getGroup().getName(),
                    event.getSenderName());
        }else if(event instanceof FriendMessageEvent){
            result = String.format(friendMessageTemplate,
                    getUserName(event.getSender()));
        }else if(event instanceof GroupTempMessageEvent){
            result = String.format(groupTempMessageTemplate,
                    ((GroupTempMessageEvent) event).getGroup().getName(),
                    event.getSenderName());
        }else if(event instanceof StrangerMessageEvent){
            result = String.format(strangerMessageTemplate,
                    event.getSenderName());
        }else if(event instanceof GroupMessageSyncEvent){
            result = String.format(messageSyncTemplate,
                    ((GroupMessageSyncEvent) event).getGroup().getName());
        }else if(event instanceof FriendMessageSyncEvent){
            result = String.format(messageSyncTemplate,
                    getUserName(event.getSender()));
        }else if(event instanceof GroupTempMessageSyncEvent){
            result = String.format(messageSyncTemplate,
                    event.getSenderName());
        }else if(event instanceof StrangerMessageSyncEvent){
            result = String.format(messageSyncTemplate,
                    event.getSenderName());
        }else{
            result = (String.format(unknownTemplate,
                    event.getSenderName()));
        }
        return Utils.removeControlCharacters(result);
    }
}
