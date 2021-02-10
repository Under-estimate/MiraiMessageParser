package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.data.RichMessage;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that displays a mini app.
 * */
@RichMessageType(typeName = "MiniApp", fullName = "RichMessage/JSON/MiniApp", acceptedFeatureValues = "com.tencent.miniapp_01")
public final class MiniAppMessage extends JsonMessage{
    private static final String viewId="view_8C8E89B49BE609866298ADDFF2DBABA4";
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        JSONObject json= JSON.parseObject(message.getContent());
        if(!viewId.equals(json.getString("view")))
            return new ParsedRichMessage(this.getClass());
        json=json.getJSONObject("meta").getJSONObject("detail_1");
        ParsedRichMessage prm=new ParsedRichMessage("[MiniApp]",this.getClass());
        if(json.containsKey("qqdocurl"))
            prm.addLink(json.getString("qqdocurl"),"["+json.getString("title")+"]"+json.getString("desc"));
        prm.addImage("http://"+json.getString("preview"));
        return prm;
    }
}
