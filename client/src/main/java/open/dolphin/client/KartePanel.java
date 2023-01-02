package open.dolphin.client;

import javax.swing.*;

/**
 * KartePanel abstract for KarteViewer and KarteEditor.
 * 実体は KartePanelFactory で作る.
 *
 * @author pns
 */
public abstract class KartePanel extends PrintablePanel {
    
    public abstract JTextPane getPTextPane();

    public abstract JTextPane getSoaTextPane();

    public abstract JLabel getTimeStampLabel();

    public abstract int getTimeStampPanelHeight();

    public abstract JPanel getTimeStampPanel();
}
