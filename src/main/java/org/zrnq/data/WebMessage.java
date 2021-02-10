package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.data.RichMessage;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains a link to a website.
 * */
@RichMessageType(typeName = "Web", fullName = "RichMessage/JSON/Struct/Web", acceptedFeatureValues = "新闻")
public final class WebMessage extends StructMessage{
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        JSONObject json= JSON.parseObject(message.getContent());
        json=json.getJSONObject("meta").getJSONObject("news");
        ParsedRichMessage parsed=new ParsedRichMessage(json.getString("desc"),this.getClass());
        parsed.addLink(json.getString("jumpUrl"),json.getString("title"));
        return parsed;
    }
}
