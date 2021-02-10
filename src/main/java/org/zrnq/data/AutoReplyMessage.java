package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.data.RichMessage;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains one header and multiple buttons with text.
 * Click on one of the buttons will make the client automatically send corresponding message.
 * This is meant to be used in customer services.
 * */
@RichMessageType(typeName = "AutoReply", fullName = "RichMessage/JSON/AutoReply", acceptedFeatureValues = "com.tencent.autoreply")
public final class AutoReplyMessage extends JsonMessage{
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        JSONObject json= JSON.parseObject(message.getContent());
        json=json.getJSONObject("meta").getJSONObject("metadata");
        StringBuilder sb=new StringBuilder(json.getString("title"));
        JSONArray choices=json.getJSONArray("buttons");
        for(int i=0;i<choices.size();i++)
            sb.append("\r\n").append(choices.getJSONObject(i).getString("name"))
                    .append("=>").append(choices.getJSONObject(i).getString("action_data"));
        return new ParsedRichMessage(sb.toString(),this.getClass());
    }
}
