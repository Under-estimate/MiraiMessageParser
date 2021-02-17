package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import org.zrnq.annotation.RichMessageType;

/**
 * Struct messages is used when sharing a link.
 * */
@RichMessageType(typeName = "Struct", fullName = "RichMessage/JSON/Struct", acceptedFeatureValues = "com.tencent.structmsg")
public class StructMessage extends JsonMessage{
    @Override
    public String getClassifyingFeatureValue(String message) {
        return JSON.parseObject(message).getString("desc");
    }
}
