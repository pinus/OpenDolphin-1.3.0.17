package open.dolphin.infomodel;

import open.dolphin.util.ModelUtils;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ModuleModel の beanBytes からテキストを取り出すブリッジ.
 *
 * @author masuda, Masuda Naika
 */
public class ModuleModelBridge implements ValueBridge<Object, String> {
    @Override
    public String toIndexedValue(Object object, ValueBridgeToIndexedValueContext context) {

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

    @Override
    public Object fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
        return ValueBridge.super.fromIndexedValue(value, context);
    }

    @Override
    public String parse(String value) {
        return ValueBridge.super.parse(value);
    }

    @Override
    public boolean isCompatibleWith(ValueBridge<?, ?> other) {
        return ValueBridge.super.isCompatibleWith(other);
    }

    @Override
    public void close() {
        ValueBridge.super.close();
    }
}
