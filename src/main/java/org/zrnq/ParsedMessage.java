package org.zrnq;

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
public class ParsedMessage {
    /**
     * Images included in this message.
     * Images may be fetched from normal messages that contains images or from rich messages.
     * */
    public final ArrayList<URL> images = new ArrayList<>(1);
    /**
     * Links included in this message.
     * Links may be fetched from normal messages that contains links or from rich messages.
     * */
    public final ArrayList<Link> links = new ArrayList<>(1);

    /**
     * Text that represents this message.
     * */
    public String text;

    protected ParsedMessage(){
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
        StringBuilder sb=new StringBuilder(text);
        sb.append("\r\n");
        for(URL url:images)
            sb.append("[Image]").append(url.toString()).append("\r\n");
        for(ParsedMessage.Link l:links)
            sb.append("[Link]").append(l.desc).append("::").append(l.link).append("\r\n");
        return sb.toString();
    }
    public static class Link{
        /**
         * The url of this link.
         * */
        public final URL link;
        /**
         * The description of this link.
         * */
        public final String desc;
        public Link(URL url,String desc){
            this.link=url;
            this.desc=desc;
        }
    }
}
