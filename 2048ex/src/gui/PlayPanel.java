package gui;

import game.DB;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import game.DrawUtils;
import game.Game;
import game.GameBoard;
import game.Keys;
import game.ScoreManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PlayPanel extends GuiPanel {
    
    private final GameBoard board;
    private final BufferedImage info;
    private final ScoreManager scores;
    private final Font scoreFont;
    private String timeF;
    private String bestTimeF;
    private final GuiButton mainMenu;
    private final GuiButton screenShot;
    private final int smallButtonWidth = 160;
    private final int spacing = 20;
    private final int largeButtonWidth = smallButtonWidth * 2 + spacing;
    private final int buttonHeight = 50;
    private boolean added;
    private int alpha = 0;
    private final Font gameOverFont;
    private boolean screenshot;
    private boolean newGame;
    private final MainMenuPanel mainMenuPanel = new MainMenuPanel();
    
    public PlayPanel() {
	scoreFont = Game.MAIN.deriveFont(24f);
	gameOverFont = Game.MAIN.deriveFont(70f);
	board = new GameBoard(Game.WIDTH / 2 - GameBoard.BOARD_WIDTH / 2, Game.HEIGHT - GameBoard.BOARD_HEIGHT - 20);
	scores = board.getScores();
	info = new BufferedImage(Game.WIDTH, 200, BufferedImage.TYPE_INT_RGB);
	mainMenu = new GuiButton(Game.WIDTH / 2 - largeButtonWidth / 2, 450, largeButtonWidth, buttonHeight);
	screenShot = new GuiButton(Game.WIDTH / 2 - largeButtonWidth / 2, 375, largeButtonWidth, buttonHeight);
	screenShot.setText("Screenshot");
	mainMenu.setText("Back to Main Menu");
	screenShot.addActionListener((ActionEvent e) -> {
            screenshot = true;
        });
	mainMenu.addActionListener((ActionEvent e) -> {
            newGame=true;
            GuiScreen.getInstance().setCurrentPanel("Menu");
        });
    }

    private void drawGui(Graphics2D g) {
	
	timeF = DrawUtils.formatTime(scores.getTime());
	bestTimeF = DrawUtils.formatTime(scores.getBestTime());
	long x = scores.getBestTime();
	
	Graphics2D g2d = (Graphics2D) info.getGraphics();
	g2d.setColor(Color.white);
	g2d.fillRect(0, 0, info.getWidth(), info.getHeight());
	g2d.setColor(Color.lightGray);
	g2d.setFont(scoreFont);
	g2d.drawString("" + scores.getCurrentScore(), 30, 40);
	g2d.setColor(Color.red);
        //g2d.drawString("Best: " + scores.getCurrentTopScore(), Game.WIDTH - DrawUtils.getMessageWidth("Best: " + scores.getCurrentTopScore(), scoreFont, g2d) - 20, 40);
        g2d.drawString("Best: " + DB.getInstance().getBestScore(DB.getCurrentUsername()), Game.WIDTH - DrawUtils.getMessageWidth("Best: " + DB.getInstance().getBestScore(DB.getCurrentUsername()), scoreFont, g2d) - 20, 40);
        if (x == Integer.MAX_VALUE) 
            g2d.drawString("Fastest: " + "No Time", Game.WIDTH - DrawUtils.getMessageWidth("Fastest: " + "No Time", scoreFont, g2d) - 20, 90);
        else
            g2d.drawString("Fastest: " + bestTimeF, Game.WIDTH - DrawUtils.getMessageWidth("Fastest: " + bestTimeF, scoreFont, g2d) - 20, 90);
	g2d.setColor(Color.black);
	g2d.drawString("Time: " + timeF, 30, 90);
	g2d.dispose();
	g.drawImage(info, 0, 0, null);
    }

    public void drawGameOver(Graphics2D g) {
	g.setColor(new Color(222, 222, 222, alpha));
	g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
	g.setColor(Color.red);
        g.setFont(gameOverFont);
	g.drawString("Game Over!", Game.WIDTH / 2 - DrawUtils.getMessageWidth("Game Over!", gameOverFont, g) / 2, 250);
        g.setColor(Color.black);
        g.setFont(scoreFont);
        g.drawString("Press ESC to Try Again", Game.WIDTH / 2 - DrawUtils.getMessageWidth("Press ESC to Try Again", scoreFont, g) / 2, 325);
    }

    @Override
    public void update() {
	board.update();
        if(true==mainMenuPanel.getDiff()) {
            newGame=true;
            mainMenuPanel.setDiff(false);
        }
        newGame();
	if (board.isDead()) {
            alpha++;
            if (alpha > 170) alpha = 170;
	}
                	
    }
	
    @Override
    public void render(Graphics2D g) {
	drawGui(g);
	board.render(g);
	if (screenshot) {
            BufferedImage bi = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.getGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            drawGui(g2d);
            board.render(g2d);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    ImageIO.write(bi, "png", new File(file.getAbsolutePath()));
                } catch (IOException ex) {
                    System.out.println("Failed to save image!");
                }
            } else {
                System.out.println("No file choosen!");
            }
            screenshot = false;
	}
	if (board.isDead()) {
            if (!added) {
		added = true;
		add(mainMenu);
		add(screenShot);
            }
            drawGameOver(g);
        }
	super.render(g);
    }
        
    public void newGame(){
        if(!Keys.pressed[KeyEvent.VK_ESCAPE]&&Keys.prev[KeyEvent.VK_ESCAPE]||newGame){
            board.reset();
            scores.reset();
            if(added){
                remove(mainMenu);
                remove(screenShot);
                alpha=0;
                added=false;
            }
            newGame=false;
        }
    }
    
}
