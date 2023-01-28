package top.cutestar.antiRecall

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    @ValueDescription("设置启用 true为启用 false为禁用")
    var enable by value(true)

    @ValueDescription("消息缓存时间 单位:分钟 只支持正整数 注:设置过大可能会导致 多余消息没有得到清除而 内存溢出")
    var cacheTime by value(30)
//audio(语音), 暂时实现不了
    @ValueDescription("转发消息的类型 可选recall(撤回消息),flashimage(闪照),image(图片)")
    var type by value(mutableSetOf("recall","flashimage"))

    @ValueDescription("连续发送消息延迟 单位:毫秒")
    var sendDelay by value((500).toLong())

    @ValueDescription("转发消息的消息链 表示发送者:\"${'$'}{sender}\" 表示消息类型:\"${'$'}{type}\" 表示原消息:\"${'$'}{message}\" 使用合并转发的方式发送:\"${'$'}{forwardMessage}\"")
    var messageText by value(
        """${'$'}{sender}发送了一条${'$'}{type}消息:
${'$'}{message}""")
}