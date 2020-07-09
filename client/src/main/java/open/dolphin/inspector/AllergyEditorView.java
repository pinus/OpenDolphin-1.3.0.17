package open.dolphin.inspector;

import javax.swing.*;

public class AllergyEditorView extends JPanel {

    private JTextField factorFld;
    private JTextField identifiedFld;
    private JLabel causeLbl;
    private JLabel levelLbl;
    private JLabel memoLbl;
    private JLabel dateLbl;
    private JTextField memoFld;
    private JComboBox<String> reactionCombo;
    
    public AllergyEditorView() {
        initComponents();
    }

    private void initComponents() {
        causeLbl = new JLabel("要因：");
        levelLbl = new JLabel("反応：");
        memoLbl = new JLabel("メモ：");
        dateLbl = new JLabel("同定日：");

        factorFld = new JTextField(30);
        identifiedFld = new JTextField();
        memoFld = new JTextField(30);
        reactionCombo = new JComboBox<>();
        reactionCombo.setModel(new DefaultComboBoxModel<>(new String[]{"severe", "moderate", "mild", "none"}));

        JPanel causePanel = new JPanel();
        causePanel.setLayout(new BoxLayout(causePanel, BoxLayout.X_AXIS));
        causePanel.add(causeLbl); causePanel.add(factorFld);

        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.add(levelLbl); datePanel.add(reactionCombo); datePanel.add(Box.createHorizontalStrut(70));
        datePanel.add(dateLbl);datePanel.add(identifiedFld);

        JPanel memoPanel = new JPanel();
        memoPanel.setLayout(new BoxLayout(memoPanel, BoxLayout.X_AXIS));
        memoPanel.add(memoLbl); memoPanel.add(memoFld);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(causePanel); add(memoPanel); add(datePanel);
    }

    public JTextField getFactorFld() {
        return factorFld;
    }

    public JTextField getIdentifiedFld() {
        return identifiedFld;
    }

    public JTextField getMemoFld() {
        return memoFld;
    }

    public JComboBox<String> getReactionCombo() {
        return reactionCombo;
    }
}
