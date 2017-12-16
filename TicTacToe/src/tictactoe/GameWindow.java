/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.event.MouseAdapter;
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
    private void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playingLabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
        chanceDisplay.setText("");
        count = 0;
    }

    private void endGame(String currentPlayer) {
        if (currentPlayer != null) {
            JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " Wins !!!");
        }
        int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
        if (playAgainOrNot == JOptionPane.YES_OPTION) {
            reset();
        } else {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }
    
    private void check(int currentPlayer) {
        String currentPlayerMove;
        if (currentPlayer == 1) {
            currentPlayerMove = playerTwoOption;
        } else {
            currentPlayerMove = playerOneOption;
        }
        boolean isGameOver = false, flag;

        // checking horizontal match
        for (int i = 0; i < 3; i++) {
            flag = true;
            for (int j = 0; j < 3; j++) {
                if (!playingLabels[i][j].getText().equals(currentPlayerMove)) {
                    flag = false;
                }
            }
            if (flag) {
                isGameOver = true;
            }
        }
        // checking vertical match
        for (int j = 0; j < 3; j++) {
            flag = true;
            for (int i = 0; i < 3; i++) {
                if (!playingLabels[i][j].getText().equals(currentPlayerMove)) {
                    flag = false;
                }
            }
            if (flag) {
                isGameOver = true;
            }
        }

        // checking diagonals match
        flag = true;
        for (int i = 0; i < 3; i++) {
            if (!playingLabels[i][i].getText().equals(currentPlayerMove)) {
                flag = false;
            }
        }
        if (flag) {
            isGameOver = true;
        }

        flag = true;
        for (int i = 0; i < 3; i++) {
            if (!playingLabels[i][2 - i].getText().equals(currentPlayerMove)) {
                flag = false;
            }
        }
        if (flag) {
            isGameOver = true;
        }

        if (isGameOver) {
            if (currentPlayer == 0) {
                endGame(playerNameOne);
            } else {
                endGame(playerNameTwo);
            }
        }
        //For Draw Match
        if (count == 9 && isGameOver == false) {
            JOptionPane.showMessageDialog(this, "Its A Draw!!");
            reset();
            endGame(null);
        }

    }
    
    private void cellClicked(MouseEvent evt){
        boolean player = false;
        JLabel currentCell = (JLabel) evt.getComponent();
        if (!(".".equals(currentCell.getText()))) {
            JOptionPane.showMessageDialog(this, "This Position Already Chosen");
        } else {
            count++;
            if (currentPlayer == 0) {
                currentCell.setText("" + playerOneOption);
                player = true;
            } else {
                currentCell.setText("" + playerTwoOption);
            }
            currentPlayer = (currentPlayer + 1) % 2;
            check((currentPlayer + 1) % 2);
        }
        if (player) {
            chanceDisplay.setText("" + playerNameTwo + "'s Chance");
        } else {
            chanceDisplay.setText("" + playerNameOne + "'s Chance");
        }
    }
    
    private void addPlayingComponents(JPanel playingArea){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playingLabels[i][j] = new JLabel(".");
                playingLabels[i][j].setFont(new java.awt.Font("Ubuntu", 1, 18));
                playingLabels[i][j].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                playingLabels[i][j].setVisible(true);
                playingLabels[i][j].setLocation(i * (CELLSIZE + SPACEBETWEENCELLS), j * (CELLSIZE + SPACEBETWEENCELLS));
                playingLabels[i][j].setSize(CELLSIZE, CELLSIZE);
                playingArea.add(playingLabels[i][j]);

                playingLabels[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
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
        Object[] options = {"Online",
            "Offline"};
        int n = JOptionPane.showOptionDialog(this,
                "Type of Game",
                "Essential Information",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, 
                options, 
                options[0]);
        if(n == 1){
            joinOnilneButton.setEnabled(false);
            hostOnlineButton.setEnabled(false);
        }
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
        playerOneOption1 = new javax.swing.JRadioButton();
        playerOneOption2 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        playerTwoOption1 = new javax.swing.JRadioButton();
        playerTwoOption2 = new javax.swing.JRadioButton();
        startButton = new javax.swing.JButton();
        chanceDisplay = new javax.swing.JLabel();
        joinOnilneButton = new javax.swing.JButton();
        hostOnlineButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        jLabel1.setText("Player One\n");

        buttonGroup1.add(playerOneOption1);
        playerOneOption1.setText("X");
        playerOneOption1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerOneOption1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(playerOneOption2);
        playerOneOption2.setText("O");
        playerOneOption2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerOneOption2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Player Two\n");

        buttonGroup2.add(playerTwoOption1);
        playerTwoOption1.setText("X");
        playerTwoOption1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerTwoOption1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(playerTwoOption2);
        playerTwoOption2.setText("O");
        playerTwoOption2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerTwoOption2ActionPerformed(evt);
            }
        });

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        joinOnilneButton.setText("JOIN");
        joinOnilneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinOnilneButtonActionPerformed(evt);
            }
        });

        hostOnlineButton.setText("HOST");
        hostOnlineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostOnlineButtonActionPerformed(evt);
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
                    .addComponent(playerOneOption1)
                    .addComponent(playerOneOption2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerTwoOption1)
                    .addComponent(playerTwoOption2))
                .addGap(48, 48, 48))
            .addGroup(layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(startButton)
                .addGap(50, 50, 50)
                .addComponent(joinOnilneButton)
                .addGap(18, 18, 18)
                .addComponent(hostOnlineButton)
                .addGap(0, 33, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chanceDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(title)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2)))
                        .addGap(41, 41, 41))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerOneOption1)
                    .addComponent(playerTwoOption1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerOneOption2)
                    .addComponent(playerTwoOption2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                .addComponent(chanceDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(joinOnilneButton)
                    .addComponent(hostOnlineButton))
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playerOneOption1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerOneOption1ActionPerformed
        // TODO add your handling code here:
        if (playerTwoOption != "X" || playerTwoOption == null) {
            playerOneOption = "X";
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            playerOneOption2.setSelected(true);
        }
    }//GEN-LAST:event_playerOneOption1ActionPerformed

    private void playerOneOption2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerOneOption2ActionPerformed
        // TODO add your handling code here:
        if (playerTwoOption != "O" || playerTwoOption == null) {
            playerOneOption = "O";
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            playerOneOption1.setSelected(true);
        }
    }//GEN-LAST:event_playerOneOption2ActionPerformed

    private void playerTwoOption1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerTwoOption1ActionPerformed
        // TODO add your handling code here:
        if (playerOneOption != "X" || playerOneOption == null) {
            playerTwoOption = "X";
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            playerTwoOption2.setSelected(true);
        }
    }//GEN-LAST:event_playerTwoOption1ActionPerformed

    private void playerTwoOption2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerTwoOption2ActionPerformed
        // TODO add your handling code here:
        if (playerOneOption != "O" || playerOneOption == null) {
            playerTwoOption = "O";
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            playerTwoOption1.setSelected(true);
        }
    }//GEN-LAST:event_playerTwoOption2ActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        // TODO add your handling code here:
        chanceDisplay.setText("" + playerNameOne + "'s Chance");
        if ((playerOneOption != null) && (playerTwoOption != null)) {
            playingArea.setVisible(true);
        } else {
            if (playerOneOption == null && playerTwoOption != null) {
                JOptionPane.showMessageDialog(this, "Player One Please Select Your Option and Start The Game");
            } else {
                if (playerTwoOption == null && playerOneOption != null) {
                    JOptionPane.showMessageDialog(this, "Player Two Please Select Your Option and Start The Game");
                } else {
                    JOptionPane.showMessageDialog(this, "Player One And Player Two ,Please Select Your Option and Start The Game");
                }
            }
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void joinOnilneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinOnilneButtonActionPerformed
        // TODO add your handling code here:
        String ipAddress = JOptionPane.showInputDialog(this, "Enter IP Address");
        String port = JOptionPane.showInputDialog(this, "Enter Port Number");

    }//GEN-LAST:event_joinOnilneButtonActionPerformed

    private void hostOnlineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostOnlineButtonActionPerformed
       
    }//GEN-LAST:event_hostOnlineButtonActionPerformed

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
    private javax.swing.JLabel chanceDisplay;
    private javax.swing.JButton hostOnlineButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton joinOnilneButton;
    private javax.swing.JRadioButton playerOneOption1;
    private javax.swing.JRadioButton playerOneOption2;
    private javax.swing.JRadioButton playerTwoOption1;
    private javax.swing.JRadioButton playerTwoOption2;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
