package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.zrnq.ParsedRichMessage;
import org.zrnq.annotation.RichMessageType;

/**
 * Rich message that contains one image, with text at the bottom of the image.
 * */
@RichMessageType(typeName = "BannerImage", fullName = "RichMessage/JSON/BannerImage", acceptedFeatureValues = "com.tencent.qqpay.qqmp.groupmsg")
public final class BannerImage extends JsonMessage{
    @Override
    public ParsedRichMessage parseMessage(String message) {
        JSONObject json= JSON.parseObject(message);
        json=json.getJSONObject("meta").getJSONObject("groupPushData");
        ParsedRichMessage prm=new ParsedRichMessage(json.getString("bannerTxt"),this.getClass());
        prm.addImage(json.getString("bannerImg"));
        return prm;
    }
}
