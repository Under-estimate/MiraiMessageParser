# MiraiMessageParser
Providing uniform expression of (nearly) every type of messages.  
This project is based on project [mamoe/mirai](https://github.com/mamoe/mirai)  

## Content

- [Usage](#usage)
- [Message Structure](#message-structure)
  
    - [Normal Message](#normal-message)
    - [Rich Message](#rich-message)
    - [Sound Message](#sound-message)
- [Supporting New Rich Message Types](#supporting-new-rich-message-types)
- [Logging](#logging)
- [Not Using Mirai Platform](#not-using-mirai-platform)

---
### Usage
- Download jar file from [Releases](https://github.com/Under-estimate/MiraiRichMessageParser/releases).  
- Add the jar file as a dependency of your project.
- You may need to add the following dependencies if they are not present:
```groovy
dependencies {
    api('net.mamoe:mirai-core:+')
    implementation 'com.alibaba:fastjson:1.2.75'
    implementation 'org.slf4j:slf4j-simple:2.0.0-alpha1'
    implementation 'org.reflections:reflections:0.9.12'
}
```
- Use [MiraiMessageParser](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/MiraiMessageParser.java) to parse messages.   

### Message Structure

#### Normal Message
By parsing a normal message, you'll get an instance of [ParsedMessage](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/ParsedMessage.java). In which these features will be extracted from normal messages:

- Source Prompt (A short text describing the source of this message)
- Images
- Links (If the message contains a valid link)
- Text

#### Rich Message
By parsing a rich message, you'll get an instance of [ParsedRichMessage](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/ParsedRichMessage.java), which is a subtype of [ParsedMessage](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/ParsedMessage.java). The description text, link and images included in the rich message will be extracted. Besides, [ParsedRichMessage](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/ParsedRichMessage.java) also contains the type of this rich message.

#### Sound Message
Since voice messages and music share messages contain a new media type, parsing them will give you an instance of [ParsedSoundMessage](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/ParsedSoundMessage.java), which contains a sound link.

### Supporting New Rich Message Types
Since there exists numerous rich message types, it's hard to cover every type. New types will be supported gradually.  
If you've discovered an unsupported rich message type, please [create an issue](https://github.com/Under-estimate/MiraiRichMessageParser/issues/new/choose) with associated information.  

### Logging
When creating an instance of [RichMessageParser](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/RichMessageParser.java), you'll be required to provide a file for storing the parser's log. The log will contain important information when the parser has encountered an unsupported rich message type, which is useful for supporting new rich message types.  
If you want to disable logging for some reasons, pass the `logDestination` argument with `null` value.

### Not Using Mirai Platform
If you are not using mirai platform, you can still use [RichMessageParser](https://github.com/Under-estimate/MiraiRichMessageParser/tree/main/src/main/java/org/zrnq/RichMessageParser.java), which accept raw JSON or XML when parsing rich messages.