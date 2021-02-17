# MiraiRichMessageParser
Providing support on extracting key information from rich messages.  
This project is based on project [mamoe/mirai](https://github.com/mamoe/mirai)  

---
### Usage
- Download jar file from [Releases](https://github.com/Under-estimate/MiraiRichMessageParser/releases).  
- Add the jar file as a dependency of your project.
- You may need to add the following dependencies if they are not present:
```groovy
dependencies {
    implementation 'com.alibaba:fastjson:1.2.75'
    implementation 'org.slf4j:slf4j-simple:2.0.0-alpha1'
    implementation 'org.reflections:reflections:0.9.12'
}
```
- Use [RichMessageParser](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/RichMessageParser.java) to parse rich messages.   

### Supporting New Rich Message Types
Since there exists numerous rich message types, it's hard to cover every type. New types will be supported gradually.  
If you've discovered an unsupported message type (when [isParseFailed](https://github.com/Under-estimate/MiraiRichMessageParser/blob/749b553affd89e750e4536447ef1ebd5786245e7/src/main/java/org/zrnq/ParsedRichMessage.java#L41) is true), please [create an issue](https://github.com/Under-estimate/MiraiRichMessageParser/issues/new/choose) with associated information.  

### Logging
When creating an instance of [RichMessageParser](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/RichMessageParser.java), you'll be required to provide a file for storing the parser's log. The log will contain important information when the parser has encountered an unsupported rich message type, which is useful for supporting new rich message types.  
If you want to disable logging for some reasons, pass the [logDestination](https://github.com/Under-estimate/MiraiRichMessageParser/blob/749b553affd89e750e4536447ef1ebd5786245e7/src/main/java/org/zrnq/RichMessageParser.java#L13) argument with `null` value.
