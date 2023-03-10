package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import gui.GuiScreen;
import gui.LeaderboardsPanel;
import gui.MainMenuPanel;
import gui.PlayPanel;
import gui.DifficultyPanel;


public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    public static final int WIDTH = GameBoard.BOARD_WIDTH + 40;
    public static final int HEIGHT = 630;
    public static final Font MAIN = new Font("Langdon", Font.PLAIN, 28);
    private Thread game;
    private boolean running;
    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final GuiScreen screen;
    private String username;
	
    public Game() {
	setFocusable(true);
	setPreferredSize(new Dimension(WIDTH, HEIGHT));
	addKeyListener(this);
	addMouseListener(this);
	addMouseMotionListener(this);
	screen = GuiScreen.getInstance();
        String username = DB.getCurrentUsername();
        MainMenuPanel mainMenuPanel = new MainMenuPanel();
        mainMenuPanel.setCreator(username);
	screen.add("Menu", mainMenuPanel);
	screen.add("Play", new PlayPanel());
        screen.add("Difficulty",new DifficultyPanel());
	screen.add("Leaderboards", new LeaderboardsPanel());
        screen.setCurrentPanel("Menu");
    }

    private void update() {
	screen.update();
        Keys.update();
    }

    private void render() {
	Graphics2D g = (Graphics2D) image.getGraphics();
	g.setColor(Color.white);
	g.fillRect(0, 0, WIDTH, HEIGHT);
	screen.render(g);
	g.dispose();
        Graphics2D g2d = (Graphics2D) getGraphics();
	g2d.drawImage(image, 0, 0, null);
	g2d.dispose();
    }

    @Override
    public void run() {
	int fps = 0, updates = 0;
	long fpsTimer = System.currentTimeMillis();
	double nsPerUpdate = 1000000000.0 / 60;

	double then = System.nanoTime();
	double unprocessed = 0;

	while (running) {
            boolean shouldRender = false;
            double now = System.nanoTime();
            unprocessed += (now - then) / nsPerUpdate;
            then = now;

            while (unprocessed >= 1) {

                updates++;
                update();
                unprocessed--;
                shouldRender = true;
            }

            if (shouldRender) {
                fps++;
                render();
                shouldRender = false;
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (System.currentTimeMillis() - fpsTimer > 1000) {
                fps = 0;
                updates = 0;
                fpsTimer += 1000;
            }
	}
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        game = new Thread(this, "game");
        game.start();
    }

    public synchronized void stop() {
        if (!running) return;
	running = false;
	System.exit(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
		
    }

    @Override
    public void keyPressed(KeyEvent e) {
	Keys.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
	Keys.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
	screen.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	screen.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	screen.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	screen.mouseMoved(e);
    }
}
