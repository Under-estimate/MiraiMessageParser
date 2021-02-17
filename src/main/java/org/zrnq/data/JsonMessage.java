package org.zrnq.data;

import com.alibaba.fastjson.JSON;
import org.zrnq.annotation.RichMessageType;

/**
 * The top level message type of a json rich message.
 * */
@RichMessageType(typeName = "JSON", fullName = "RichMessage/JSON", acceptedFeatureValues = "{")
public class JsonMessage extends RichMessageTemplate{
    @Override
    public String getClassifyingFeatureValue(String message) {
        return JSON.parseObject(message).getString("app");
    }
}
