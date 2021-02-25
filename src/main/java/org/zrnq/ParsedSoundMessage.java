package org.zrnq;

import java.net.URL;

public class ParsedSoundMessage extends ParsedMessage{
    /**
     * Voice included in this message.
     * Voice can only be fetched from voice messages without other content.
     * */
    public URL sound;
    public String description;
    public ParsedSoundMessage(String desc, String sound){
        this.description = desc;
        try{
            this.sound = new URL(sound);
        }catch (Exception e){
            throw new IllegalArgumentException("Illegal voice url:"+sound,e);
        }
    }
    @Override
    public String toString() {
        return super.toString()+"[Sound]"+description+"::"+sound;
    }
}
