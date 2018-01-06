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
	
	Rectangle s = new Rectangle(380, 380, 20, 20);
	
	private int score = 0;
	private Text scoreText = new Text("Score: " + score);
	
	private boolean gameOver = false;
	
	Text result;
	Text retry = new Text("Press space to retry");
	
	private Random rand = new Random();
	
	Pane root = new Pane();
	
	private Parent create() {
		root.setPrefSize(800, 800);
		
		s.setFill(Color.LIME);
		
		snake.add(s);
		
		Rectangle f = new Rectangle(rand.nextInt(40)*20, rand.nextInt(40)*20, 20, 20);
		f.setFill(Color.INDIANRED);
		
		scoreText.setX(700);
		scoreText.setY(30);
		scoreText.setFont(Font.font(20));
		
		Rectangle bg = new Rectangle(800, 800);
		bg.setFill(Color.SKYBLUE);
		
		root.getChildren().addAll(bg, s, f, scoreText);
				
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.035), event -> {
			if (!gameOver) {
				for (int i = snake.size() - 1; i > 0; i--) {
					snake.get(i).setX(snake.get(i-1).getX());
					snake.get(i).setY(snake.get(i-1).getY());
				}
				
				s.setX(s.getX() + xSpeed * 20);
				s.setY(s.getY() + ySpeed * 20);
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
			
			if (s.getX() == root.getWidth() || s.getX() == -20 || s.getY() == root.getHeight() || s.getY() == -20) {
				gameOver = true;
				gameOverScreen();
				timeline.stop();
			}
			
			if (s.getX() == f.getX() && s.getY() == f.getY()) {
				f.setX(rand.nextInt(40)*20);
				f.setY(rand.nextInt(40)*20);
				
				Rectangle rect = new Rectangle(snake.get(snake.size() - 1).getX(), snake.get(snake.size() - 1).getY(), 20, 20);
				rect.setFill(Color.LIME);
				root.getChildren().add(rect);
				snake.add(rect);
				
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
		result = new Text("Score: " + score);
		result.setFont(Font.font("Arial Black", 80));
		result.setFill(Color.WHITE);
		result.setX(70);
		result.setY(360);
		
		retry.setFont(Font.font(30));
		retry.setFill(Color.WHITE);
		retry.setX(70);
		retry.setY(410);
		
		root.getChildren().addAll(result, retry);
	}
	
	private void restart() {
		if (snake.size() > 1) {
			for (int i = snake.size() - 1; i > 0; i--) {
				root.getChildren().remove(snake.get(i));
				snake.remove(i);
			}
		}
		
		s.setX(380);
		s.setY(380);
		
		dir(1, 0);
		
		score = 0;
		scoreText.setText("Score: " + score);
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
	
	private void dir(int x, int y) {
		xSpeed = x;
		ySpeed = y;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
