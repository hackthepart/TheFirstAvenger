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
    private static String firstMarker;
    private static String secondMarker;
    
    private void reset(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                playingLabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
    }

    private void endGame(int currentPlayer){
        if((currentPlayer + 1)==0)
        JOptionPane.showMessageDialog(null, GameSetup.secondPlayer+ " Wins !!!");
        else
                    JOptionPane.showMessageDialog(null, GameSetup.firstPlayer + " Wins !!!");

        int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
        if(playAgainOrNot == JOptionPane.YES_OPTION)
            reset();
        else 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        
    }
    
    private void check(int currentPlayer){
        String currentPlayerMove = firstMarker;
        if(currentPlayer == 1)
            currentPlayerMove = secondMarker;
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
        checkDrawGame();
        
    }
    private void checkDrawGame()
    {   int flag=0;
        for(int i=0; i< 3; i++){
            for(int j=0;j<3;j++)
            {
                if(!playingLabels[i][j].getText().equals("."))
                    flag++;
            }
        }
        if(flag==9){
                    JOptionPane.showMessageDialog(null,"Game " + " Draw !!!");
            int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
        if(playAgainOrNot == JOptionPane.YES_OPTION)
            reset();
        else 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
            
    }
    private boolean validateMove(JLabel cell){
        boolean valid;
        if(cell.getText().equals("."))
            valid=true;
        else
        {
            JOptionPane.showMessageDialog(null, "Not A Valid Move", "Alert", JOptionPane.ERROR_MESSAGE);
            valid=false;
        }
        return valid;
    }
    
    private void cellClicked(MouseEvent evt){
        JLabel currentCell = (JLabel) evt.getComponent();
        if(validateMove(currentCell)){
        if(currentPlayer == 0){
            currentCell.setText(firstMarker);
            turn.setText(GameSetup.secondPlayer+"'s turn ");
        }
        else{
            currentCell.setText(secondMarker);
                        turn.setText(GameSetup.firstPlayer+"'s turn ");

        }
        currentPlayer = (currentPlayer + 1) % 2;
        check( (currentPlayer + 1) % 2 );
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
        if(GameSetup.firstZeroState)
         firstMarker="O";
        else 
            firstMarker="X";
        if(GameSetup.secondZeroState)
            secondMarker="O";
        else 
            secondMarker="X";
        player1.setText(GameSetup.firstPlayer);
        player2.setText(GameSetup.secondPlayer);
        player1Marker.setText(firstMarker);
        player2Marker.setText(secondMarker);
        turn.setText(GameSetup.firstPlayer+"'s turn");
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

        title = new javax.swing.JLabel();
        player1Label = new javax.swing.JLabel();
        player2Label = new javax.swing.JLabel();
        player1 = new javax.swing.JLabel();
        player2 = new javax.swing.JLabel();
        turn = new javax.swing.JLabel();
        player1Marker = new javax.swing.JLabel();
        player2Marker = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("TIC TAC TOE");

        player1Label.setText("Player1 ");

        player2Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player2Label.setText("Player 2");

        player1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        player1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        player2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        player2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        turn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        turn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        player1Marker.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        player1Marker.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        player2Marker.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        player2Marker.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(player1Marker, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(player1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(player1Label)
                                .addGap(0, 18, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(title)
                                .addGap(48, 48, 48)
                                .addComponent(player2Label))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(player2Marker, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addComponent(player2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(turn, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(player1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(title)
                    .addComponent(player2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(player2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(player1Marker, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player2Marker, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                .addComponent(turn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JLabel player1;
    private javax.swing.JLabel player1Label;
    private javax.swing.JLabel player1Marker;
    private javax.swing.JLabel player2;
    private javax.swing.JLabel player2Label;
    private javax.swing.JLabel player2Marker;
    private javax.swing.JLabel title;
    private javax.swing.JLabel turn;
    // End of variables declaration//GEN-END:variables
}
