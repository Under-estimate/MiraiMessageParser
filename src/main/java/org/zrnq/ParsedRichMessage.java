package org.zrnq;

import org.zrnq.annotation.RichMessageType;
import org.zrnq.data.RichMessageTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Parsed rich message contains text that represents the rich message, links and images in the rich message.
 * You do not need to instantiate this class. You can get the instance of this class in RichMessageParser.
 * @see RichMessageParser
 * */
public class ParsedRichMessage {
    /**
     * Images included in the rich message.
     * */
    public final ArrayList<URL> images;
    /**
     * Links included in the rich message.
     * */
    public final ArrayList<Link> links;
    /**
     * Text that represents the rich message.
     * */
    public final String text;
    /**
     * The type of this rich message.
     * Or the last known type of this message when parsing failed.
     * @see ParsedRichMessage#isParseFailed
     * */
    public final Class<? extends RichMessageTemplate> messageType;
    /**
     * Whether the parse has failed.
     * If this field is true, then messageType will represent the last known type.
     * @see ParsedRichMessage#messageType
     * */
    public final boolean isParseFailed;
    /**
     * Using this constructor will set isParseFailed to true.
     * @see ParsedRichMessage#isParseFailed
     * */
    public ParsedRichMessage(Class<? extends RichMessageTemplate> messageType){
        this.messageType=messageType;
        isParseFailed=true;
        images=new ArrayList<>(1);
        links=new ArrayList<>(1);
        text="[Parse Failed]";
    }
    /**
     * Using this constructor will set isParseFailed to false.
     * @see ParsedRichMessage#isParseFailed
     * */
    public ParsedRichMessage(String text,Class<? extends RichMessageTemplate> messageType){
        this.text=text;
        this.messageType=messageType;
        images=new ArrayList<>(1);
        links=new ArrayList<>(1);
        isParseFailed=false;
    }
    public String getMessageTypeName(){
        return messageType.getAnnotation(RichMessageType.class).fullName();
    }
    public void addImage(String url){
        try{
            images.add(new URL(url));
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid image url: "+url,e);
        }
    }
    public ArrayList<BufferedImage> downloadImages(){
        ArrayList<BufferedImage> data=new ArrayList<>();
        for(URL url:images){
            try{
                URLConnection conn=url.openConnection();
                conn.connect();
                data.add(ImageIO.read(conn.getInputStream()));
            }catch (Exception e){
                throw new IllegalArgumentException("Cannot download image from url:"+url);
            }
        }
        return data;
    }
    public void addLink(String url,String desc){
        try{
            links.add(new Link(new URL(url),desc));
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid link url: "+url,e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder(getMessageTypeName());
        for(URL url:images){
            sb.append("[Image]").append(url.toString()).append("\r\n");
        }
        for(Link l:links){
            sb.append("[Link]").append(l.desc).append("::").append(l.link).append("\r\n");
        }
        return sb.toString();
    }

    static class Link{
        /**
         * The url of this link.
         * */
        final URL link;
        /**
         * The description of this link.
         * */
        final String desc;
        public Link(URL url,String desc){
            this.link=url;
            this.desc=desc;
        }
    }
}
