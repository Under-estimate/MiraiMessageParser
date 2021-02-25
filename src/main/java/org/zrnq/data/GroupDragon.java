package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.zrnq.ParsedMessage;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Group "connect the dragon" message. The prompt only contains the name of the dragon.
 * */
@RichMessageType(typeName = "GroupDragon", fullName = "RichMessage/JSON/GroupDragon", acceptedFeatureValues = "com.tencent.groupDragon")
public final class GroupDragon extends JsonMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        JSONObject json= JSON.parseObject(message);
        return new ParsedRichMessage(json.getString("prompt"),this.getClass());
    }
}
