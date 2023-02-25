/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Sebastiano
 */
public interface GuiComponent {
    void update();
    void render(Graphics2D g);
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseDragged(MouseEvent e);
    void mouseMoved(MouseEvent e);
}
