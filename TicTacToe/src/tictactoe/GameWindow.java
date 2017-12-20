/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.event.MouseAdapter;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author nikhil
 */
public class GameWindow extends javax.swing.JFrame {

    /**
     * Creates new form GameWindow
     */
    private static NetworkManager netManage = new NetworkManager();
    protected boolean server = false;
    protected boolean client = false;
    private CheckPoint playingLabels[][] = new CheckPoint[3][3];
    private final int PLAYINGAREAX = 110, PLAYINGAREAY = 100, CELLSIZE = 40, SPACEBETWEENCELLS = 10;
    private int currentPlayer = 0;
    private String playerNameOne = "";
    private String playerNameTwo = "";
    private String playerOneOption = null;
    private String playerTwoOption = null;
    private Integer count = 0;
    private boolean online;
    private boolean playing = false;
    JPanel playingArea = new JPanel();
    protected static GameWindow gm;

    private void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playingLabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
        chanceDisplayer.setText("" + playerNameOne + "'s Chance");
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

        if (count == 9 && isGameOver == false) {
            JOptionPane.showMessageDialog(this, "Its A Draw!!");
            reset();
            endGame(null);
        }

    }

    private void cellClicked(MouseEvent evt) throws IOException {
        CheckPoint currentCell = (CheckPoint) evt.getComponent();
        if (playing && online) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    cellClickedSub(currentCell);
                    playing = false;
                    try {
                        sendMove(currentCell.row, currentCell.column);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t1.start();
        } else {
            if (!online) {
                cellClickedSub(currentCell);
            }
        }
    }

    private void cellClickedSub(CheckPoint clickedLabel) {
        boolean player = false;
        if (!(".".equals(clickedLabel.getText())) && !online) {
            JOptionPane.showMessageDialog(this, "This Position Already Chosen");
        } else {
            count++;
            if (currentPlayer == 0) {
                clickedLabel.setText("" + playerOneOption);
                player = true;
            } else {
                clickedLabel.setText("" + playerTwoOption);
            }
            currentPlayer = (currentPlayer + 1) % 2;
            check((currentPlayer + 1) % 2);
        }
        if (player && !client) {
            chanceDisplayer.setText("|" + playerNameTwo + "'s Chance|");
        } else {
            if (!client) {
                chanceDisplayer.setText("|" + playerNameOne + "'s Chance|");
            } else {
            }
            if (client && playing) {
                chanceDisplayer.setText("|" + playerNameOne + "'s Chance|");
            } else {
                chanceDisplayer.setText("|" + playerNameTwo + "'s Chance|");
            }
        }
    }

    private void addPlayingComponents(JPanel playingArea) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playingLabels[i][j] = new CheckPoint(".");
                playingLabels[i][j].row = i;
                playingLabels[i][j].column = j;
                playingLabels[i][j].setFont(new java.awt.Font("Ubuntu", 1, 18));
                playingLabels[i][j].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                playingLabels[i][j].setVisible(true);
                playingLabels[i][j].setLocation(i * (CELLSIZE + SPACEBETWEENCELLS), j * (CELLSIZE + SPACEBETWEENCELLS));
                playingLabels[i][j].setSize(CELLSIZE, CELLSIZE);
                playingArea.add(playingLabels[i][j]);

                playingLabels[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        try {
                            cellClicked(evt);
                        } catch (IOException ex) {
                            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        }
    }

    public GameWindow() {
        initComponents();

        // Adding panel
        playingArea.setLocation(PLAYINGAREAX, PLAYINGAREAY);
        playingArea.setSize(3 * (CELLSIZE + SPACEBETWEENCELLS), 3 * (CELLSIZE + SPACEBETWEENCELLS));
        playingArea.setVisible(false);
        playingArea.setLayout(null);
        add(playingArea);
        addPlayingComponents(playingArea);
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
        if (n == 1) {
            joinButton.setEnabled(false);
            hostButton.setEnabled(false);
        } else {
            playerTwoOption1.setEnabled(false);
            playerTwoOption2.setEnabled(false);
            online = true;
        }
        playerNameOne = JOptionPane.showInputDialog("Enter Player One Name");
        playerNameDisplayer1.setText(playerNameDisplayer1.getText() + " " + playerNameOne);
        if (!online) {
            playerNameTwo = JOptionPane.showInputDialog("Enter Player Two Name To Start");
            playerNameDisplayer2.setText(playerNameDisplayer2.getText() + " " + playerNameTwo);
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
        playerNameDisplayer1 = new javax.swing.JLabel();
        playerOneOption1 = new javax.swing.JRadioButton();
        playerOneOption2 = new javax.swing.JRadioButton();
        playerNameDisplayer2 = new javax.swing.JLabel();
        playerTwoOption1 = new javax.swing.JRadioButton();
        playerTwoOption2 = new javax.swing.JRadioButton();
        startButton = new javax.swing.JButton();
        chanceDisplayer = new javax.swing.JLabel();
        joinButton = new javax.swing.JButton();
        hostButton = new javax.swing.JButton();
        connectionTrancation = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TIC TAC TOE");
        setBackground(new java.awt.Color(206, 58, 50));

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        playerNameDisplayer1.setText("Player One\n");

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

        playerNameDisplayer2.setText("Player Two\n");

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

        joinButton.setText("JOIN");
        joinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinButtonActionPerformed(evt);
            }
        });

        hostButton.setText("HOST");
        hostButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerNameDisplayer1)
                    .addComponent(playerOneOption1)
                    .addComponent(playerOneOption2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerTwoOption1)
                    .addComponent(playerTwoOption2))
                .addGap(48, 48, 48))
            .addGroup(layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 158, Short.MAX_VALUE)
                        .addComponent(playerNameDisplayer2))
                    .addComponent(title))
                .addGap(41, 41, 41))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionTrancation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(startButton)
                .addGap(30, 30, 30)
                .addComponent(joinButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hostButton)
                .addGap(0, 59, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(chanceDisplayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerNameDisplayer2)
                    .addComponent(playerNameDisplayer1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerOneOption1)
                    .addComponent(playerTwoOption1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerOneOption2)
                    .addComponent(playerTwoOption2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addComponent(chanceDisplayer, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(joinButton)
                    .addComponent(hostButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectionTrancation, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playerOneOption1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerOneOption1ActionPerformed
        // TODO add your handling code here:
        if (playerTwoOption != "X" || playerTwoOption == null) {
            playerOneOption = "X";
            if (online) {
                playerTwoOption = "O";
            }
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            playerOneOption2.setSelected(true);

        }
    }//GEN-LAST:event_playerOneOption1ActionPerformed

    private void playerOneOption2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerOneOption2ActionPerformed
        // TODO add your handling code here:
        if (playerTwoOption != "O" || playerTwoOption == null) {
            playerOneOption = "O";
            if (online) {
                playerTwoOption = "X";
            }
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
        if (online) {
            gameRequisites();
        } else {
            chanceDisplayer.setText("|" + playerNameOne + "'s Chance|");
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
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void joinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinButtonActionPerformed
        // TODO add your handling code here:
        client = true;
        netManage.statusAssign(server, client);
        String ipAddress = JOptionPane.showInputDialog(this, "Enter IP Address");
        String port = JOptionPane.showInputDialog(this, "Enter Port Number");
        netManage.addressManager(ipAddress, port);
        netManage.NetworkManagerSub(gm);
    }//GEN-LAST:event_joinButtonActionPerformed

    private void hostButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostButtonActionPerformed
        playing = true;
        if (playerOneOption == null) {
            JOptionPane.showMessageDialog(this, "Select A Option");
        } else {
            server = true;
            netManage.statusAssign(server, client);
            netManage.NetworkManagerSub(gm);
            joinButton.setEnabled(false);
        }


    }//GEN-LAST:event_hostButtonActionPerformed

    protected void gameRequisites() {
        playingArea.setVisible(true);
        try {
            int i;
            String query;
            if (server) {
                System.out.println(playerOneOption);
                query = playerNameOne + "^#" + playerOneOption;
            } else {
                query = playerNameOne + "^";
            }
            for (i = 0; i < query.length(); i++) {
                netManage.connectionSocket.getOutputStream().write(query.charAt(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMove(int x, int y) throws IOException {
        netManage.connectionSocket.getOutputStream().write('&');
        netManage.connectionSocket.getOutputStream().write((char) x);
        netManage.connectionSocket.getOutputStream().write((char) y);
        netManage.connectionSocket.getOutputStream().write('%');
    }

    protected void moveManager(int row, int column) {
        cellClickedSub(playingLabels[row][column]);
        playing = true;
    }

    protected void optionSetter(char op1, char op2) {
        playerOneOption = op1 + "";
        playerTwoOption = op2 + "";
    }

    protected void playerNameSetter(String name2) {
        this.playerNameTwo = name2;
        playerNameDisplayer2.setText("Player Name " + name2);
    }

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
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gm = new GameWindow();
                gm.setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel chanceDisplayer;
    public javax.swing.JLabel connectionTrancation;
    private javax.swing.JButton hostButton;
    private javax.swing.JButton joinButton;
    private javax.swing.JLabel playerNameDisplayer1;
    private javax.swing.JLabel playerNameDisplayer2;
    private javax.swing.JRadioButton playerOneOption1;
    private javax.swing.JRadioButton playerOneOption2;
    private javax.swing.JRadioButton playerTwoOption1;
    private javax.swing.JRadioButton playerTwoOption2;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
