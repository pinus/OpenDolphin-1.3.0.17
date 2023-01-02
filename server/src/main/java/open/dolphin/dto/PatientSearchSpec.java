package open.dolphin.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PatientSearchSpec.
 *
 * @author Minagawa, Kazushi
 * @author pns
 */
public class PatientSearchSpec implements Serializable {

    public enum SEARCH {
        ID, NAME, KANA, ROMAN, DATE, BIRTHDAY, MEMO, FULLTEXT, QUERY, REGEXP
    }

    /**
     * 検索方法.
     */
    private SEARCH type;
    /**
     * 患者番号.
     */
    private String patientId;
    /**
     * 患者名.
     */
    private String name;
    /**
     * 受診日.
     */
    private String date;
    /**
     * 誕生日.
     */
    private String birthday;
    /**
     * 検索文字列
     */
    private String searchText;
    /**
     * 絞り込み検索のための PatientModel pk リスト.
     */
    private final List<Long> narrowingIds = new ArrayList<>();

    public void setType(SEARCH type) { this.type = type; }

    public SEARCH getType() { return type; }

    public void setName(String name) {
        // 名前に全角スペースが入っていた場合, 半角に置換する
        this.name = Objects.nonNull(name) ? name.replace('　', ' ') : "";
    }

    public String getName() { return name; }

    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getBirthday() { return birthday; }

    public void setPatientId(String patientId) {
        // ID が６桁未満の数字の時は, 先頭にゼロを補って６桁にする.ただし頭が 0 の時はそのまま通す.
        this.patientId = Objects.nonNull(patientId) && patientId.charAt(0) != '0'
            ? String.format("%6s", patientId).replace(' ', '0')
            : patientId;
    }

    public String getPatientId() { return patientId; }

    public void setSearchText(String text) { this.searchText = text; }

    public String getSearchText() { return searchText; }

    public void setNarrowingIds(List<Long> list) {
        narrowingIds.clear();
        narrowingIds.addAll(list);
    }

    public List<Long> getNarrowingList() { return narrowingIds; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }
}
