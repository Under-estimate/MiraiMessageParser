package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.data.RichMessage;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * A share message of QZone album.
 * May contain multiple images.
 * */
@RichMessageType(typeName = "QZoneAlbum", fullName = "RichMessage/JSON/QZoneAlbum", acceptedFeatureValues = "com.tencent.qzone.albumInvite")
public final class QZoneAlbum extends JsonMessage{
    @Override
    public ParsedRichMessage parseMessage(RichMessage message) {
        JSONObject json= JSON.parseObject(message.getContent());
        json=json.getJSONObject("meta").getJSONObject("albumData");
        ParsedRichMessage prm=new ParsedRichMessage(json.getString("desc"),this.getClass());
        prm.addLink(json.getString("h5Url"),json.getString("title"));
        JSONArray pics=json.getJSONArray("pics");
        for(int i=0;i<pics.size();i++)
            prm.addImage(pics.getJSONObject(i).getString("url"));
        return prm;
    }
}
