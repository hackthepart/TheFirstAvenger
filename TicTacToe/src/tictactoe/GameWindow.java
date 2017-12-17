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
    private boolean isServerBoolean = true, isGameOver;
    private final int port = 747;
    private String playerName,playerSymbol,opponentName,opponentSymbol,IP;
    public int message = 0;
    JPanel playingArea = new JPanel();
    ServerSocket serverSocket;
    Socket _serverSocket,_clientSocket;
    Scanner serverScan,clientScan;
    PrintStream serverSendMove,clientSendMove;
    private int arr[][] = {{1,1,1},{1,1,1},{1,1,1}};
    
    private void reset(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                playingLabels[i][j].setText(".");
            }
        }
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                arr[i][j]=1;
            }
        }
        if(!isServerBoolean){
            //System.out.println("c8");
            message = clientScan.nextInt();
            //System.out.println("c9");
            decode(message);
            //System.out.println("c10");
            print();
        }
    }

    private void endGame(String playerName){
        if((checkBlank() == 0)&&!isGameOver){
            JOptionPane.showMessageDialog(null, "Draw :)");  
        }
        else{
            JOptionPane.showMessageDialog(null, playerName + " Wins");
        }
            int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
            if(playAgainOrNot == JOptionPane.YES_OPTION)
                reset();
            else 
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    private boolean check(String playerSymbol, String playerName){
        String currentPlayerMove = playerSymbol;
        isGameOver = false;
        boolean flag;

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
        if((checkBlank() == 0)||isGameOver){
            endGame(playerName);
            return true;
        }
        return false;
}
    
    private void cellClicked(MouseEvent evt){
        code:{
            JLabel currentCell = (JLabel) evt.getComponent();
            //System.out.println("isServer : " + isServerBoolean);
            if(currentCell.getText().equals(".")){
                if(isServerBoolean){
                    //System.out.println("ccs1");
                    print();
                    turnLabel.setText("(*) " + playerName + " turn");
                    turnLabel.paintImmediately(turnLabel.getVisibleRect());
                    //System.out.println("ccs2");
                    //System.out.println("X:"+currentCell.getX()/(CELLSIZE + SPACEBETWEENCELLS));
                    //System.out.println("X:"+currentCell.getY()/(CELLSIZE + SPACEBETWEENCELLS));
                    arr[currentCell.getX()/(CELLSIZE + SPACEBETWEENCELLS)][currentCell.getY()/(CELLSIZE + SPACEBETWEENCELLS)] = 2;
                    //System.out.println("ccs3");
                    currentCell.setText(playerSymbol);
                    //System.out.println("ccs4");
                    currentCell.paintImmediately(currentCell.getVisibleRect());
                    //System.out.println("ccs5");
                    //System.out.println("ccs6");
                    message = code();
                    serverSendMove.println(message);
                    if(check(playerSymbol,playerName)){
                        break code;
                    }
                    turnLabel.setText("( ) " + opponentName + " turn");
                    turnLabel.paintImmediately(turnLabel.getVisibleRect());
                    //System.out.println("ccs7");
                    try{
                        message = serverScan.nextInt();
                    }
                    catch(Exception e){
                        if(e instanceof java.util.NoSuchElementException){
                            JOptionPane.showMessageDialog(null, opponentName + " disconnected");
                        }
                    }
                    //System.out.println("ccs8");
                    decode(message);
                    //System.out.println("ccs9" + message);
                    print();
                    turnLabel.setText("(*) " + playerName + " turn");
                    turnLabel.paintImmediately(turnLabel.getVisibleRect());
                    //System.out.println("ccs10");
                    if(check(opponentSymbol,opponentName)){
                        break code;
                    }
                    //System.out.println("ccs11");
                }
                else{
                    print();
                    turnLabel.setText("(*) " + playerName + " turn");
                    turnLabel.paintImmediately(turnLabel.getVisibleRect());
                    //System.out.println("cc1");
                    
                    //System.out.println("cc2");
                    arr[currentCell.getX()/(CELLSIZE + SPACEBETWEENCELLS)][currentCell.getY()/(CELLSIZE + SPACEBETWEENCELLS)] = 3;
                    //System.out.println("cc3");
                    currentCell.setText(playerSymbol);
                    //System.out.println("cc4");
                    currentCell.paintImmediately(currentCell.getVisibleRect());
                    //System.out.println("cc5");

                    //System.out.println("cc6");
                    message = code();
                    clientSendMove.println(message);
                    if(check(playerSymbol,playerName)){
                        break code;
                    }
                    turnLabel.setText("( ) " + opponentName + " turn");
                    turnLabel.paintImmediately(turnLabel.getVisibleRect());
                    //System.out.println("cc7");
                    try{
                        message = clientScan.nextInt();
                    }
                    catch(Exception e){
                        if(e instanceof java.util.NoSuchElementException){
                            JOptionPane.showMessageDialog(null, opponentName + " disconnected");
                        }
                    }
                    
                    //System.out.println("cc8");
                    decode(message);
                    //System.out.println("cc9" + message);
                    print();
                    turnLabel.setText("(*) " + playerName + " turn");
                    turnLabel.paintImmediately(turnLabel.getVisibleRect());
                    //System.out.println("cc10");
                    if(check(opponentSymbol,opponentName)){
                        break code;
                    }
                    //System.out.println("cc11");
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Place already occupied");
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
        turnLabel = new javax.swing.JLabel();
        opponentDetailsLabel = new javax.swing.JLabel();
        myDetailsLabel = new javax.swing.JLabel();
        playerNameLabel = new javax.swing.JLabel();
        playerSymbolLabel = new javax.swing.JLabel();
        opponentNameLabel = new javax.swing.JLabel();
        opponentSymbolLabel = new javax.swing.JLabel();

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

        ipField.setText("127.0.0.1");
        ipField.setEnabled(false);

        ipLabel.setText("IP:");

        opponentDetailsLabel.setText("Opponent Details:-");

        myDetailsLabel.setText("My Details:-");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(myDetailsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(playerSymbolLabel)
                                    .addComponent(playerNameLabel))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(title)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(opponentDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(opponentSymbolLabel)
                                    .addComponent(opponentNameLabel))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isClient, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(isServer, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ipLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(turnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addGap(18, 18, 18)
                        .addComponent(nameField))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(symbolLabel)
                        .addGap(18, 18, 18)
                        .addComponent(symbolField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(opponentDetailsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opponentNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opponentSymbolLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(title))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(myDetailsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerSymbolLabel)))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(symbolField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(symbolLabel))
                .addGap(163, 163, 163)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isServer, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                            .addComponent(turnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(isClient)
                            .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ipLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
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
                playerNameLabel.setText(nameField.getText());
                playerNameLabel.paintImmediately(playerNameLabel.getVisibleRect());
                playerSymbolLabel.setText(symbolField.getText());
                playerSymbolLabel.paintImmediately(playerSymbolLabel.getVisibleRect());
                if(isClient.isSelected()){
                    isServerBoolean = false;
                    if(ipField.getText().equals("")){
                       JOptionPane.showMessageDialog(null, "Enter IP");
                    }
                    else{
                        try {
                            IP = ipField.getText();
                            //System.out.println("stsygyds");
                            _clientSocket = new Socket(IP,port);
                            //System.out.println("c1");
                            clientSendMove = new PrintStream(_clientSocket.getOutputStream());
                            //System.out.println("c2");
                            clientSendMove.println(nameField.getText());
                            //System.out.println("c3");
                            clientScan = new Scanner(_clientSocket.getInputStream());
                            //System.out.println("c4");
                            opponentName = clientScan.nextLine();
                            //System.out.println("c5");
                            clientSendMove.println(symbolField.getText());
                            //System.out.println("c6");
                            opponentSymbol = clientScan.nextLine();
                            //System.out.println("c7");
                            playerName = nameField.getText();
                            playerSymbol = symbolField.getText();
                            //System.out.println("client locking");
                            lockGUI(true);
                            //System.out.println("client Locked");
                        } catch (IOException ex) {
                            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else{
                    isServerBoolean = true;
                    
                    try {
                        serverSocket = new ServerSocket(port);
                        //System.out.println("s1");
                        _serverSocket = serverSocket.accept();
                        //System.out.println("s2");
                        serverScan = new Scanner(_serverSocket.getInputStream());
                        //System.out.println("s3");
                        opponentName = serverScan.nextLine();
                        //System.out.println("s4");
                        serverSendMove = new PrintStream(_serverSocket.getOutputStream());
                        //System.out.println("s5");
                        serverSendMove.println(nameField.getText());
                        //System.out.println("s6");
                        opponentSymbol = serverScan.nextLine();
                        //System.out.println("s7");
                        if(symbolField.getText().equals(opponentSymbol)){
                            if(opponentSymbol != "X")
                                playerSymbol = "X";
                            else
                                playerSymbol = "O";
                            JOptionPane.showMessageDialog(null, "Your symbol match with opponent symbol");
                        }
                        else{
                            playerSymbol = symbolField.getText();
                            //System.out.println("s8");
                            serverSendMove.println(playerSymbol);
                            //System.out.println("s9");
                            playerName = nameField.getText();
                            playerSymbol = symbolField.getText();
                            //System.out.println("s10");
                        }
                        //System.out.println("server locking");
                        lockGUI(true);
                        //System.out.println("server locked");
                    } catch (IOException ex) {
                        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }            
            }
        }
        turnLabel.setText("(*)" + playerName + "] turn");
        turnLabel.paintImmediately(turnLabel.getVisibleRect());
        opponentNameLabel.setText(opponentName);
        opponentNameLabel.paintImmediately(opponentNameLabel.getVisibleRect());
        opponentSymbolLabel.setText(opponentSymbol);
        opponentSymbolLabel.paintImmediately(playerNameLabel.getVisibleRect());
        //System.out.println("s11 and c7");
        if(!isServerBoolean){
            turnLabel.setText("( )" + opponentName + "] turn");
            turnLabel.paintImmediately(turnLabel.getVisibleRect());
            //System.out.println("c8");
            message = clientScan.nextInt();
            //System.out.println("c9");
            decode(message);
            //System.out.println("c10");
            print();
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

    private int code(){
        int message = 0;
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
                if(isServerBoolean){
                    if(arr[i][j] == 1){
                        playingLabels[i][j].setText(".");
                        playingLabels[i][j].paintImmediately(playingLabels[i][j].getVisibleRect());
                    }
                    else if(arr[i][j] == 2){
                        playingLabels[i][j].setText(playerSymbol);
                        playingLabels[i][j].paintImmediately(playingLabels[i][j].getVisibleRect());
                    }
                    else if(arr[i][j] == 3){
                        playingLabels[i][j].setText(opponentSymbol);
                        playingLabels[i][j].paintImmediately(playingLabels[i][j].getVisibleRect());
                    }
                }
                else{
                    if(arr[i][j] == 1){
                        playingLabels[i][j].setText(".");
                        playingLabels[i][j].paintImmediately(playingLabels[i][j].getVisibleRect());
                    }
                    else if(arr[i][j] == 2){
                        playingLabels[i][j].setText(opponentSymbol);
                        playingLabels[i][j].paintImmediately(playingLabels[i][j].getVisibleRect());
                    }
                    else if(arr[i][j] == 3){
                        playingLabels[i][j].setText(playerSymbol);
                        playingLabels[i][j].paintImmediately(playingLabels[i][j].getVisibleRect());
                    }
                }
                //System.out.print(arr[i][j]);
            }
            //System.out.println(" ");
        }
    }
    
    private int checkBlank(){
        int blank = 0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(playingLabels[i][j].getText().equals(".")){
                    blank++;
                }
            }
        }
        return blank;
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
        playingArea.repaint();
        isServer.repaint();
        isClient.repaint();
        ipLabel.repaint();
        ipField.repaint();
        startButton.repaint();
        symbolLabel.repaint();
        nameLabel.repaint();
        symbolField.repaint();
        nameField.repaint();
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
    private javax.swing.JLabel myDetailsLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel opponentDetailsLabel;
    private javax.swing.JLabel opponentNameLabel;
    private javax.swing.JLabel opponentSymbolLabel;
    private javax.swing.JLabel playerNameLabel;
    private javax.swing.JLabel playerSymbolLabel;
    private javax.swing.JButton startButton;
    private javax.swing.JTextField symbolField;
    private javax.swing.JLabel symbolLabel;
    private javax.swing.JLabel title;
    private javax.swing.JLabel turnLabel;
    // End of variables declaration//GEN-END:variables
}
