package org.zrnq;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    static final String NoLog="If you would like to report rich messages that failed to be parsed, pass a log file destination when instantiating RichMessageParser.";
    static final String Log="You can find the raw content of rich messages that failed to be parsed here: %s";
    static final String parseFail="Failed to parse rich message type : %s. This may because some types of rich message has undergone structural changes.";
    static final String unrecognizedMessage="RichMessageParser has encountered an unrecognized message type. The known classification of the message is: %s.";
    public static String getStackTrace(Throwable t){
        StringWriter sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
    public static String getTimeString(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
