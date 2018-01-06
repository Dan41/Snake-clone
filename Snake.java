package snakeGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class Snake extends Application {
	
	Timeline timeline;
	
	private int xSpeed = 1;
	private int ySpeed = 0;
	
	private ObservableList<Rectangle> snake = FXCollections.observableArrayList();
	
	Rectangle s = new Rectangle(376, 376, 23, 23);
	
	private boolean overlap;
	
	private int score = 0;
	private Text scoreText = new Text("Score: " + score);
	
	private boolean gameOver = false;
	
	Text result;
	Text retry = new Text("Press space to retry");
	
	private Random rand = new Random();
	
	Pane root = new Pane();
	
	private Parent create() {
		root.setPrefSize(800, 800);
		
		s.setFill(Color.WHITE);
		snake.add(s);
		
		Rectangle f = new Rectangle(rand.nextInt(32)*25+1, rand.nextInt(32)*25+1, 23, 23);
		f.setFill(Color.RED);
		
		scoreText.setX(700);
		scoreText.setY(30);
		scoreText.setFont(Font.font(20));
		scoreText.setFill(Color.LIME);
		
		Rectangle bg = new Rectangle(800, 800);
		bg.setFill(Color.BLACK);
		
		root.getChildren().addAll(bg, scoreText, s, f);
				
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.04), event -> {
			if (!gameOver) {
				for (int i = snake.size() - 1; i > 0; i--) {
					snake.get(i).setX(snake.get(i-1).getX());
					snake.get(i).setY(snake.get(i-1).getY());
				}
				
				s.setX(s.getX() + xSpeed * 25);
				s.setY(s.getY() + ySpeed * 25);
			}
			
			for (int i = snake.size() - 1; i > 0; i--) {
				try {
					if (snake.get(i).getX() == s.getX() && snake.get(i).getY() == s.getY()) {
						gameOver = true;
						gameOverScreen();
						timeline.stop();
					}
				}
				catch(Exception e) {}
			}
			
			if (s.getX() > root.getWidth() || s.getX() < 0 || s.getY() > root.getHeight() || s.getY() < 0) {
				gameOver = true;
				gameOverScreen();
				timeline.stop();
			}
			
			if (s.getX() == f.getX() && s.getY() == f.getY()) {
				while (true) {
					overlap = false;
					f.setX(rand.nextInt(32)*25 + 1);
					f.setY(rand.nextInt(32)*25 + 1);
					
					for(int i = snake.size() - 1; i > 0; i--) {
						if (snake.get(i).getX() == f.getX() && snake.get(i).getY() == f.getY()) {
							overlap = true;
						}
					}
					if (!overlap) {
						break;
					}
				}
				
				Rectangle rect = new Rectangle(snake.get(snake.size() - 1).getX(), snake.get(snake.size() - 1).getY(), 23, 23);
				rect.setFill(Color.WHITE);
				root.getChildren().add(rect);
				snake.add(rect);
				
				root.getChildren().remove(scoreText);
				root.getChildren().add(scoreText);
				score++;
				scoreText.setText("Score: " + score);
			}
		}));
		timeline.setAutoReverse(true);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		
		return root;
	}
	
	private void gameOverScreen() {
		result = new Text("Game over");
		result.setFont(Font.font("Arial Black", 80));
		result.setX(70);
		result.setY(360);
		result.setFill(Color.LIME);
		
		retry.setFont(Font.font(30));
		retry.setX(70);
		retry.setY(410);
		retry.setFill(Color.LIME);
		
		root.getChildren().addAll(result, retry);
	}
	
	private void restart() {
		if (snake.size() > 1) {
			for (int i = snake.size() - 1; i > 0; i--) {
				root.getChildren().remove(snake.get(i));
				snake.remove(i);
			}
		}
		
		s.setX(376);
		s.setY(376);
		
		dir(1, 0);
		
		score = 0;
		scoreText.setText("Score: " + score);
	}
	
	private void dir(int x, int y) {
		xSpeed = x;
		ySpeed = y;
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(create());
		
		scene.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.UP)) {
				if (ySpeed != 1) 
					dir(0, -1);
			}
			if (event.getCode().equals(KeyCode.RIGHT)) {
				if (xSpeed != -1) 
					dir(1, 0);
			}
			if (event.getCode().equals(KeyCode.DOWN)) {
				if (ySpeed != -1) 
					dir(0, 1);
			}
			if (event.getCode().equals(KeyCode.LEFT)) {
				if (xSpeed != 1) 
					dir(-1, 0);
			}
			
			if (event.getCode().equals(KeyCode.SPACE) && gameOver) {
				root.getChildren().removeAll(result, retry);
				restart();
				gameOver = false;
				timeline.play();
			}
		});
		
		stage.setScene(scene);
		stage.setTitle("Snake game");
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
