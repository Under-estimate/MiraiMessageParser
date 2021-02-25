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


    /**
     * These unicode characters will impact the display of messages.
     * */
    private static final char[] blacklist = {
            //LRM      RLM      LRE      RLE      PDF       LRO
            '\u200e','\u200f','\u202a','\u202b','\u202c', '\u202d',
            //RLO      LRI      RLI      FSI      PDI
            '\u202e','\u2066','\u2067','\u2068','\u2069'};
    
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
    public static String removeControlCharacters(String input){
        StringBuilder sb = new StringBuilder(input);
        OUTER:
        for (int i = 0; i < sb.length(); i++) {
            for (char c : blacklist) {
                if (sb.charAt(i) == c){
                    sb.deleteCharAt(i);
                    i--;
                    continue OUTER;
                }
            }
        }
        return sb.toString();
    }
}
