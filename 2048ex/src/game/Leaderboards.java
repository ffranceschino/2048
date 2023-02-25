package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Leaderboards {
    
    private static Leaderboards lBoard;
    private String filePath;
    private final String highScores;
	
    private final ArrayList<Integer> topScores;
    private final ArrayList<Integer> topTiles;
    private final ArrayList<Long> topTimes;
	
    private Leaderboards(){
	try {
            filePath = new File("").getAbsolutePath();
            System.out.println(filePath);
	} catch (Exception e) {
            e.printStackTrace();
	}
        highScores = "Scores";
		
	topScores = new ArrayList<>();
	topTiles = new ArrayList<>();
	topTimes = new ArrayList<>();
    }
	
    public static Leaderboards getInstance(){
	if(lBoard == null){
            lBoard = new Leaderboards();
	}
	return lBoard;
    }
	
    public void addScore(int score){
	for(int i = 0; i < topTiles.size(); i++){
            if(score >= topScores.get(i)){
		topScores.add(i, score);
		topScores.remove(topScores.size() - 1);
		return;
            }
	}
    }

    public void addTile(int tileValue){
	for(int i = 0; i < topTiles.size(); i++){
            if(tileValue >= topTiles.get(i)){
		topTiles.add(i, tileValue);
		topTiles.remove(topTiles.size() - 1);
		return;
            }
	}
    }

    public void addTime(long millis){
	for(int i = 0; i < topTimes.size(); i++){
            if(millis <= topTimes.get(i)){
		topTimes.add(i, millis);
		topTimes.remove(topTimes.size() - 1);
		return;
            }
	}
    }
	
    public void loadScores() {
	try {
            File f = new File(filePath, highScores);
            if (!f.isFile()) {
		createSaveData();
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
                topScores.clear();
                topTiles.clear();
                topTimes.clear();
                
                String[] scores = reader.readLine().split("-");
                String[] tiles = reader.readLine().split("-");
                String[] times = reader.readLine().split("-");
                
                for (int i = 0; i < scores.length; i++) {
                    topScores.add(Integer.parseInt(scores[i]));
                }
                for (int i = 0; i < tiles.length; i++) {
                    topTiles.add(Integer.parseInt(tiles[i]));
                }
                for (int i = 0; i < times.length; i++) {
                    topTimes.add(Long.parseLong(times[i]));
                }
            }
	} catch (IOException | NumberFormatException e) {
            e.printStackTrace();
	}
    }

    public void saveScores() {
	FileWriter output = null;

	try {
            File f = new File(filePath, highScores);
            output = new FileWriter(f);
            try (BufferedWriter writer = new BufferedWriter(output)) {
                writer.write(topScores.get(0) + "-" + topScores.get(1) + "-" + topScores.get(2) + "-" + topScores.get(3) + "-" + topScores.get(4));
                writer.newLine();
                writer.write(topTiles.get(0) + "-" + topTiles.get(1) + "-" + topTiles.get(2) + "-" + topTiles.get(3) + "-" + topTiles.get(4));
                writer.newLine();
                writer.write(topTimes.get(0) + "-" + topTimes.get(1) + "-" + topTimes.get(2) + "-" + topTimes.get(3) + "-" + topTimes.get(4));
            }
	} catch (IOException ex) {
            ex.printStackTrace();
	}
    }

    private void createSaveData() {
	try {
            File file = new File(filePath, highScores);
            FileWriter output = new FileWriter(file);
            try (BufferedWriter writer = new BufferedWriter(output)) {
                writer.write("0-0-0-0-0");
                writer.newLine();
                writer.write("0-0-0-0-0");
                writer.newLine();
                writer.write(Integer.MAX_VALUE + "-" + Integer.MAX_VALUE + "-" + Integer.MAX_VALUE + "-" + Integer.MAX_VALUE + "-" + Integer.MAX_VALUE);
            }
	} catch (IOException e) {
            e.printStackTrace();
	}
    }
	
    public int getHighScore(){
	return topScores.get(0);
    }
	
    public long getFastestTime(){
	return topTimes.get(0);
    }

    public ArrayList<Integer> getTopScores() {
	return topScores;
    }

    public ArrayList<Integer> getTopTiles() {
	return topTiles;
    }

    public ArrayList<Long> getTopTimes() {
	return topTimes;
    }
    
}
