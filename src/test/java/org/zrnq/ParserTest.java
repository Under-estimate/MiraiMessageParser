package org.zrnq;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zrnq.annotation.RichMessageType;
import org.zrnq.data.RichMessageTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class ParserTest {
    public static int total=0, skipped=0, passed=0, failed=0;
    public static RichMessageParser parser;
    Logger logger= LoggerFactory.getLogger(ParserTest.class);
    /**
     * According to the super class of final rich message types, add corresponding xml or json file to
     * test resource. Then run this test.
     * */
    @TestFactory
    @SuppressWarnings("unchecked")
    ArrayList<DynamicTest> testParser(){
        File logDestination=new File("parser.log");
        //Clear parser log
        logDestination.delete();
        parser=new RichMessageParser(logDestination);
        Reflections reflect=new Reflections("org.zrnq.data");
        Collection<?> list = reflect.getTypesAnnotatedWith(RichMessageType.class);
        ArrayList<DynamicTest> tests=new ArrayList<>();
        for(Object obj:list){
            Class<? extends RichMessageTemplate> clazz=(Class<? extends RichMessageTemplate>) obj;
            tests.add(DynamicTest.dynamicTest(RichMessageTypes.getRichMessageType(clazz).getTypeName(),new singleTestCase(clazz)::run));
        }
        return tests;
    }
}
