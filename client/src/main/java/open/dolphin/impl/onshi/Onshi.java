package open.dolphin.impl.onshi;

import open.dolphin.client.AbstractChartDocument;
import open.dolphin.delegater.OrcaDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.helper.StringTool;
import open.dolphin.orca.orcadao.bean.OnshiYakuzai;
import open.dolphin.ui.PNSScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Onshi extends AbstractChartDocument {
    // Title
    private static final String TITLE = "オン資";

    private JTextPane textPane;

    public Onshi() {
        setTitle(TITLE);
    }

    @Override
    public void start() {
        JPanel myPanel = getUI();
        myPanel.setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setMargin(new Insets(7, 7, 7, 7));
        PNSScrollPane scroller = new PNSScrollPane(textPane);
        scroller.setVerticalScrollBarPolicy(PNSScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(PNSScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        myPanel.add(scroller);
    }

    @Override
    public void enter() {
        String ptnum = getContext().getPatient().getPatientId();
        OrcaDelegater delegater = new OrcaDelegater();

        DBTask<List<OnshiYakuzai>> task = new DBTask<List<OnshiYakuzai>>() {

            @Override
            protected List<OnshiYakuzai> doInBackground() {
                OrcaDelegater delegater = new OrcaDelegater();
                return delegater.getDrugHistory(ptnum);
            }

            @Override
            public void succeeded(List<OnshiYakuzai> onshiYakuzai) {
                StringBuilder sb = new StringBuilder();
                String date = "";
                int shoho = 0;
                int chozai = 0;
                for (OnshiYakuzai o : onshiYakuzai) {
                    if (!date.equals(o.getIsoDate()) || o.getShohoSeqnum() != shoho || o.getChozaiSeqnum() != chozai) {
                        date = o.getIsoDate();
                        shoho = o.getShohoSeqnum();
                        chozai = o.getChozaiSeqnum();
                        sb.append(String.format("%s 医療機関:%d 薬局:%d\n", date, shoho, chozai));
                    }
                    String yakuzainame = o.getYakuzainame();
                    yakuzainame = StringTool.toHankakuNumber(yakuzainame);
                    yakuzainame = StringTool.toHankakuUpperLower(yakuzainame);
                    yakuzainame = yakuzainame.replaceAll("．", ".");

                    sb.append(String.format("    %s ", yakuzainame));
                    if (o.getYohoname().equals("")) {
                        // 外用剤
                        sb.append(String.format("%s%s %s\n", o.getSuryo(), o.getTaniname(), o.getShiji()));
                    } else {
                        sb.append(String.format("%s%s %s %sTD\n", o.getSuryo(), o.getTaniname(), o.getYohoname(), o.getKaisu()));
                    }
                }
                textPane.setText(sb.toString());
            }
        };
        task.execute();
    }
}
