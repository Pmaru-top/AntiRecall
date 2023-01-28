package top.cutestar.antiRecall

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    @ValueDescription("�������� trueΪ���� falseΪ����")
    var enable by value(true)

    @ValueDescription("��Ϣ����ʱ�� ��λ:���� ֻ֧�������� ע:���ù�����ܻᵼ�� ������Ϣû�еõ������ �ڴ����")
    var cacheTime by value(30)
//audio(����), ��ʱʵ�ֲ���
    @ValueDescription("ת����Ϣ������ ��ѡrecall(������Ϣ),flashimage(����),image(ͼƬ)")
    var type by value(mutableSetOf("recall","flashimage"))

    @ValueDescription("����������Ϣ�ӳ� ��λ:����")
    var sendDelay by value((500).toLong())

    @ValueDescription("ת����Ϣ����Ϣ�� ��ʾ������:\"${'$'}{sender}\" ��ʾ��Ϣ����:\"${'$'}{type}\" ��ʾԭ��Ϣ:\"${'$'}{message}\" ʹ�úϲ�ת���ķ�ʽ����:\"${'$'}{forwardMessage}\"")
    var messageText by value(
        """${'$'}{sender}������һ��${'$'}{type}��Ϣ:
${'$'}{message}""")
}