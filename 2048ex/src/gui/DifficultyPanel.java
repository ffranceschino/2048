package gui;

import game.DrawUtils;
import game.Game;
import game.GameBoard;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

public class DifficultyPanel extends GuiPanel {
    
    private final Font titleFont = Game.MAIN.deriveFont(72f);
    private final String title = "Difficulty";
    private final int buttonWidth = 220;
    private final GuiButton EasyButton,MedButton,HardButton;
    private static final long easy=200000;
    private static final long med=100000;
    private static final long hard=10000;
    
    public DifficultyPanel() {
	EasyButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, 220, buttonWidth, 60);
	EasyButton.addActionListener((ActionEvent e) -> {
            GameBoard.setTime(easy);
            GuiScreen.getInstance().setCurrentPanel("Play");
        });
        EasyButton.setText("Easy");
        add(EasyButton);
        MedButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, 310, buttonWidth, 60);
	MedButton.addActionListener((ActionEvent e) -> {
            GameBoard.setTime(med);
            GuiScreen.getInstance().setCurrentPanel("Play");
        });
	MedButton.setText("Medium");
	add(MedButton);
	HardButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, 400, buttonWidth, 60);
	HardButton.addActionListener((ActionEvent e) -> {
            GameBoard.setTime(hard);
            GuiScreen.getInstance().setCurrentPanel("Play");
        });
	HardButton.setText("Hard");
        add(HardButton);
    }
        
	
    @Override
    public void update(){
    
    }
    
    @Override
    public void render(Graphics2D g){
	super.render(g);
	g.setFont(titleFont);
	g.setColor(Color.black);
	g.drawString(title, Game.WIDTH / 2 - DrawUtils.getMessageWidth(title, titleFont, g) / 2, 150);
    }
    
}
