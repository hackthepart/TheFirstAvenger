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
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikhil
 */
public class GameWindow extends javax.swing.JFrame {

    /**
     * Creates new form GameWindow
     */
    private JLabel playingLabels[][] = new JLabel[3][3];
    private final int PLAYINGAREAX = 150, PLAYINGAREAY = 100, CELLSIZE = 40, SPACEBETWEENCELLS = 10;
    private int currentPlayer = 0,port = 474;
    int moves[][]=new int[3][3]; //0-empty 1-player 2-opponent
    boolean playing,connected=false,gamedraw=false;
    int x,y;
    JPanel playingArea;
    String ip = "127.0.0.1";
    String player,opponent;
    String playerSymbol,opponentSymbol;
    Socket cSocket,sSocket;
    ServerSocket serSocket;
    Scanner cScanner,sScanner;
    PrintStream cPrintStream,sPrintStream;
    
    private void reset(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                playingLabels[i][j].setText(".");
                playingLabels[i][j].paintImmediately(playingLabels[i][j].getVisibleRect());
                moves[i][j]=0;
            }
        }
        gamedraw=false;
        if(cli_ser_ComboBox.getSelectedItem().equals("Server"))
        {
            sersetup();
        }
        else
        {
            clisetup();
        }
        currentPlayer = 0;
    }

    private void endGame(int currentPlayer){
        if(currentPlayer==0&&!gamedraw)
        {
            JOptionPane.showMessageDialog(null, player + " Wins !!!");
        }
        else if(currentPlayer==1&&!gamedraw)
        {
            JOptionPane.showMessageDialog(null, opponent + " Wins !!!");
        }
        else
        {
            JOptionPane.showMessageDialog(null, "This game is draw!!!");
        }    
        int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
        if(playAgainOrNot == JOptionPane.YES_OPTION)
        {
            reset();
        }
        else 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        
    }
    
    private boolean check(String symbol){
        boolean isGameOver = false, flag, draw = true;

        // checking horizontal match
        for(int i = 0; i < 3; i++){
            flag = true;
            for( int j = 0; j < 3; j++)
                if(!playingLabels[i][j].getText().equals(symbol))
                    flag = false;
            if(flag)
                isGameOver = true;
        }
        
        // checking vertical match
        for(int j = 0; j < 3; j++){
            flag = true;
            for( int i = 0; i < 3; i++)
                if(!playingLabels[i][j].getText().equals(symbol))
                    flag = false;
            if(flag)
                isGameOver = true;
        }
        
        // checking diagonals match
        flag = true;
        for(int i = 0; i < 3; i++){
            if(!playingLabels[i][i].getText().equals(symbol))
                flag = false;
        }
        if(flag)
            isGameOver = true;
        
        flag = true;
        for(int i = 0; i < 3; i++){
            if(!playingLabels[i][2 - i].getText().equals(symbol))
                flag = false;
        }
        if(flag)
            isGameOver = true;
        
        if(isGameOver)
            return true;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(moves[i][j]==0)
                {
                    draw=false;
                }
            }
        }
        if(draw)
        {
            gamedraw=true;
            return true;
        }
        return false;
        
    }
    
    private void cellClicked(MouseEvent evt){
        if(playing){
            end:{
                if(!connected)
                {
                    JOptionPane.showMessageDialog(null, "Wait for opponent to connect.");
                }
                else
                {
                    JLabel currentCell = (JLabel) evt.getComponent();
                    int px = currentCell.getY()/(CELLSIZE+SPACEBETWEENCELLS);
                    int py = currentCell.getX()/(CELLSIZE+SPACEBETWEENCELLS);
                    if(moves[px][py]==0)
                    {
                        currentCell.setText(playerSymbol);
                        if(cli_ser_ComboBox.getSelectedItem().equals("Server"))
                        {
                            sPrintStream.println(px);
                            sPrintStream.println(py);
                            update(px,py,0);
                            if(check(playerSymbol))
                            {
                                endGame(0);
                                break end;
                            }
                            playing = false;
                            turnLabel.setText(opponent + "'s Turn...");
                            turnLabel.paintImmediately(turnLabel.getVisibleRect());
                            x=sScanner.nextInt();
                            y=sScanner.nextInt();
                            turnLabel.setText("Your Turn...");
                            turnLabel.paintImmediately(turnLabel.getVisibleRect());
                            playing = true;
                            update(x,y,1);
                            if(check(opponentSymbol))
                            {
                                endGame(1);
                                break end;
                            }
                        }
                        else
                        {
                            cPrintStream.println(px);
                            cPrintStream.println(py);
                            update(px,py,0);
                            if(check(playerSymbol))
                            {
                                endGame(0);
                                break end;
                            }
                            playing = false;
                            turnLabel.setText(opponent + "'s Turn...");
                            turnLabel.paintImmediately(turnLabel.getVisibleRect());
                            x=cScanner.nextInt();
                            y=cScanner.nextInt();
                            turnLabel.setText("Your Turn...");
                            turnLabel.paintImmediately(turnLabel.getVisibleRect());
                            playing = true;
                            moves[x][y]=2;
                            update(x,y,1);
                            if(check(opponentSymbol))
                            {
                                endGame(1);
                                break end;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void update(int x,int y,int ply)
    {
       for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(((playingArea.getComponent(i+(j*3)).getY()/(CELLSIZE+SPACEBETWEENCELLS))== x) && ((playingArea.getComponent(i+(j*3)).getX()/(CELLSIZE+SPACEBETWEENCELLS)) == y) )
                {
                    JLabel temp = (JLabel) playingArea.getComponent(i+(j*3));
                    if(ply==0)
                    {
                        moves[x][y]=1;
                        temp.setText(playerSymbol);
                        temp.paintImmediately(temp.getVisibleRect());
                    }
                    else
                    {
                        moves[x][y]=2;
                        temp.setText(opponentSymbol);
                        temp.paintImmediately(temp.getVisibleRect());
                    }
                }
            }
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
    
    private void clisetup(){
        playing = false;
        turnLabel.setText(opponent + "'s Turn...");
        turnLabel.paintImmediately(turnLabel.getVisibleRect());
        x=cScanner.nextInt();
        y=cScanner.nextInt();
        playing =true;
        update(x,y,1);
        turnLabel.setText("Your Turn...");
        turnLabel.paintImmediately(turnLabel.getVisibleRect());
    }
    
    private void sersetup(){
        playing = true;
        turnLabel.setText("Your Turn...");
        turnLabel.paintImmediately(turnLabel.getVisibleRect());
    }
    
    public GameWindow() {
        initComponents();
        
        // Adding panel
        playingArea = new JPanel();
        playingArea.setLocation(PLAYINGAREAX, PLAYINGAREAY);
        playingArea.setSize( 3 * (CELLSIZE + SPACEBETWEENCELLS) , 3 * (CELLSIZE + SPACEBETWEENCELLS));
        playingArea.setVisible(true);
        playingArea.setLayout(null);
        add(playingArea);
        addPlayingComponents(playingArea);
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();
        cli_ser_ComboBox = new javax.swing.JComboBox<>();
        nameTextField = new javax.swing.JTextField();
        symbolTextField = new javax.swing.JTextField();
        actionButton = new javax.swing.JButton();
        ipTextField = new javax.swing.JTextField();
        opponentLabel = new javax.swing.JLabel();
        youLabel = new javax.swing.JLabel();
        playerDisplay = new javax.swing.JLabel();
        opponentDisplay = new javax.swing.JLabel();
        turnLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        cli_ser_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Server", "Client" }));
        cli_ser_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cli_ser_ComboBoxActionPerformed(evt);
            }
        });

        nameTextField.setText("Your Name");

        symbolTextField.setText("O");

        actionButton.setText("Host");
        actionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionButtonActionPerformed(evt);
            }
        });

        ipTextField.setText("127.0.0.1");
        ipTextField.setEnabled(false);

        opponentLabel.setText("Opponent:");

        youLabel.setText("You:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(title)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cli_ser_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(opponentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(opponentDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(youLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(playerDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(symbolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(actionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(turnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cli_ser_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(symbolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(actionButton)
                    .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(youLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(playerDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(opponentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(opponentDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(turnLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void actionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionButtonActionPerformed
        actionButton.setEnabled(false);
        if(cli_ser_ComboBox.getSelectedItem().equals("Server"))
        {
            if(nameTextField.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter name.");                
            }
            else if(symbolTextField.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter symbol.");                
            }
            else
            {
                player=nameTextField.getText();
                nameTextField.setEnabled(false);
                playerSymbol=symbolTextField.getText();
                symbolTextField.setEnabled(false);
                try {
                    serSocket = new ServerSocket(port);
                    sSocket = serSocket.accept();
                    sPrintStream = new PrintStream(sSocket.getOutputStream());
                    sScanner = new Scanner(sSocket.getInputStream());
                    opponent=sScanner.nextLine();
                    sPrintStream.println(player);
                    opponentSymbol=sScanner.nextLine();
                    sPrintStream.println(playerSymbol);
                    connected=true;
                    playerDisplay.setText(player);
                    opponentDisplay.setText(opponent);
                    sersetup();
                } catch (IOException ex) {
                    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    actionButton.setEnabled(true);
                }
            }
        }
        else
        {
            if(ipTextField.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter IP Address.");
            }
            else if(nameTextField.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter name.");                
            }
            else if(symbolTextField.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter symbol.");                
            }
            else
            {
                ip=ipTextField.getText();
                ipTextField.setEnabled(false);
                player=nameTextField.getText();
                nameTextField.setEnabled(false);
                playerSymbol=symbolTextField.getText();
                symbolTextField.setEnabled(false);
                try {
                    cSocket = new Socket(ip,port);
                    cPrintStream = new PrintStream(cSocket.getOutputStream());
                    cScanner = new Scanner(cSocket.getInputStream());
                    cPrintStream.println(player);
                    opponent=cScanner.nextLine();
                    cPrintStream.println(playerSymbol);
                    opponentSymbol=cScanner.nextLine();
                    if(opponentSymbol.equals(playerSymbol))
                    {
                        JOptionPane.showMessageDialog(null, "Same symbol: Changing to default");
                        if(opponentSymbol.equals("O"))
                        {
                            playerSymbol="X";
                        }
                        else
                        {
                            playerSymbol="O";
                        }
                        symbolTextField.setText(playerSymbol);
                    }
                    connected=true;
                    playerDisplay.setText(player);
                    playerDisplay.paintImmediately(playerDisplay.getVisibleRect());
                    opponentDisplay.setText(opponent);
                    opponentDisplay.paintImmediately(opponentDisplay.getVisibleRect());
                    clisetup();
                } catch (IOException ex) {
                    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    actionButton.setEnabled(true);
                }   
            }
        }
    }//GEN-LAST:event_actionButtonActionPerformed

    private void cli_ser_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cli_ser_ComboBoxActionPerformed
        if(cli_ser_ComboBox.getSelectedItem().equals("Server"))
        {
            ipTextField.setEnabled(false);
            symbolTextField.setText("O");
            actionButton.setText("Host");
        }
        else
        {
            ipTextField.setEnabled(true);
            ipTextField.setText("127.0.0.1");
            symbolTextField.setText("X");
            actionButton.setText("Connect");            
        }
    }//GEN-LAST:event_cli_ser_ComboBoxActionPerformed

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
    private javax.swing.JButton actionButton;
    private javax.swing.JComboBox<String> cli_ser_ComboBox;
    private javax.swing.JTextField ipTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel opponentDisplay;
    private javax.swing.JLabel opponentLabel;
    private javax.swing.JLabel playerDisplay;
    private javax.swing.JTextField symbolTextField;
    private javax.swing.JLabel title;
    private javax.swing.JLabel turnLabel;
    private javax.swing.JLabel youLabel;
    // End of variables declaration//GEN-END:variables
}
