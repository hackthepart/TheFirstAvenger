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

    String pName1;
    String pName2, currentPlayerMove;
    int sign1,sign2;
    /**
     * Creates new form GameWindow
   */
    private JLabel playingLabels[][] = new JLabel[3][3];
    private final int PLAYINGAREAX = 110, PLAYINGAREAY = 100, CELLSIZE = 40, SPACEBETWEENCELLS = 10;
    private int currentPlayer = 0;
    
    private void reset(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                playingLabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
    }

    private void endGame(int currentPlayer){
        if(currentPlayer == 0)
            JOptionPane.showMessageDialog(null, "Player " + pName1 + " Wins !!!");
        else
            JOptionPane.showMessageDialog(null, "Player " + pName2 + " Wins !!!");
        int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
        if(playAgainOrNot == JOptionPane.YES_OPTION)
            reset();
        else 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        
    }
    
    private void check(int currentPlayer){
        if(sign1 == 0){
            currentPlayerMove = "O";
            if(currentPlayer == 1)
                currentPlayerMove = "X";
        }
        else{
            currentPlayerMove = "X";
            if(currentPlayer == 1)
                currentPlayerMove = "O";
        }
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
        System.out.println(currentCell.getText());
        if(((current.getText()).equals(pName1))&&(currentPlayer == 0)&&((currentCell.getText() != "O")&&(currentCell.getText() != "X"))){
            if(sign1 == 0)
                currentCell.setText("O");
            else
                currentCell.setText("X");
            current.setText(pName2);
        }
        else if(((current.getText()).equals(pName2))&&(currentPlayer == 1)){
            if(sign2 == 0)
                currentCell.setText("O");
            else
                currentCell.setText("X");
            current.setText(pName1);
        }
        else{
            JOptionPane.showMessageDialog(null, "Cell is already occupied! Try Again!!");
        }
        currentPlayer = (currentPlayer + 1) % 2;
        check( (currentPlayer + 1) % 2 );
    }
    
    private void addPlayingComponents(JPanel playingArea){
        current.setText(pName1);
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
    
    public GameWindow(String pName1, String pName2, int sign1, int sign2) {
        initComponents();
        
        // Adding panel
        this.pName1 = pName1;
        this.pName2 = pName2;
        this.sign1 = sign1;
        this.sign2 = sign2;
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        CurrentPlayer = new javax.swing.JLabel();
        current = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        CurrentPlayer.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        CurrentPlayer.setText("CURRENT PLAYER");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(CurrentPlayer, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(current, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addComponent(title)
                .addGap(54, 54, 54))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CurrentPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(current, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(280, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CurrentPlayer;
    private javax.swing.JLabel current;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
