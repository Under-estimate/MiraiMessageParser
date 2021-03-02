package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.CollectData;
import org.zrnq.annotation.RichMessageType;

/**
 * Group announce message. The text may not be complete if the announce is too long.
 * */
@CollectData
@RichMessageType(typeName = "GroupAnnounce", fullName = "RichMessage/JSON/GroupAnnounce", acceptedFeatureValues = "com.tencent.mannounce")
public final class GroupAnnounce extends JsonMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        JSONObject json= JSON.parseObject(message);
        return new ParsedRichMessage(json.getString("prompt"),this.getClass());
    }
}
