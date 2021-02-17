package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * When a group member has uploaded/created a group album, this rich message is sent to the group.
 * Contains a link to the album and possibly multiple images.
 * */
@RichMessageType(typeName = "GroupAlbum", fullName = "RichMessage/JSON/GroupAlbum", acceptedFeatureValues = "com.tencent.groupphoto")
public final class GroupAlbum extends JsonMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        JSONObject json= JSON.parseObject(message);
        json=json.getJSONObject("meta").getJSONObject("albumData");
        ParsedRichMessage prm=new ParsedRichMessage(json.getString("title"),this.getClass());
        prm.addLink(json.getString("h5Url"),json.getString("albumName"));
        JSONArray pics=json.getJSONArray("pics");
        for(int i=0;i<pics.size();i++)
            prm.addImage(pics.getJSONObject(i).getString("url"));
        return prm;
    }
}
