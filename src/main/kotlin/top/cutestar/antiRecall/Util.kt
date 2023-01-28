package top.cutestar.antiRecall

import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.message.data.*

object Util {
    private suspend fun bulkSend(bot: Bot, msg: MessageChain) {
        if (msg.isEmpty()) return
        EventHost.receiveFriends.forEach {
            bot.getFriend(it)?.sendMessage(msg)
            delay(Config.sendDelay)
        }
        EventHost.receiveGroups.forEach {
            bot.getGroup(it)?.sendMessage(msg)
            delay(Config.sendDelay)
        }
    }

    suspend fun eventHandler(s: String, event: Event) {
        var text = s
        val msgBuilder = MessageChainBuilder()
        val isForwardMessage = "${"$"}{forwardMessage}" in text
        val isMessage = "${'$'}{message}" in text
        text = text
            .replace("${"$"}{forwardMessage}", "")
            .replace("${'$'}{message}", "")

        when (event) {
            is MessageRecallEvent -> {
                text = text.replace("${'$'}{type}", "撤回")
                when (event) {
                    is MessageRecallEvent.GroupRecall -> {
                        event.apply { text = text.replace("${'$'}{sender}", "${group.name}/${author.nick}($authorId)") }
                    }
                    is MessageRecallEvent.FriendRecall -> {
                        event.apply { text = text.replace("${'$'}{sender}", "${author.nick}($authorId)") }
                    }
                }
                msgBuilder.add(text)

                event.apply {
                    if (isMessage) {
                        msgBuilder.add(
                            EventHost.msgMap[messageIds[0]]?.messageSource?.originalMessage ?: return@apply
                        )
                    }
                    val msg = when (isForwardMessage) {
                        true -> msgBuilder.build().toForwardMessage(authorId, author.nick).toMessageChain()
                        else -> msgBuilder.build()
                    }
                    bulkSend(bot, msg)
                }
            }

            is MessageEvent -> {
                when (event) {
                    is GroupMessageEvent -> {
                        event.apply {
                            text = text.replace("${'$'}{sender}", "${group.name}/${senderName}(${sender.id})")
                        }
                    }
                    else -> {
                        event.apply { text = text.replace("${'$'}{sender}", "${senderName}(${sender.id})") }
                    }
                }

                event.run {
                    if (toCommandSender().permitteeId.hasPermission(AntiRecall.monitoredPermission ?: return)) {
                        when {
                            FlashImage in message -> {
                                if ("flashimage" in Config.type) {
                                    text = text.replace("${'$'}{type}", "闪照")
                                    msgBuilder.add(text)
                                    if (isMessage) message.forEach {
                                        if (it is FlashImage) msgBuilder.add(it.image)
                                    }
                                }
                            }
                            //不知道为什么发不了语音，先咕了
//                        Audio in message -> {
//                            if ("audio" in Config.type) {
//                                text = text.replace("${'$'}{type}", "语音")
//                                msgBuilder.add(text)
//                                if (isMessage) message.forEach {
//                                    if (it is OnlineAudio) {
//                                        msgBuilder.add(it.urlForDownload)
//                                        bulkSend(bot, OfflineAudio(it).toMessageChain())
//                                    }
//                                }
//                            }
//                        }
                            Image in message -> {
                                if ("image" in Config.type) {
                                    text = text.replace("${'$'}{type}", "图片")
                                    msgBuilder.add(text)
                                    if (isMessage) message.forEach {
                                        if (it is Image) msgBuilder.add(it)
                                    }
                                }
                            }
                            else -> return
                        }
                    }

                    val msg = when (isForwardMessage) {
                        true -> msgBuilder.build().toForwardMessage(sender, time).toMessageChain()
                        else -> msgBuilder.build()
                    }
                    bulkSend(bot, msg)
                }
            }
        }
    }
}