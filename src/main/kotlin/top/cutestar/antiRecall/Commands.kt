package top.cutestar.antiRecall

import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.testPermission
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import top.cutestar.antiRecall.AntiRecall.reload

@OptIn(ConsoleExperimentalApi::class)
object Commands : CompositeCommand(
    owner = AntiRecall,
    primaryName = "ar",
    description = "AR反撤回插件命令权限"
) {
    @Description("设置启用")
    @SubCommand("Enable", "en", "启用")
    suspend fun CommandSender.enable(@Name("true/false") isEnable: Boolean) {
        Config.enable = isEnable
        sendMessage("开关已设置为 ${Config.enable}")
    }

    @Description("设置消息缓存时间 单位:分钟 只支持正整数")
    @SubCommand("CacheTime", "ca", "缓存时间")
    suspend fun CommandSender.cacheTime(@Name("分钟") time: Int) {
        Config.cacheTime = time
        sendMessage("缓存时间已设置为 ${Config.cacheTime}")
    }

    @Description("设置转发消息的消息链 详细参考配置文件")
    @SubCommand("ForwardMsg", "fm", "转发")
    suspend fun CommandSender.forwardMsg(@Name("格式文本") s: String) {
        Config.messageText = s
        sendMessage("格式文本已设置为 ${Config.cacheTime}")
    }

    @Description("重载配置")
    @SubCommand("rd", "reload")
    suspend fun CommandSender.reload() {
        Config.reload()
        Bot.instances.forEach { bot ->
            bot.friends.asSequence()
                .filter {
                    AntiRecall.receiverPermission?.testPermission(AbstractPermitteeId.ExactFriend(it.id)) ?: false
                }.forEach { EventHost.receiveFriends.add(it.id) }

            bot.groups.asSequence()
                .filter {
                    AntiRecall.receiverPermission?.testPermission(AbstractPermitteeId.ExactGroup(it.id)) ?: false
                }.forEach { EventHost.receiveGroups.add(it.id) }
        }
        sendMessage("已重载")
    }
}