package org.zrnq.data;

import net.mamoe.mirai.message.data.RichMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.zrnq.annotation.RichMessageType;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * The top level message type for xml rich message types.
 * */
@RichMessageType(typeName = "XML", fullName = "RichMessage/XML", acceptedFeatureValues = "<")
public class XmlMessage extends RichMessageTemplate{
    protected static DocumentBuilder builder;
    public XmlMessage(){
        try{
            builder= DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch (Exception e){
            Logger logger = LoggerFactory.getLogger(XmlMessage.class);
            logger.error("Unexpected Exception: Failed to create document builder.",e);
        }
    }
    @Override
    public String getClassifyingFeatureValue(RichMessage message) {
        Element root=parseDocument(message.getContent());
        return root.getAttribute("serviceID");
    }
    protected Element parseDocument(String xml){
        try{
            Document doc=builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            return doc.getDocumentElement();
        }catch (Exception e){
            throw new IllegalArgumentException("Rich message type is not xml.",e);
        }
    }
}
