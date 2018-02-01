package open.dolphin.infomodel;

import org.hibernate.search.bridge.StringBridge;

/**
 * ModuleModel の beanBytes からテキストを取り出すブリッジ.
 *
 * @author masuda, Masuda Naika
 */
public class ModuleModelBridge implements StringBridge {
    @Override
    public String objectToString(Object object) {

        byte[] beanBytes = (byte[]) object;
        InfoModel im = (InfoModel) ModelUtils.xmlDecode(beanBytes);
        String text;

        if (im instanceof ProgressCourse) {
            String xml = ((ProgressCourse) im).getFreeText();
            text = ModelUtils.extractText(xml);
        } else {
            text = im.toString();
        }

        return text;
    }
}
