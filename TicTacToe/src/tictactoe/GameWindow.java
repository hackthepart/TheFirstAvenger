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
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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
    private JLabel playingLabels[][] = new JLabel[3][3];
    private final int PLAYINGAREAX = 110, PLAYINGAREAY = 100, CELLSIZE = 40, SPACEBETWEENCELLS = 10;
    private int currentPlayer;
    private boolean isServerBoolean = true;
    private final int port = 747;
    private String playerName,playerSymbol,opponentName,opponentSymbol;
    public int message = 0;
    JPanel playingArea = new JPanel();
    ServerSocket serverSocket;
    Socket _serverSocket,_clientSocket;
    Scanner serverGetNameOfClient,clientGetNameOfServer,serverScan,clientScan;
    PrintStream clientSendName,serverSendName,serverSendMove,clientSendMove;
    private int arr[][] = {{1,1,1},{1,1,1},{1,1,1}};
    private void reset(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                playingLabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
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
        String currentPlayerMove = playerSymbol;
        if(currentPlayer == 1)
            currentPlayerMove = opponentSymbol;
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
        
    }
    
    private void cellClicked(MouseEvent evt){
        JLabel currentCell = (JLabel) evt.getComponent();
        if(currentCell.getText().equals(".")){
            if(isServerBoolean){
                if(currentPlayer == 0){
                    arr[currentCell.getX()/(CELLSIZE + SPACEBETWEENCELLS)][currentCell.getY()/(CELLSIZE + SPACEBETWEENCELLS)] = 2;
                    message = 0;
                    code(message);
                    try {
                        serverSendMove = new PrintStream(_serverSocket.getOutputStream());
                        serverSendMove.println(message);
                    } catch (IOException ex) {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    serverSendMove.close();
                }
                else{
                    try {
                        serverScan = new Scanner(_serverSocket.getInputStream());
                        message = serverScan.nextInt();
                    } catch (IOException ex) {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    decode(message);
                    serverScan.close();
                }
            }
            else{
                if(currentPlayer == 0){
                    arr[currentCell.getX()/(CELLSIZE + SPACEBETWEENCELLS)][currentCell.getY()/(CELLSIZE + SPACEBETWEENCELLS)] = 3;
                    message = 0;
                    code(message);
                    try {
                        clientSendMove = new PrintStream(_clientSocket.getOutputStream());
                        clientSendMove.println(message);
                    } catch (IOException ex) {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    clientSendMove.close();
                }
                else{
                    try {
                        clientScan = new Scanner(_clientSocket.getInputStream());
                        message = clientScan.nextInt();
                    } catch (IOException ex) {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    decode(message);
                    clientScan.close();
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Place is already occupied");
            return ;
        }
        print();
        currentPlayer = (currentPlayer + 1) % 2;
        check( (currentPlayer + 1) % 2 );
        /*if(currentPlayer == 0){
            currentCell.setText("O");
        }
        else{
            currentCell.setText("X");
        }
        currentPlayer = (currentPlayer + 1) % 2;
        check( (currentPlayer + 1) % 2 );
        */
        /*if(currentPlayer == 0){
            if(isServerBoolean){
                if(currentCell.getText().equals(".")){
                    currentCell.setText(playerSymbol);
                    arr[currentCell.getX()][currentCell.getY()] = 2;
                    message = 0;
                    code(message);
                    serverSendMove.println(message);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Field already occupied!!!!");
                    return;
                }
            }
            else{
                message = clientScan.nextInt();
                decode(message);   
                print();
            }
        }
        else{
            if(isServerBoolean){
                message = serverScan.nextInt();
                decode(message);   
                print();
            }
            else{
                if(currentCell.getText().equals(".")){
                    currentCell.setText(playerSymbol);
                    arr[currentCell.getX()][currentCell.getY()] = 2;
                    message = 0;
                    code(message);
                    clientSendMove.println(message);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Field already occupied!!!!");
                    return;
                }
            }
        }
        currentPlayer = (currentPlayer + 1) % 2;
        check( (currentPlayer + 1) % 2 );
        */
        /*
        if(isServerBoolean){
            if(currentPlayer == 0){
                if(currentCell.getText().equals(".")){
                    currentCell.setText(playerSymbol);
                    arr[currentCell.getX()][currentCell.getY()] = 2;
                    message = 0;
                    code(message);
                    serverSendMove.println(message);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Field already occupied!!!!");
                    return;
                }
            }
            else{
                message = serverScan.nextInt();
                decode(message);   
                print();
            }
        }
        else{
            if(currentPlayer == 0){
                if(currentCell.getText().equals(".")){
                    currentCell.setText(playerSymbol);
                    arr[currentCell.getX()][currentCell.getY()] = 1;
                    message = 0;
                    code(message);
                    clientSendMove.println(message);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Field already occupied!!!!");
                    return;
                }
            }
            else{
                message = clientScan.nextInt();
                decode(message);   
                print();
            }
        }
        currentPlayer = (currentPlayer + 1) % 2;
        check( (currentPlayer + 1) % 2 );*/
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
        playingArea.setVisible(false);  //not visible initially
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

        buttonGroup = new javax.swing.ButtonGroup();
        title = new javax.swing.JLabel();
        isServer = new javax.swing.JRadioButton();
        isClient = new javax.swing.JRadioButton();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        symbolLabel = new javax.swing.JLabel();
        symbolField = new javax.swing.JTextField();
        startButton = new javax.swing.JButton();
        ipField = new javax.swing.JTextField();
        ipLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        buttonGroup.add(isServer);
        isServer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        isServer.setSelected(true);
        isServer.setText("Server");
        isServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isServerActionPerformed(evt);
            }
        });

        buttonGroup.add(isClient);
        isClient.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        isClient.setText("Client ");
        isClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isClientActionPerformed(evt);
            }
        });

        nameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        nameLabel.setText("Name");

        symbolLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        symbolLabel.setText("Symbol");

        startButton.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        startButton.setForeground(new java.awt.Color(0, 200, 100));
        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        ipField.setEnabled(false);

        ipLabel.setText("IP:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(title)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(symbolLabel)
                        .addGap(18, 18, 18)
                        .addComponent(symbolField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(isClient, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(isServer, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ipLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(nameLabel)
                                .addGap(18, 18, 18)
                                .addComponent(nameField)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(symbolField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(symbolLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                        .addComponent(isServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(isClient)
                            .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ipLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startButton)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        // TODO add your handling code here:
        if(nameField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Enter Name");
        }
        else{
            if(symbolField.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Enter Symbol");
            }
            else{
                if(isClient.isSelected()){
                    if(ipField.getText().equals("")){
                       JOptionPane.showMessageDialog(null, "Enter IP");
                    }
                    else{
                        try {
                            _clientSocket = new Socket(ipField.getText(),port);
                            clientSendName = new PrintStream(_clientSocket.getOutputStream());
                            clientSendName.println(nameField.getText());
                            clientGetNameOfServer = new Scanner(_clientSocket.getInputStream());
                            opponentName = clientGetNameOfServer.nextLine();
                            clientSendName.println(symbolField.getText());
                            opponentSymbol = clientGetNameOfServer.nextLine();
                            
                            lockGUI(true);
                        } catch (IOException ex) {
                            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else{
                    try {
                        serverSocket = new ServerSocket(port);
                        _serverSocket = serverSocket.accept();
                        serverGetNameOfClient = new Scanner(_serverSocket.getInputStream());
                        opponentName = serverGetNameOfClient.nextLine();
                        serverSendName = new PrintStream(_serverSocket.getOutputStream());
                        serverSendName.println(nameField.getText());
                        opponentSymbol = serverGetNameOfClient.nextLine();
                        if(symbolField.getText().equals(opponentSymbol)){
                            if(opponentSymbol != "X")
                                playerSymbol = "X";
                            else
                                playerSymbol = "Y";
                            JOptionPane.showMessageDialog(null, "Your symbol match with opponent symbol");
                        }else{
                            playerSymbol = symbolField.getText();
                            serverSendName.println(playerSymbol);
                            playerName = nameField.getText();
                        }
                        lockGUI(true);
                        serverGetNameOfClient.close();
                        serverSendName.close();
                    } catch (IOException ex) {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        /*
        initialised with scanner and printer objects
        */
        try {
            clientSendMove = new PrintStream(_clientSocket.getOutputStream());
            
            
            serverScan = new Scanner(_serverSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(isServerBoolean){
            currentPlayer = 0;
        }
        else{
            currentPlayer = 1;
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void isServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isServerActionPerformed
        // TODO add your handling code here:
        isServerBoolean = true;
        ipField.setEnabled(false);
    }//GEN-LAST:event_isServerActionPerformed

    private void isClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isClientActionPerformed
        // TODO add your handling code here:
        isServerBoolean = false;
        ipField.setEnabled(true);
    }//GEN-LAST:event_isClientActionPerformed

    private int code(int message){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                message = message * 10 + arr[i][j];
            }
        }
        return message;
    }
    
    private void decode(int message){
        for(int i=2;i>=0;i--){
            for(int j=2;j>=0;j--){
                arr[i][j] = message % 10;
                message /= 10;
            }
        }
    }
    
    private void print(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(arr[i][j]==1){
                    playingLabels[i][j].setText(".");
                }
                else if(arr[i][j]==2){
                    playingLabels[i][j].setText(playerSymbol);
                }
                else{
                    playingLabels[i][j].setText(opponentSymbol);
                }
            }
        }
    }
    
    public void lockGUI(boolean condition){
        playingArea.setVisible(condition);
        isServer.setVisible(!condition);
        isClient.setVisible(!condition);
        ipLabel.setVisible(!condition);
        ipField.setVisible(!condition);
        startButton.setVisible(!condition);
        symbolLabel.setVisible(!condition);
        nameLabel.setVisible(!condition);
        symbolField.setVisible(!condition);
        nameField.setVisible(!condition);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JTextField ipField;
    private javax.swing.JLabel ipLabel;
    private javax.swing.JRadioButton isClient;
    private javax.swing.JRadioButton isServer;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton startButton;
    private javax.swing.JTextField symbolField;
    private javax.swing.JLabel symbolLabel;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
