package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.data.RichMessage;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Group "connect the dragon" message. The prompt only contains the name of the dragon.
 * */
@RichMessageType(typeName = "GroupDragon", fullName = "RichMessage/JSON/GroupDragon", acceptedFeatureValues = "com.tencent.groupDragon")
public final class GroupDragon extends JsonMessage{
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        JSONObject json= JSON.parseObject(message.getContent());
        return new ParsedRichMessage(json.getString("prompt"),this.getClass());
    }
}
