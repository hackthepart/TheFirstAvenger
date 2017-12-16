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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
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
    private CheckPoint playingLabels[][] = new CheckPoint[3][3];
    private final int PLAYINGAREAX = 110, PLAYINGAREAY = 100, CELLSIZE = 40, SPACEBETWEENCELLS = 10;
    private int currentPlayer = 0;
    private String playerNameOne;
    private String playerNameTwo = "";
    private String playerOneOption = null;
    private String playerTwoOption = null;
    private Integer count = 0;
    private boolean flag;
    private boolean online;
    private Socket s;
    private PrintStream p;
    private boolean playing = false;
    JPanel playingArea = new JPanel();

    private void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playingLabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
        jLabel3.setText("" + playerNameOne + "'s Chance");
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
        if (playing) {
            boolean player = false;
            CheckPoint currentCell = (CheckPoint) evt.getComponent();
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
                jLabel3.setText("|" + playerNameTwo + "'s Chance|");
            } else {
                jLabel3.setText("|" + playerNameOne + "'s Chance|");
            }
            playing = false;
            sendMove(currentCell.row, currentCell.column);
        } else {
            if (!online) {
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
                System.out.println(currentCell + "");
                if (player) {
                    jLabel3.setText("|" + playerNameTwo + "'s Chance|");
                } else {
                    jLabel3.setText("|" + playerNameOne + "'s Chance|");
                }

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
            jRadioButton3.setEnabled(false);
            jRadioButton4.setEnabled(false);
            online = true;
        }
        playerNameOne = JOptionPane.showInputDialog("Enter Player One Name");
        jLabel1.setText(jLabel1.getText() + " " + playerNameOne);
        if (!online) {
            playerNameTwo = JOptionPane.showInputDialog("Enter Player Two Name To Start");
            jLabel2.setText(jLabel2.getText() + " " + playerNameTwo);
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
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        startButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        joinButton = new javax.swing.JButton();
        hostButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

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
                    .addComponent(jLabel1)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton4))
                .addGap(48, 48, 48))
            .addGroup(layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2))
                    .addComponent(title))
                .addGap(41, 41, 41))
            .addGroup(layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(startButton)
                .addGap(30, 30, 30)
                .addComponent(joinButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hostButton)
                .addGap(0, 59, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(joinButton)
                    .addComponent(hostButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        if (playerTwoOption != "X" || playerTwoOption == null) {
            playerOneOption = "X";
            if (online) {
                playerTwoOption = "O";
            }
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            jRadioButton2.setSelected(true);

        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        if (playerTwoOption != "O" || playerTwoOption == null) {
            playerOneOption = "O";
            if (online) {
                playerTwoOption = "X";
            }
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            jRadioButton1.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        if (playerOneOption != "X" || playerOneOption == null) {
            playerTwoOption = "X";
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            jRadioButton4.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
        if (playerOneOption != "O" || playerOneOption == null) {
            playerTwoOption = "O";
        } else {
            JOptionPane.showMessageDialog(this, "Option Already Chosen by other oponent");
            jRadioButton3.setSelected(true);
        }
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        // TODO add your handling code here:
        if (online) {
            gameRequisites();
        } else {
            jLabel3.setText("|" + playerNameOne + "'s Chance|");
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
        int check = 0;
        String ipAddress = JOptionPane.showInputDialog(this, "Enter IP Address");
        String port = JOptionPane.showInputDialog(this, "Enter Port Number");
        try {
            Socket s = new Socket("localhost", Integer.parseInt(port));
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            int c;
            while ((char) (c = in.read()) != '$') {
                if ((char) c != '#' && check != 1) {
                    jLabel4.setText(jLabel4.getText() + (char) +c);
                } else {
                    check = 1;
                }
                if (check == 1 && (char) c != '#' && (char) c != 'X' && (char) c != 'O') {
                    playerNameTwo = playerNameTwo + (char) c;
                }
                if ((char) c == 'X' || (char) c == 'O') {
                    playerTwoOption = (char) c + "";
                }
                if ((char) c == '$') {
                    c = -1;
                }
            }

            if (playerTwoOption.equals("O")) {
                playerOneOption = "X";
            } else {
                playerOneOption = "O";
            }
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            dout.writeChars(playerNameOne + '@');
            jLabel2.setText("Player Name : " + playerNameTwo);
            playingArea.setVisible(true);
            
            dout.close();
            in.close();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_joinButtonActionPerformed

    private void hostButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostButtonActionPerformed
        playing = true;
        if (playerOneOption == null) {
            JOptionPane.showMessageDialog(this, "Select A Option");
        } else {
            try {
                boolean i = true;
                ServerSocket ss = new ServerSocket(8080);
                jLabel4.setText("Waiting For Player to Join");
                s = ss.accept();
                jLabel4.setText("Conncted with Player At " + s.getRemoteSocketAddress() + ":");
                if (s.getRemoteSocketAddress() != null) {
                    p = new PrintStream(s.getOutputStream());
                    p.print("Welcome You Are Connected To 127:0:0:1:8080#");
                    p.print(playerNameOne + playerTwoOption + "$");
                    flag = true;
                    playingArea.setVisible(flag);
                }
            } catch (Exception e) {

            }
        }
    }//GEN-LAST:event_hostButtonActionPerformed

    private void gameRequisites() {
        try {
            int c;
            DataInputStream din = new DataInputStream(s.getInputStream());
            while ((c = din.readChar()) != '@') {
                playerNameTwo = playerNameTwo + (char) c;
            }
            din.close();
            jLabel2.setText(playerNameTwo);
        } catch (Exception e) {

        }
    }

    private void sendMove(int x, int y) throws IOException {
        p.print(x);
        p.print(y);
    }

    private void moveManager(String cooString) {

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
    private javax.swing.JButton hostButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JButton joinButton;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
