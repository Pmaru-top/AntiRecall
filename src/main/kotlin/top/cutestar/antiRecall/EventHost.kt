package top.cutestar.antiRecall

import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermissionService.Companion.testPermission
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.events.MessageRecallEvent
import top.cutestar.antiRecall.AntiRecall.monitoredPermission
import top.cutestar.antiRecall.AntiRecall.receiverPermission
import top.cutestar.antiRecall.data.MessageSource
import java.util.*
import kotlin.concurrent.schedule

object EventHost {
    val msgMap = mutableMapOf<Int, MessageSource>()
    val receiveGroups = mutableSetOf<Long>()
    val receiveFriends = mutableSetOf<Long>()

    init {
        Timer().schedule(10_000, 10_000) {
            msgMap.run {
                keys.forEach {
                    if (this[it]?.messageSource?.time!! > System.currentTimeMillis() / 1000 + Config.cacheTime * 60) {
                        remove(it)
                    }
                }
            }
        }
    }

    object MessageEventHost : SimpleListenerHost() {
        @OptIn(ExperimentalCommandDescriptors::class, ConsoleExperimentalApi::class)
        @EventHandler
        suspend fun onHandle(event: MessageEvent) {
            if (!Config.enable) return
            event.run {
                if (toCommandSender().permitteeId.hasPermission(monitoredPermission ?: return)) {
                    msgMap[source.ids[0]] = MessageSource(source, bot.id)
                }
            }
            Util.eventHandler(Config.messageText, event)
        }

        @EventHandler
        suspend fun onRecall(event: MessageRecallEvent) {
            if (!Config.enable) return
            if ("recall" in Config.type) Util.eventHandler(Config.messageText, event)
        }

        @EventHandler
        fun onLogin(event: BotOnlineEvent) {
            Bot.instances.forEach { bot ->
                bot.friends.asSequence()
                    .filter {
                        receiverPermission?.testPermission(AbstractPermitteeId.ExactFriend(it.id)) ?: false
                    }.forEach {
                        receiveFriends.add(it.id)
                    }

                bot.groups.asSequence()
                    .filter {
                        receiverPermission?.testPermission(AbstractPermitteeId.ExactGroup(it.id)) ?: false
                    }.forEach { receiveGroups.add(it.id) }
            }
        }
    }
}