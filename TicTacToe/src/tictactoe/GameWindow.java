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
    public int currentPlayer = 0;
    private int moves=0;
    public boolean isServer=false;
    public boolean isMulti=false;
    public LAN lan;
    public String player[]=new String[2];
    
    
    public void begin() {
        showName();
        if (isMulti&&!isServer) {
            listenMove();
        }
    }
    
    private void showName() {
        txtPlayer.setText(player[currentPlayer]);
    }
    
    private void reset(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                playingLabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
        moves=0;
    }

    private void endGame(int currentPlayer){
        if (currentPlayer==-1) {
            JOptionPane.showMessageDialog(null,"Draw ");
        }
        else {
            JOptionPane.showMessageDialog(null, player[currentPlayer] + " Wins !!!");
        }
        int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
        if(playAgainOrNot == JOptionPane.YES_OPTION)
            reset();
        else 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        
    }
    
    private void check(int currentPlayer){
        String currentPlayerMove = "O";
        if(currentPlayer == 1)
            currentPlayerMove = "X";
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
        
        else if (moves==9)
            endGame(-1);
        
    }
    
    private int getIndex(JLabel label) {
        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                if (playingLabels[i][j]==label)
                    return i*3+j;
            }
        }
        return 0;
    }
    
    private void listenMove() {
        try {
            Thread waitForData = new Thread(new Runnable(){
                @Override
                public void run() {
                    if (isServer)
                        System.out.print("Server : ");
                    else
                        System.out.print("Client : ");
                    try {
                        String index=lan.getData();
                        if (index.equals("")) {
                            System.out.println("empty");
                        }
                        System.out.println(index);
                        int pos=Integer.valueOf(index);
                        int i=pos/3;
                        int j=pos%3;
                        performMove(playingLabels[i][j],true);
                    }
                    catch (Exception e) {
                        System.out.println("Error");
                    }
                    
                }
            });
            waitForData.start();
        }
        catch (Exception e) {
            //listenMove();
            System.out.println("Problem");
        }
    }
    
    private void cellClicked(MouseEvent evt){
        JLabel currentCell = (JLabel) evt.getComponent();
        performMove(currentCell,false);
    }
    
    private void performMove(JLabel currentCell,boolean other) {
        if (!currentCell.getText().equals("."))
            return;
        if (isMulti) {
            boolean listen=true;
            if (isServer) {
                if (currentPlayer==0) {
                    currentCell.setText("O");
                    lan.sendData(getIndex(currentCell)+"");
                }
                else {
                    if (!other)
                        return;
                    currentCell.setText("X");
                    listen=false;
                }
            }
            else {
                if (currentPlayer==1) {
                    currentCell.setText("X");
                    lan.sendData(getIndex(currentCell)+"");
                }
                else {
                    if (!other)
                        return;
                    currentCell.setText("O");
                    listen=false;
                }
            }
            moves++;
            currentPlayer = (currentPlayer + 1) % 2;
            showName();
            check( (currentPlayer + 1) % 2 );
            if (listen)
                listenMove();
            return;
        }
        moves++;
            
        if(currentPlayer == 0){
            currentCell.setText("O");
        }
        else{
            currentCell.setText("X");
        }
        currentPlayer = (currentPlayer + 1) % 2;
        showName();
        check( (currentPlayer + 1) % 2 );
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
        JPanel playingArea = new JPanel();
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        title = new javax.swing.JLabel();
        txtPlayer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        txtPlayer.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        txtPlayer.setText("Player 1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(title))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                .addComponent(txtPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>                        

    
    // Variables declaration - do not modify                     
    private javax.swing.JLabel title;
    private javax.swing.JLabel txtPlayer;
    // End of variables declaration                   
}
