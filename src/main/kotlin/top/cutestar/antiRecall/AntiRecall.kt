package top.cutestar.antiRecall

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info

object AntiRecall : KotlinPlugin(
    JvmPluginDescription(
        id = "top.cutestar.antiRecall",
        name = "AntiRecall",
        version = "1.0.3",
    ) {
        author("CuteStar")
        info("""一款可以反撤回/闪照破解的mirai插件""")
    }
) {
    var receiverPermission: Permission? = null
    var monitoredPermission: Permission? = null
    override fun onEnable() {
        receiverPermission = PermissionService.INSTANCE.register(
            PermissionId("top.cutestar.antirecall", "receiver"),
            "转发消息接收者"
        )

        monitoredPermission = PermissionService.INSTANCE.register(
            PermissionId("top.cutestar.antirecall", "monitored"),
            "被监听者"
        )
        AntiRecall.globalEventChannel().registerListenerHost(EventHost.MessageEventHost)
        Config.reload()
        logger.info { "$name 插件已加载" }
        CommandManager.registerCommand(Commands)
    }

    override fun onDisable() {
        Config.save()
    }
}