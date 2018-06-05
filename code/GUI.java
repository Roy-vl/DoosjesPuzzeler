import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUI extends javax.swing.JFrame {
    
    ProblemStatement PS = null;
    PackerStrategy strategy = new AutoSelectPack();

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        
        //Initialize Systemout Dump
        MessageConsole mc = new MessageConsole(SystemOut);
        mc.redirectOut(Color.green, System.out);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        SystemOut = new javax.swing.JTextArea();
        AlgoSelector = new javax.swing.JComboBox<>();
        PackButton = new javax.swing.JButton();
        EvaluateButton = new javax.swing.JButton();
        fileChooser = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImages(null);

        SystemOut.setColumns(20);
        SystemOut.setRows(5);
        jScrollPane1.setViewportView(SystemOut);

        AlgoSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AutoSelect", "BackTrackCorner", "GreedyCorner", "GreedyTrivial", "MultipleGreedyCorner" }));
        AlgoSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlgoSelectorActionPerformed(evt);
            }
        });

        PackButton.setText("Pack");
        PackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PackButtonMouseClicked(evt);
            }
        });

        EvaluateButton.setText("Evaluate");
        EvaluateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EvaluateButtonMouseClicked(evt);
            }
        });

        fileChooser.setText("Choose file");
        fileChooser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileChooserMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AlgoSelector, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EvaluateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileChooser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AlgoSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EvaluateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AlgoSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AlgoSelectorActionPerformed

        switch(AlgoSelector.getSelectedItem().toString()){
            case "AutoSelect" :
                strategy = new AutoSelectPack();
                break;
            case "BackTrackCorner" :
                strategy = new BacktrackCornerPack();
                break;
            case "GreedyCorner" :
                strategy = new GreedyCornerPack();
                break;
            case "MultipleGreedyCorner" :
                strategy = new MultipleGreedyCornerPack();
                break;
            case "GreedyTrivial":
                strategy = new GreedyTrivialPack();
                break;
            default:
                strategy = null;
        }
    }//GEN-LAST:event_AlgoSelectorActionPerformed

    private void PackButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PackButtonMouseClicked
        if(PS==null){
            System.out.println("No problem selected");
            return;
        }
        if(strategy==null){
            System.out.println("No strategy selected");
            return;
        }
        if(!strategy.applicable(PS)){
            System.out.println("Strategy not applicable to current problem");
            return;
        }
        
        long startTime = System.nanoTime();
        RectanglesContainer packedRC = strategy.pack(PS);
        long estimatedTime = System.nanoTime() - startTime;  

        System.out.println("Packing time : " + (double)(estimatedTime)/1000000 + "ms");
        
        packedRC.visualize();
    }//GEN-LAST:event_PackButtonMouseClicked

    private void EvaluateButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EvaluateButtonMouseClicked

        if(PS==null){
            System.out.println("No PS selected");
            return;
        }
        if(strategy==null){
            System.out.println("No strategy selected");
            return;
        }
        if(strategy instanceof AutoSelectPack){
            System.out.println("Evaluation should not be run with AutoSelect");
            return;
        }
        if(!strategy.applicable(PS)){
            System.out.println("Strategy not applicable to current problem");
            return;
        }
        
        double ct = 0;
        double cfr = 0;
        int tries = 100;
        for(int i=0;i<tries;i++){
            long startTime = System.nanoTime();
            RectanglesContainer packedRC = strategy.pack(PS);
            long estimatedTime = System.nanoTime() - startTime;
            ct += estimatedTime;
            cfr += (float)(packedRC.getRectanglesArea())/packedRC.getBoundingArea();
        }
        
        System.out.println("Average time(ms) of "+tries+" runs: "+ct/tries/1000000);
        System.out.println("Filling ratio of :"+cfr/tries);
    }//GEN-LAST:event_EvaluateButtonMouseClicked

    private void fileChooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileChooserMouseClicked
 
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        File[] files = dialog.getFiles();
        if (files != null && files.length > 0) {
            for (File file : files) { 
                try {
                    fileChooser.setText(file.getName());
                    PS = new ProblemStatement();
                    PS.parseInput(new Scanner(file));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }        
            }
        } else {
            fileChooser.setText("Choose problem");
            PS = null;
        }

    }//GEN-LAST:event_fileChooserMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> AlgoSelector;
    private javax.swing.JButton EvaluateButton;
    private javax.swing.JButton PackButton;
    private javax.swing.JTextArea SystemOut;
    private javax.swing.JButton fileChooser;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
