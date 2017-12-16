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
    boolean moved[][]=new boolean[3][3];
    String name1,name2;
    boolean play=false;
    String symbol_p1="O";
    String symbol_p2="X";
    private void reset(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                playingLabels[i][j].setText(".");
                moved[i][j]=false;
            }
        } 
        nextButton.setVisible(true);
        startButton.setVisible(false);
        startButton.setEnabled(true);
        playerLabel.setText("Player 1 Name :");
        chanceLabel.setText("");
        nameTextfield.setText(name1);
        nameTextfield.setEditable(true);
        currentPlayer = 0;
        play=false;
        changeButton.setEnabled(true);
        p1Field.setText("O");
        p2Field.setText("X");
        p1Label.setText(name1);
        p2Label.setText(name2);
        p1Field.setEditable(true);
        p2Field.setEditable(true);
    }

    private void endGame(int currentPlayer){
        if(currentPlayer==0)
            JOptionPane.showMessageDialog(null, name1 + " Wins !!!");
        else if(currentPlayer==1)
            JOptionPane.showMessageDialog(null, name2 + " Wins !!!");
        else 
            JOptionPane.showMessageDialog(null, "This game is Draw.");
        int playAgainOrNot = JOptionPane.showConfirmDialog(null, "Want to play again???", "Continue?", 0);
        if(playAgainOrNot == JOptionPane.YES_OPTION)
            reset();
        else 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        
    }
    
    private boolean checkdraw(){
        boolean draw=true;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
                if(!moved[i][j])
                {
                    draw=false;
                }
        }
        if(draw)
        {
            endGame(2);
            return true;
        }
        return false;
    }
    
    private void check(int currentPlayer){
        String currentPlayerMove = symbol_p1;
        if(currentPlayer == 1)
            currentPlayerMove = symbol_p2;
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
        checkdraw();
    }
    
    private void cellClicked(MouseEvent evt){
        JLabel currentCell = (JLabel) evt.getComponent();
        int x = currentCell.getX()/(CELLSIZE+SPACEBETWEENCELLS);
        int y = currentCell.getY()/(CELLSIZE+SPACEBETWEENCELLS);
        if(!moved[x][y]&&play)
        {
            if(currentPlayer == 0){
                currentCell.setText(symbol_p1);
                chanceLabel.setText(name2+"'s CHANCE");

            }
            else{
                currentCell.setText(symbol_p2);
                chanceLabel.setText(name1+"'s CHANCE");
            }
            currentPlayer = (currentPlayer + 1) % 2;
            check( (currentPlayer + 1) % 2 );
            moved[x][y]=true;
        }
        else if(moved[x][y]&&play)
        {
            if(!checkdraw())
            {
                JOptionPane.showMessageDialog(null, "Choose another spot!!!");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please Enter names first.");
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
                startButton.setVisible(false);
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();
        playerLabel = new javax.swing.JLabel();
        nameTextfield = new javax.swing.JTextField();
        nextButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        chanceLabel = new javax.swing.JLabel();
        p1Label = new javax.swing.JLabel();
        p2Label = new javax.swing.JLabel();
        p1Field = new javax.swing.JTextField();
        p2Field = new javax.swing.JTextField();
        changeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        playerLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        playerLabel.setText("Player 1 Name :");

        nameTextfield.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        p1Label.setText("Player 1:");

        p2Label.setText("Player 2:");

        p1Field.setText("O");

        p2Field.setText("X");

        changeButton.setText("Change");
        changeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(title)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(p1Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(p1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(playerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(chanceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                    .addComponent(nameTextfield, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(p2Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(p2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(changeButton)
                                .addGap(35, 35, 35))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerLabel)
                    .addComponent(nameTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextButton)
                    .addComponent(startButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 201, Short.MAX_VALUE)
                .addComponent(chanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(p2Label)
                        .addComponent(p2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(changeButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(p1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(p1Label)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if(!nameTextfield.getText().equals(""))
        {
            name1 = nameTextfield.getText();
            startButton.setLocation(nextButton.getX(), nextButton.getY());
            nextButton.setVisible(false);
            startButton.setVisible(true);
            nameTextfield.setText("");
            playerLabel.setText("Player 2 Name :");
            p1Label.setText(name1);
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Please choose a valid name.");
        }
    }//GEN-LAST:event_nextButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if(!nameTextfield.getText().equals(""))
        {   
            if(!name1.equals(nameTextfield.getText()))
            {
                nameTextfield.setEditable(false);
                name2 = nameTextfield.getText();
                System.out.println(name1+name2);
                chanceLabel.setText(name1+"'s CHANCE");
                startButton.setEnabled(false);
                play=true;
                p2Label.setText(name2);
                p1Field.setEditable(false);
                p2Field.setEditable(false);
                changeButton.setEnabled(false);
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Please choose different name.");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Please choose a valid name.");
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void changeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeButtonActionPerformed
        if(p1Field.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please choose a valid symbol for player 1.");
        }
        else if(p2Field.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null,"Please choose a valid symbol for player 2.");
        }
        else
        {
            if(!p1Field.getText().equals(p2Field.getText()))
            {
                System.out.println(p1Field.getText().equals(p2Field.getText()));
                symbol_p1=p1Field.getText();
                symbol_p2=p2Field.getText();
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Please choose different symbols.");
            }
        }
    }//GEN-LAST:event_changeButtonActionPerformed

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
    private javax.swing.JLabel chanceLabel;
    private javax.swing.JButton changeButton;
    private javax.swing.JTextField nameTextfield;
    private javax.swing.JButton nextButton;
    private javax.swing.JTextField p1Field;
    private javax.swing.JLabel p1Label;
    private javax.swing.JTextField p2Field;
    private javax.swing.JLabel p2Label;
    private javax.swing.JLabel playerLabel;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
