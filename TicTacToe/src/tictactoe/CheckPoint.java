/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import javax.swing.JLabel;

/**
 *
 * @author manas
 */
public class CheckPoint extends JLabel {
    int row;
    int column;
    
    public CheckPoint(String str) {
        super(str);
    }
}
