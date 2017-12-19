/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author nikhil
 */
public class GameWindow extends javax.swing.JFrame 
{
   /**
     * Creates new form GameWindow
     */
    private int start=0;//for controlling whether the game should be started or not
    private JLabel playinglabels[][] = new JLabel[3][3];
    private final int PLAYINGAREAX = 242, PLAYINGAREAY = 100, CELLSIZE = 25, SPACEBETWEENCELLS = 20;
    private int currentPlayer = 0;
    private int c=9;
    //private JLabel player1_name=new JLabel();
    //private JLabel player2_name=new JLabel();
    
    
    private void reset()
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                playinglabels[i][j].setText(".");
            }
        }
        currentPlayer = 0;
        guide1.setVisible(true);
        guide1.setText("");
        Name_1_TF.setVisible(true);
        Name_1_TF.setEnabled(true);
        Name_1_TF.setEditable(true);
        Name_1_TF.setText("");
        Player1_name.setText("ENTER PLAYER 1 NAME");
        guide2.setText("");
        guide2.setVisible(true);
        Name_2_TF.setVisible(true);
        Name_2_TF.setEnabled(true);
        Name_2_TF.setEditable(true);
        Name_2_TF.setText("");
        Player2_name.setText("ENTER PLAYER 2 NAME");
        X_1.setEnabled(true);X_1.setEnabled(true);X_2.setEnabled(true);O_1.setEnabled(true);O_2.setEnabled(true);
        Start.setEnabled(true);
        c=0;
        start=0;
    }
    
    
    

    private void endGame(int currentPlayer)
    {
        TURN.setText("");
        if(currentPlayer==0)
        JOptionPane.showMessageDialog(this, Player1_name.getText()+" WINS!!!");
        else JOptionPane.showMessageDialog(this, Player2_name.getText()+" WINS!!!");
        int playAgainOrNot = JOptionPane.showConfirmDialog(this, "Want to play again???", "Continue?", 0);
        if(playAgainOrNot == JOptionPane.YES_OPTION)
            reset();
        else 
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));        
    }    
    
    private void check(int currentPlayer)
    {
        String currentPlayerMove="";
        if(currentPlayer==0) currentPlayerMove=guide1.getText();
        if(currentPlayer==1) currentPlayerMove = guide2.getText();
        boolean isGameOver = false;
        boolean flag;

        // checking horizontal match
        for(int i = 0; i < 3; i++)
        {
            flag = true;
            for( int j = 0; j < 3; j++)
                if(!playinglabels[i][j].getText().equals(currentPlayerMove))
                    flag = false;
            if(flag)
                isGameOver = true;
        }
        
        // checking vertical match
        for(int j = 0; j < 3; j++)
        {
            flag = true;
            for( int i = 0; i < 3; i++)
                if(!playinglabels[i][j].getText().equals(currentPlayerMove))
                    flag = false;
            if(flag)
                isGameOver = true;
        }
        
        // checking diagonals match
        flag = true;
        for(int i = 0; i < 3; i++)
        {
            if(!playinglabels[i][i].getText().equals(currentPlayerMove))
                flag = false;
        }
        if(flag)
            isGameOver = true;
        
        flag = true;
        for(int i = 0; i < 3; i++)
        {
            if(!playinglabels[i][2 - i].getText().equals(currentPlayerMove))
                flag = false;
        }
        if(flag)
            isGameOver = true;
        
        
        //checking for draw        
        if(c==0)
        {
            JOptionPane.showMessageDialog(this,"ITS A DRAW!!");
            int playAgainOrNot = JOptionPane.showConfirmDialog(this, "Want to play again???", "Continue?", 0);
            if(playAgainOrNot == JOptionPane.YES_OPTION)
                reset();
            else 
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        
        if(isGameOver)
            endGame(currentPlayer);
        
    }
    
    private void cellClicked(MouseEvent evt)
    {
        if(start==1)
        {
            c--;
            JLabel currentCell = (JLabel) evt.getComponent();        
            if(currentPlayer == 0)
            {
                currentCell.setText(guide1.getText());
                TURN.setText(Player2_name.getText()+"'s turn");
            }
            else
            {           
                currentCell.setText(guide2.getText());
                TURN.setText(Player1_name.getText()+"'s turn");
            }
            currentPlayer =(currentPlayer + 1) % 2;
            check( (currentPlayer + 1) % 2 );
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Please Start The Game");
        }
    }
    
    private void addPlayingComponents(JPanel playingArea)
    {
        for(int i = 0; i < 3; i++)
        {
            for( int j = 0; j< 3 ; j++)
            {
                
                playinglabels[i][j] = new JLabel(".");
                playinglabels[i][j].setFont(new java.awt.Font("Ubuntu", 1, 18));
                playinglabels[i][j].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                playinglabels[i][j].setVisible(true);
                playinglabels[i][j].setLocation(i * (CELLSIZE + SPACEBETWEENCELLS), j * (CELLSIZE + SPACEBETWEENCELLS));
                playinglabels[i][j].setSize(CELLSIZE, CELLSIZE);
                playingArea.add(playinglabels[i][j]);                
                playinglabels[i][j].addMouseListener(new MouseAdapter()
                {
                    public void mouseClicked(MouseEvent evt)
                    {
                        cellClicked(evt);
                    }
                });
            }
        }
        String name1=Name_1_TF.getText();
        String name2=Name_2_TF.getText();
        
       
        
    }
    
    public GameWindow() //constructor
    {
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        title = new javax.swing.JLabel();
        Name_1_TF = new javax.swing.JTextField();
        Name_2_TF = new javax.swing.JTextField();
        Player1_name = new javax.swing.JLabel();
        Player2_name = new javax.swing.JLabel();
        guide1 = new javax.swing.JLabel();
        guide2 = new javax.swing.JLabel();
        TURN = new javax.swing.JLabel();
        X_1 = new javax.swing.JRadioButton();
        O_1 = new javax.swing.JRadioButton();
        X_2 = new javax.swing.JRadioButton();
        O_2 = new javax.swing.JRadioButton();
        Start = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        title.setText("TIC TAC TOE");

        Name_1_TF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Name_1_TFActionPerformed(evt);
            }
        });
        Name_1_TF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Name_1_TFKeyPressed(evt);
            }
        });

        Name_2_TF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Name_2_TFActionPerformed(evt);
            }
        });
        Name_2_TF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Name_2_TFKeyPressed(evt);
            }
        });

        Player1_name.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Player1_name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Player1_name.setText("ENTER PLAYER 1 NAME");

        Player2_name.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Player2_name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Player2_name.setText("ENTER PLAYER 2 NAME");

        guide1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        guide1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guide1.setAlignmentX(60.0F);

        guide2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        guide2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guide2.setAlignmentX(60.0F);
        guide2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        TURN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TURN.setAlignmentX(30.0F);

        buttonGroup1.add(X_1);
        X_1.setSelected(true);
        X_1.setText("I WANT X");
        X_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                X_1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(O_1);
        O_1.setText("I WANT O");
        O_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                O_1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(X_2);
        X_2.setText("I WANT X");
        X_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                X_2ActionPerformed(evt);
            }
        });

        buttonGroup2.add(O_2);
        O_2.setSelected(true);
        O_2.setText("I WANT O");
        O_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                O_2ActionPerformed(evt);
            }
        });

        Start.setText("CLICK TO START THE GAME");
        Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(X_1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(X_2)
                .addGap(83, 83, 83))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(O_1)
                        .addGap(88, 88, 88)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TURN, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(title)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Name_1_TF)
                            .addComponent(Player1_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Name_2_TF)
                            .addComponent(Player2_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(47, 47, 47))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(O_2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(guide1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(guide2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Start, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(204, 204, 204))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Player1_name)
                    .addComponent(Player2_name))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Name_2_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Name_1_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guide1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(X_2)
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TURN, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(O_2)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(X_1)
                                .addGap(18, 18, 18)
                                .addComponent(O_1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Start, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guide2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Name_1_TFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Name_1_TFActionPerformed
        
        
    }//GEN-LAST:event_Name_1_TFActionPerformed

    private void Name_2_TFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Name_2_TFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Name_2_TFActionPerformed

    @SuppressWarnings("empty-statement")
    private void Name_1_TFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Name_1_TFKeyPressed
              
        
    }//GEN-LAST:event_Name_1_TFKeyPressed

    private void Name_2_TFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Name_2_TFKeyPressed
        
         
    }//GEN-LAST:event_Name_2_TFKeyPressed

    private void O_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_O_1ActionPerformed
        X_2.setSelected(true);
    }//GEN-LAST:event_O_1ActionPerformed

    private void O_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_O_2ActionPerformed
        X_1.setSelected(true);
    }//GEN-LAST:event_O_2ActionPerformed

    private void X_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_X_1ActionPerformed
        O_2.setSelected(true);
    }//GEN-LAST:event_X_1ActionPerformed

    private void X_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_X_2ActionPerformed
        O_1.setSelected(true);
    }//GEN-LAST:event_X_2ActionPerformed

    private void StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartActionPerformed
        if(Name_1_TF.getText().isEmpty()==true || Name_2_TF.getText().isEmpty()==true)
        {
            JOptionPane.showMessageDialog(this,"Please Enter Name For Both The Players");
        }
        else
        {
            X_1.setEnabled(false);
            X_2.setEnabled(false);
            O_1.setEnabled(false);
            O_2.setEnabled(false);
            Player1_name.setAlignmentX((float) 60.0);
            Player2_name.setAlignmentX((float) 60.0);
            Player1_name.setText(Name_1_TF.getText());
            Player2_name.setText(Name_2_TF.getText());
            Name_1_TF.setEnabled(false);
            Name_2_TF.setEnabled(false);
            Name_1_TF.setVisible(false);
            Name_2_TF.setVisible(false);
            TURN.setText(Player1_name.getText()+"'s Turn");
            if(X_1.isSelected()==true)
            {
                guide1.setText("X");
                guide2.setText("O");
                JOptionPane.showMessageDialog(this,"PLAYER 1 is "+Player1_name.getText()+" With Symbol X  And  "
                + "Player 2 is "+Player2_name.getText()+" With Symbol= O");
            } 
            if(X_2.isSelected()==true)
            {
                guide1.setText("O");
                guide2.setText("X");
                JOptionPane.showMessageDialog(this,"PLAYER 1 is "+Player1_name.getText()+" With Symbol= O  And  "
                + "Player 2 is "+Player2_name.getText()+" With Symbol= X");
            }
            
            start=1;
            Start.setEnabled(false);
            
        }
        
        
        
       
    }//GEN-LAST:event_StartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run() 
            {
                new GameWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Name_1_TF;
    private javax.swing.JTextField Name_2_TF;
    private javax.swing.JRadioButton O_1;
    private javax.swing.JRadioButton O_2;
    private javax.swing.JLabel Player1_name;
    private javax.swing.JLabel Player2_name;
    private javax.swing.JButton Start;
    private javax.swing.JLabel TURN;
    private javax.swing.JRadioButton X_1;
    private javax.swing.JRadioButton X_2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel guide1;
    private javax.swing.JLabel guide2;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
