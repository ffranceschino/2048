package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import game.DrawUtils;
import game.Game;

public class MainMenuPanel extends GuiPanel{
    
    private final Font titleFont = Game.MAIN.deriveFont(100f);
    private final Font creatorFont = Game.MAIN.deriveFont(24f);
    private final String title = "2048";
    private String creator;
    private final int buttonWidth = 220;
    private final int a = 310;
    private static boolean diff = false;
    private final GuiButton resumeButton;
    private final GuiButton playButton;	
    private final GuiButton scoresButton;
    private final GuiButton quitButton;
    
    public MainMenuPanel() {
	super(); 
        resumeButton=new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, a-90, buttonWidth, 60);
        playButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, a, buttonWidth, 60);	
        scoresButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, a+90, buttonWidth, 60);
        quitButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2, a+180, buttonWidth, 60);
        resumeButton.addActionListener((ActionEvent e) -> {
            GuiScreen.getInstance().setCurrentPanel("Play");
        });
        resumeButton.setText("Resume");
        add(resumeButton);
        playButton.addActionListener((ActionEvent e) -> {
            diff=true;
            GuiScreen.getInstance().setCurrentPanel("Difficulty");
        });
        playButton.setText("Play");
        add(playButton);
        scoresButton.addActionListener((ActionEvent e) -> {
            GuiScreen.getInstance().setCurrentPanel("Leaderboards");
        });
	scoresButton.setText("Scores");
	add(scoresButton);
	quitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
	quitButton.setText("Quit");
	add(quitButton);
    }
    
    public void setCreator(String username) {
        this.creator = "Hi, " + username;
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
            g.setFont(creatorFont);
            g.drawString(creator, 20, Game.HEIGHT - 10);
	}
        
        public boolean getDiff() {
            return diff;
	}
	
	public void setDiff(boolean diff) {
            MainMenuPanel.diff = diff;
	}
    
}
