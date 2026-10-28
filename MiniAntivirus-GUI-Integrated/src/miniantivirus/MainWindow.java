
package miniantivirus;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(MainWindow.class.getName());

    private Scanner scanner;
    private QuarantineMgr quarantineMgr = new QuarantineMgr();
    private SwingWorker<Report, Void> scanWorker;

    public MainWindow() {
    initComponents();

    startBtn.addActionListener(this::startBtnActionPerformed);
    stopBtn.addActionListener(this::stopBtnActionPerformed);
  
    restoreBtn.addActionListener(this::restoreBtnActionPerformed);
    deleteBtn.addActionListener(this::deleteBtnActionPerformed);
}

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pathField = new javax.swing.JTextField();
        browseBtn = new javax.swing.JButton();
        startBtn = new javax.swing.JButton();
        stopBtn = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        quarantineTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        quarantineBtn = new javax.swing.JButton();
        restoreBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        reportBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        progress = new javax.swing.JProgressBar();
        jPanel4 = new javax.swing.JPanel();
        dbLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mini Anti-Virus Software");

        jLabel1.setText("Folder:");
        jPanel1.add(jLabel1);

        pathField.setColumns(25);
        jPanel1.add(pathField);

        browseBtn.setText("Browse");
        browseBtn.addActionListener(this::browseBtnActionPerformed);
        jPanel1.add(browseBtn);

        startBtn.setText("Start Scan");
        jPanel1.add(startBtn);

        stopBtn.setText("Stop Scan");
        stopBtn.setEnabled(false);
        jPanel1.add(stopBtn);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        resultTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String[]{
                    "Type", "Severity", "Signature", "File Path"
                }
        ));
        jScrollPane1.setViewportView(resultTable);

        jTabbedPane1.addTab("Scan Results", jScrollPane1);

        quarantineTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null},
                    {null, null},
                    {null, null},
                    {null, null}
                },
                new String[]{
                    "File Path", "Date"
                }
        ));
        jScrollPane2.setViewportView(quarantineTable);

        jTabbedPane1.addTab("Quarantine", jScrollPane2);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(3, 2));

        quarantineBtn.setText("Quarantine");
        quarantineBtn.addActionListener(this::quarantineBtnActionPerformed);
        jPanel5.add(quarantineBtn);
        quarantineBtn.getAccessibleContext().setAccessibleName("Quarantine");

        restoreBtn.setText("Restore");
        restoreBtn.setEnabled(false);
        jPanel5.add(restoreBtn);

        deleteBtn.setForeground(new java.awt.Color(255, 51, 51));
        deleteBtn.setText("Delete");
        deleteBtn.setEnabled(false);
        jPanel5.add(deleteBtn);

        updateBtn.setText("Update");
        jPanel5.add(updateBtn);

        reportBtn.setText("Generate Report");
        jPanel5.add(reportBtn);

        jPanel2.add(jPanel5);

        jPanel3.setLayout(new java.awt.BorderLayout());

        statusLabel.setText("Status: Ready");
        jPanel3.add(statusLabel, java.awt.BorderLayout.CENTER);
        jPanel3.add(progress, java.awt.BorderLayout.PAGE_END);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(2));

        dbLabel.setText("Database last updated: --");
        jPanel4.add(dbLabel);

        jPanel2.add(jPanel4);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }
    // </editor-fold>

    // Browse button
    private void browseBtnActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(
                JFileChooser.FILES_AND_DIRECTORIES
        );

        chooser.setDialogTitle("Select a file or folder to scan");

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            pathField.setText(selectedFile.getAbsolutePath());
            statusLabel.setText("Status: Ready to scan");
        }
    }

    // Start Scan button
    private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String path = pathField.getText().trim();

        if (path.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Please select a file or folder first.",
                    "No scan path",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            scanner = new Scanner(path);

            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            progress.setValue(0);
            progress.setIndeterminate(true);
            statusLabel.setText("Status: Scanning...");

            scanWorker = new SwingWorker<Report, Void>() {

                @Override
                protected Report doInBackground() {
                    return scanner.startScan();
                }

                @Override
                protected void done() {
                    progress.setIndeterminate(false);
                    startBtn.setEnabled(true);
                    stopBtn.setEnabled(false);

                    try {
                        Report report = get();

                        DefaultTableModel model =
                                (DefaultTableModel) resultTable.getModel();

                        model.setRowCount(0);

                        for (String filePath : scanner.getSuspiciousFiles()) {
                            model.addRow(new Object[]{
                                "Suspicious extension",
                                "Unknown",
                                "EXE extension",
                                filePath
                            });
                        }

                        progress.setValue(100);

                        if (scanner.isStopRequested()) {
                            statusLabel.setText("Status: Scan stopped");
                        } else {
                            statusLabel.setText("Status: Scan completed");
                        }

                        String message;

                        if (scanner.isStopRequested()) {
                            message = "Scan stopped.\nFiles flagged: "
                                    + report.threats;
                        } else {
                            message = "Scan completed.\nFiles flagged: "
                                    + report.threats;
                        }

                        javax.swing.JOptionPane.showMessageDialog(
                                MainWindow.this,
                                message,
                                "Scan Report",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );

                    } catch (Exception ex) {
                        statusLabel.setText("Status: Scan failed");

                        javax.swing.JOptionPane.showMessageDialog(
                                MainWindow.this,
                                "Could not complete scan: "
                                        + ex.getMessage(),
                                "Scan Error",
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            scanWorker.execute();

        } catch (IllegalArgumentException ex) {
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            progress.setIndeterminate(false);

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Invalid scan path",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Stop Scan button
    private void stopBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (scanner != null && scanner.isScanning()) {
            scanner.stopScan();
            stopBtn.setEnabled(false);
            statusLabel.setText("Status: Stopping scan...");
        }
    }

    // Quarantine button
    private void quarantineBtnActionPerformed(java.awt.event.ActionEvent evt) {
    int selectedRow = resultTable.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Please select a suspicious file first.",
                "No File Selected",
                javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    String filePath = resultTable.getValueAt(selectedRow, 3).toString();

    int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Move this file to quarantine?\n" + filePath,
            "Confirm Quarantine",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
    );

    if (confirm != javax.swing.JOptionPane.YES_OPTION) {
        return;
    }

  
    File quarantinedFile = quarantineMgr.quarantine(filePath);

    if (quarantinedFile != null) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "File moved to quarantine successfully.",
                "Quarantine Successful",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
        refreshQuarantineTable();
    } else {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Could not quarantine the selected file.",
                "Quarantine Failed",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
    }
}
private void refreshQuarantineTable() {
    DefaultTableModel model =
            (DefaultTableModel) quarantineTable.getModel();

    model.setRowCount(0);

    for (File file : quarantineMgr.getQuarantinedFiles()) {
        model.addRow(new Object[]{
            file.getAbsolutePath(),
            new java.util.Date(file.lastModified())
        });
    }

    restoreBtn.setEnabled(quarantineTable.getRowCount() > 0);
    deleteBtn.setEnabled(quarantineTable.getRowCount() > 0);
}
private void restoreBtnActionPerformed(java.awt.event.ActionEvent evt) {
    int selectedRow = quarantineTable.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Please select a quarantined file first.",
                "No File Selected",
                javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    String filePath = quarantineTable.getValueAt(selectedRow, 0).toString();

    File file = new File(filePath);
    quarantineMgr.restore(file);

    refreshQuarantineTable();

    javax.swing.JOptionPane.showMessageDialog(
            this,
            "Restore operation attempted. Please check whether the file returned to its original location.",
            "Restore",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
    );
}
private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {
    int selectedRow = quarantineTable.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Please select a quarantined file first.",
                "No File Selected",
                javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    String filePath = quarantineTable.getValueAt(selectedRow, 0).toString();

    int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Permanently delete this quarantined file?\n" + filePath,
            "Confirm Delete",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
    );

    if (confirm != javax.swing.JOptionPane.YES_OPTION) {
        return;
    }

    quarantineMgr.delete(new File(filePath));
    refreshQuarantineTable();

    javax.swing.JOptionPane.showMessageDialog(
            this,
            "Delete operation attempted. Please check the quarantine folder.",
            "Delete",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
    );
}
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(
                () -> new MainWindow().setVisible(true)
        );
    }

    // Variables declaration
    private javax.swing.JButton browseBtn;
    private javax.swing.JLabel dbLabel;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField pathField;
    private javax.swing.JProgressBar progress;
    private javax.swing.JButton quarantineBtn;
    private javax.swing.JTable quarantineTable;
    private javax.swing.JButton reportBtn;
    private javax.swing.JButton restoreBtn;
    private javax.swing.JTable resultTable;
    private javax.swing.JButton startBtn;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton stopBtn;
    private javax.swing.JButton updateBtn;
    // End of variables declaration
}
