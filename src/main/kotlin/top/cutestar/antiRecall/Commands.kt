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
    description = "AR�����ز������Ȩ��"
) {
    @Description("��������")
    @SubCommand("Enable", "en", "����")
    suspend fun CommandSender.enable(@Name("true/false") isEnable: Boolean) {
        Config.enable = isEnable
        sendMessage("����������Ϊ ${Config.enable}")
    }

    @Description("������Ϣ����ʱ�� ��λ:���� ֻ֧��������")
    @SubCommand("CacheTime", "ca", "����ʱ��")
    suspend fun CommandSender.cacheTime(@Name("����") time: Int) {
        Config.cacheTime = time
        sendMessage("����ʱ��������Ϊ ${Config.cacheTime}")
    }

    @Description("����ת����Ϣ����Ϣ�� ��ϸ�ο������ļ�")
    @SubCommand("ForwardMsg", "fm", "ת��")
    suspend fun CommandSender.forwardMsg(@Name("��ʽ�ı�") s: String) {
        Config.messageText = s
        sendMessage("��ʽ�ı�������Ϊ ${Config.cacheTime}")
    }

    @Description("��������")
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
        sendMessage("������")
    }
}