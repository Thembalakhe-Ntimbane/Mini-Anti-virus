
package miniantivirus;

public class ReportDialog extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(ReportDialog.class.getName());

    public ReportDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        reportArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        closeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(
                javax.swing.WindowConstants.DISPOSE_ON_CLOSE
        );
        setTitle("Scan Report");
        setPreferredSize(new java.awt.Dimension(600, 400));

        reportArea.setEditable(false);
        reportArea.setColumns(20);
        reportArea.setRows(5);
        jScrollPane1.setViewportView(reportArea);

        getContentPane().add(
                jScrollPane1,
                java.awt.BorderLayout.CENTER
        );

        jPanel1.setLayout(new java.awt.FlowLayout(2));

        closeBtn.setText("Close");
        closeBtn.addActionListener(this::closeBtnActionPerformed);
        jPanel1.add(closeBtn);

        getContentPane().add(
                jPanel1,
                java.awt.BorderLayout.PAGE_END
        );

        pack();
    }

    // Display the report supplied by MainWindow
    public void setReportText(String text) {
        reportArea.setText(text);
        reportArea.setCaretPosition(0);
    }

    private void closeBtnActionPerformed(
            java.awt.event.ActionEvent evt) {
        dispose();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            ReportDialog dialog =
                    new ReportDialog(new javax.swing.JFrame(), true);
            dialog.setReportText("Mini Antivirus Report");
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
    }

    private javax.swing.JButton closeBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea reportArea;
}
