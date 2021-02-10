package org.zrnq;

import org.zrnq.data.RichMessageTemplate;

import java.util.HashMap;

/**
 * Every rich message type will have an unique instance here, used for parsing rich message.
 * */
public class RichMessageTypes {
    private static final HashMap<Class<? extends RichMessageTemplate>,RichMessageTemplate> types=new HashMap<>();
    /**
     * Get the unique instance of the given rich message type class.
     * */
    @SuppressWarnings("unchecked")
    public static <T extends RichMessageTemplate> T getRichMessageType(Class<T> clazz){
        if(!types.containsKey(clazz)){
            try{
                types.put(clazz,clazz.newInstance());
            }catch (Exception e){
                throw new IllegalStateException("Failed to instantiate rich message type class: "+clazz,e);
            }
        }
        return (T)types.get(clazz);
    }
}
