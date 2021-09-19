package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that displays a mini app.
 * */
@RichMessageType(typeName = "MiniApp", fullName = "RichMessage/JSON/MiniApp", acceptedFeatureValues = "com.tencent.miniapp_01")
public final class MiniAppMessage extends JsonMessage{
    private static final String viewId="view_8C8E89B49BE609866298ADDFF2DBABA4";
    @Override
    public ParsedRichMessage parseMessage(String message) {
        JSONObject json= JSON.parseObject(message);
        if(!viewId.equals(json.getString("view")))
            return new ParsedRichMessage(this.getClass());
        json=json.getJSONObject("meta").getJSONObject("detail_1");
        String desc = "["+json.getString("title")+"]"+json.getString("desc");
        ParsedRichMessage prm=new ParsedRichMessage(desc,this.getClass());
        if(json.containsKey("qqdocurl"))
            prm.addLink(json.getString("qqdocurl"),desc);
        prm.addImage(json.getString("preview"));
        return prm;
    }
}
