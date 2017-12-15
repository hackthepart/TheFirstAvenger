/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author nikhil
 */
public class GameWindow extends javax.swing.JFrame {

    /**
     * Creates new form GameWindow
     */
    private JLabel playingLabels[][] = new JLabel[3][3];
    private final int PLAYINGAREAX = 110, PLAYINGAREAY = 100, CELLSIZE = 40, SPACEBETWEENCELLS = 10;
    private int currentPlayer = 0;
    private String playerNameOne;
    private String playerNameTwo;
    private String playerOneOption = null;
    private String playerTwoOption = null;
    private Integer count = 0;
    JPanel playingArea = new JPanel();
    private void reset(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                playingLabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
        jLabel3.setText("");
        count = 0;
    }

    private void endGame(int currentPlayer){
        JOptionPane.showMessageDialog(null, "Player " + (currentPlayer + 1) + " Wins !!!");
        int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
        if(playAgainOrNot == JOptionPane.YES_OPTION)
            reset();
        else 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        
    }
    
    private void check(int currentPlayer){
        String currentPlayerMove = "O";
        if(currentPlayer == 1)
            currentPlayerMove = playerTwoOption;
        boolean isGameOver = false, flag;

        // checking horizontal match
        for(int i = 0; i < 3; i++){
            flag = true;
            for( int j = 0; j < 3; j++)
                if(!playingLabels[i][j].getText().equals(currentPlayerMove))
                    flag = false;
            if(flag)
                isGameOver = true;
        }
        // checking vertical match
        for(int j = 0; j < 3; j++){
            flag = true;
            for( int i = 0; i < 3; i++)
                if(!playingLabels[i][j].getText().equals(currentPlayerMove))
                    flag = false;
            if(flag)
                isGameOver = true;
        }
        
        // checking diagonals match
        flag = true;
        for(int i = 0; i < 3; i++){
            if(!playingLabels[i][i].getText().equals(currentPlayerMove))
                flag = false;
        }
        if(flag)
            isGameOver = true;
        
        flag = true;
        for(int i = 0; i < 3; i++){
            if(!playingLabels[i][2 - i].getText().equals(currentPlayerMove))
                flag = false;
        }
        if(flag)
            isGameOver = true;
        
        if(isGameOver)
            endGame(currentPlayer);
        if(count == 9 && isGameOver == false){
            JOptionPane.showMessageDialog(this,"Its A Draw!!");
            reset();
        }
        
    }
    
    private void cellClicked(MouseEvent evt){
        boolean player = false;
        JLabel currentCell = (JLabel) evt.getComponent();
        if(!(".".equals(currentCell.getText()))){
                JOptionPane.showMessageDialog(this,"This Position Already Chosen");
            }
        else{
            count++;
        if(currentPlayer == 0){
            currentCell.setText(""+playerOneOption);
            player = true;
        }
        else{
            currentCell.setText(""+playerTwoOption);
        }
        currentPlayer = (currentPlayer + 1) % 2;
        check( (currentPlayer + 1) % 2 );
        }
        if(player){
            jLabel3.setText(""+playerNameTwo);
        }else{
            jLabel3.setText(""+playerNameOne);
        }
    }
    
    private void addPlayingComponents(JPanel playingArea){
        for(int i = 0; i < 3; i++){
            for( int j = 0; j< 3 ; j++){
                playingLabels[i][j] = new JLabel(".");
                playingLabels[i][j].setFont(new java.awt.Font("Ubuntu", 1, 18));
                playingLabels[i][j].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                playingLabels[i][j].setVisible(true);
                playingLabels[i][j].setLocation(i * (CELLSIZE + SPACEBETWEENCELLS), j * (CELLSIZE + SPACEBETWEENCELLS));
                playingLabels[i][j].setSize(CELLSIZE, CELLSIZE);
                playingArea.add(playingLabels[i][j]);
                
                playingLabels[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt){
                        cellClicked(evt);
                    }
                });
            }
        }
    }
    
    public GameWindow() {
        initComponents();
        
        // Adding panel
        playingArea.setLocation(PLAYINGAREAX, PLAYINGAREAY);
        playingArea.setSize( 3 * (CELLSIZE + SPACEBETWEENCELLS) , 3 * (CELLSIZE + SPACEBETWEENCELLS));
        playingArea.setVisible(false);
        playingArea.setLayout(null);
        add(playingArea);
        addPlayingComponents(playingArea);
        playerNameOne = JOptionPane.showInputDialog("Enter Player One Name");
        jLabel1.setText(jLabel1.getText()+" "+playerNameOne);
        playerNameTwo = JOptionPane.showInputDialog("Enter Player Two Name To Start");
        jLabel2.setText(jLabel2.getText()+" "+playerNameTwo);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        title = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        jLabel1.setText("Player One\n");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("X");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("O");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Player Two\n");

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setText("X");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("O");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addGap(48, 48, 48))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(title))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                                .addComponent(jLabel2)))))
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        if(playerTwoOption != "X" || playerTwoOption == null){
        playerOneOption = "X";
        }else{
            JOptionPane.showMessageDialog(this,"Option Already Chosen by other oponent");
            jRadioButton2.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        if(playerTwoOption != "O" || playerTwoOption == null){
        playerOneOption = "O";
        }else{
            JOptionPane.showMessageDialog(this,"Option Already Chosen by other oponent");
            jRadioButton1.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
         if(playerOneOption != "X" || playerOneOption == null){
        playerTwoOption = "X";
         }else{
             JOptionPane.showMessageDialog(this,"Option Already Chosen by other oponent");
            jRadioButton4.setSelected(true);
        }  
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
        if(playerOneOption != "O" || playerOneOption == null){
        playerTwoOption = "O";
        }else{
            JOptionPane.showMessageDialog(this,"Option Already Chosen by other oponent");
            jRadioButton3.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jLabel3.setText(""+playerNameOne);
        if((playerOneOption != null)&&(playerTwoOption != null) ){
           playingArea.setVisible(true);
        }
        else{
            if(playerOneOption == null && playerTwoOption != null){
                JOptionPane.showMessageDialog(this,"Player One Please Select Your Option and Start The Game");
            }else{
                if(playerTwoOption == null && playerOneOption != null){
                     JOptionPane.showMessageDialog(this,"Player Two Please Select Your Option and Start The Game");
                }else{
                     JOptionPane.showMessageDialog(this,"Player One And Player Two ,Please Select Your Option and Start The Game");
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
