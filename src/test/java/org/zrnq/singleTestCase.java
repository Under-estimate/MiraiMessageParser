package org.zrnq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zrnq.data.JsonMessage;
import org.zrnq.data.RichMessageTemplate;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;

public class singleTestCase implements Runnable{
    final Logger logger= LoggerFactory.getLogger(singleTestCase.class);
    final Class<?> targetClass;
    public singleTestCase(Class <?> targetClass){
        this.targetClass=targetClass;
    }
    @SuppressWarnings("unchecked")
    public void run(){
        Class<? extends RichMessageTemplate> clazz=(Class<? extends RichMessageTemplate>) targetClass;
        if(!Modifier.isFinal(clazz.getModifiers())){
            return;
        }
        ParserTest.total++;
        String res;
        if(JsonMessage.class.isAssignableFrom(clazz)){
            res="/data/"+clazz.getSimpleName()+".json";
        }else{
            res="/data/"+clazz.getSimpleName()+".xml";
        }
        InputStream is=getClass().getResourceAsStream(res);
        if(is==null){
            ParserTest.skipped++;
            logger.warn("Resource for "+RichMessageTypes.getRichMessageType(clazz).getTypeName()+" not found. Skipping.");
            return;
        }
        StringBuilder sb=new StringBuilder();
        char[] cbuf=new char[1024];
        int len;
        try{
            InputStreamReader isr=new InputStreamReader(is, StandardCharsets.UTF_8);
            while((len=isr.read(cbuf))!=-1){
                sb.append(cbuf,0,len);
            }
        }catch (Exception e){
            logger.error("Failed to read test resource.",e);
        }
        ParsedRichMessage prm=ParserTest.parser.parseRichMessage(sb.toString());
        if(prm.isParseFailed)
            ParserTest.failed++;
        else
            ParserTest.passed++;
        //Test if image url is valid.
        prm.downloadImages();
        Assertions.assertFalse(prm.isParseFailed);
        logger.info(prm.toString());
    }
}
