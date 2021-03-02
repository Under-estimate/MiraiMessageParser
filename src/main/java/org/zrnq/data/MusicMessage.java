package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.CollectData;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains a music link.
 * */
@CollectData
@RichMessageType(typeName = "Music", fullName = "RichMessage/JSON/Struct/Music", acceptedFeatureValues = "音乐")
public final class MusicMessage extends StructMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        JSONObject json= JSON.parseObject(message);
        json=json.getJSONObject("meta").getJSONObject("music");
        ParsedRichMessage parsed=new ParsedRichMessage(json.getString("desc"),this.getClass());
        parsed.addLink(json.getString("jumpUrl"),json.getString("title"));
        return parsed;
    }
}
