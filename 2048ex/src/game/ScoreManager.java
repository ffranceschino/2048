package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class ScoreManager {
    
    private int currentScore;
    private int currentTopScore;
    private long difficulty;
    private long time;
    private long startingTime;
    private long bestTime;
    private int[] board = new int[16];

    private String filePath;
    private final String temp;
    private final GameBoard gBoard;

    private boolean newGame;
        	
    public ScoreManager(GameBoard gBoard) {
        try {
            filePath = new File("").getAbsolutePath();
	} catch (Exception e) {
            e.printStackTrace();
	}
	temp = "TEMP.tmp";

	this.gBoard = gBoard;
    }

    public void reset() {
	File f = new File(filePath, temp);
	if (f.isFile()) {
            f.delete();
	}
	newGame = true;
	startingTime = 0;
	currentScore = 0;
	time = 0;
    }

    private void createFile() {
	FileWriter output = null;
	newGame = true;	
	try {
            File f = new File(filePath, temp);
            output = new FileWriter(f);
            try (BufferedWriter writer = new BufferedWriter(output)) {
                writer.write("" + 0);
                writer.newLine();
                writer.write("" + 0);
                writer.newLine();
                writer.write("" + 0);
                writer.newLine();
                writer.write("" + 0);
                writer.newLine();
                writer.write("" + 300000);
                writer.newLine();
                for (int row = 0; row < GameBoard.ROWS; row++) {
                    for (int col = 0; col < GameBoard.COLS; col++) {
                        if(row == GameBoard.ROWS - 1 && col == GameBoard.COLS - 1){
                            writer.write("" + 0);
                        }
                        else{
                            writer.write(0 + "-");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
	}
    }
	
    public void saveGame() {
	FileWriter output = null;
	if (newGame) newGame = false;
	try {
            File f = new File(filePath, temp);
            output = new FileWriter(f);
            try (BufferedWriter writer = new BufferedWriter(output)) {
                writer.write("" + currentScore);
                writer.newLine();
                writer.write("" + currentTopScore);
                writer.newLine();
                writer.write("" + time);
                writer.newLine();
                writer.write("" + bestTime);
                writer.newLine();
                writer.write(""+ difficulty);
                writer.newLine();
                for (int row = 0; row < GameBoard.ROWS; row++) {
                    for (int col = 0; col < GameBoard.COLS; col++) {
                        this.board[row * GameBoard.COLS + col] = gBoard.getBoard()[row][col] != null ? gBoard.getBoard()[row][col].getValue() : 0;
                        if (row == GameBoard.ROWS - 1 && col == GameBoard.COLS - 1)
                            writer.write("" + board[row * GameBoard.COLS + col]);
                        else
                            writer.write(board[row * GameBoard.COLS + col] + "-");
                    }
                }
            }
	} catch (IOException e) {
            e.printStackTrace();
	}
    }
        
    public void shuffle(){
        int[] shuf=new int[16];
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                this.board[row * GameBoard.COLS + col] = gBoard.getBoard()[row][col] != null ? gBoard.getBoard()[row][col].getValue() : 0;
                    shuf[row*GameBoard.COLS+col]=board[row * GameBoard.COLS + col];
            }
        }
        for (int i = 0; i < board.length; i++) {
            Random rand=new Random();
            while(shuf[i]==0&&i!=15){
                i++;
            }
            if(shuf[i]==0&&i==15){
                break;
            }
            int ran=rand.nextInt(16);
            while(shuf[ran]==0){
                ran=rand.nextInt(16);
            }
            int temp=shuf[ran];
            shuf[ran]=shuf[i];
            shuf[i]=temp;
        }
        System.out.print("SHUF!");
        for(int i=0;i<board.length;i++){
            if(getBoard()[i] == 0) continue;
            gBoard.spawn(i / GameBoard.ROWS, i % GameBoard.COLS, shuf[i]);
        }
    }
	
    public void loadGame() {
	try {
            File f = new File(filePath, temp);
		if (!f.isFile()) {
                    createFile();
                }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
                currentScore = Integer.parseInt(reader.readLine());
                currentTopScore = Integer.parseInt(reader.readLine());
                time = Long.parseLong(reader.readLine());
                startingTime = time;
                bestTime = Long.parseLong(reader.readLine());
                difficulty=Long.parseLong(reader.readLine());
                String[] board = reader.readLine().split("-");
                for (int i = 0; i < board.length; i++) {
                    this.board[i] = Integer.parseInt(board[i]);
                }
            }
	} catch (IOException | NumberFormatException e) {
            e.printStackTrace();
	}
    }

    public int getCurrentScore() {
	return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getCurrentTopScore() {
	return currentTopScore;
    }

    public void setCurrentTopScore(int currentTopScore) {
	this.currentTopScore = currentTopScore;
    }

    public long getTime() {
	return time;
    }

    public void setTime(long time) {
	this.time = time + startingTime;
    }

    public long getBestTime() {
	return bestTime;
    }

    public void setBestTime(long bestTime) {
	this.bestTime = bestTime;
    }

    public boolean newGame() {
	return newGame;
    }

    public int[] getBoard() {
	return board;
    }
        
    public long getDifficulty() {
        return difficulty;
    }   

    public void setDifficulty(long difficulty) {
        this.difficulty = difficulty;
    }
    
}
