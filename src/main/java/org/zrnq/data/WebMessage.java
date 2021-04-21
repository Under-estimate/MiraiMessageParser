package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains a link to a website.
 * */
@RichMessageType(typeName = "Web", fullName = "RichMessage/JSON/Struct/Web", acceptedFeatureValues = "新闻")
public final class WebMessage extends StructMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        JSONObject json= JSON.parseObject(message);
        String prompt = json.getString("prompt");
        json=json.getJSONObject("meta").getJSONObject("news");
        ParsedRichMessage parsed=new ParsedRichMessage(
                prompt + "\r\n" + json.getString("desc"),
                this.getClass());
        parsed.addLink(json.getString("jumpUrl"),json.getString("title"));
        return parsed;
    }
}
