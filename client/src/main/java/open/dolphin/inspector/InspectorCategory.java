package open.dolphin.inspector;

/**
 * 浮動インスペクタの enum.
 * ID としての name, 表示タイトル title(), インスペクタのクラス clazz()
 *
 * @author pns
 */
public enum InspectorCategory {
    メモ("メモ", MemoInspector.class),
    カレンダー("来院歴", PatientVisitInspector.class),
    文書履歴("文書履歴", DocumentHistory.class),
    アレルギー("アレルギー", AllergyInspector.class),
    身長体重("身長体重", PhysicalInspector.class),
    病名("病名", DiagnosisInspector.class),
    関連文書("関連文書", FileInspector.class),
    なし("", null);

    private final String title;
    private final Class<? extends IInspector> clazz;
    private InspectorCategory(String t, Class<? extends IInspector> c) {
        title = t;
        clazz = c;
    }

    static Object get(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static int orderOfName(String name) {
        int index = -1;
        for (int i = 0; i < InspectorCategory.values().length; i++) {
            if (InspectorCategory.values()[i].name().equals(name)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public String title() {
        return title;
    }

    public Class<? extends IInspector> clazz() {
        return clazz;
    }
}
