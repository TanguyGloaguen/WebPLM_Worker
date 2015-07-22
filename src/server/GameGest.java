package server;

import java.util.Locale;
import java.util.concurrent.Semaphore;

import com.rabbitmq.client.AMQP.BasicProperties;

import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise;
import server.listener.BasicListener;
import server.listener.ResultListener;

public class GameGest {
	private final LogHandler logger = new ServerLogHandler();
	private Game game;
	private BasicListener listener;
	private ResultListener resultLstn;
	public Semaphore endExercise = new Semaphore(0);
	
	public GameGest(Connector connector) {
		game = new Game("test", logger, Locale.FRENCH,"Java" , false);
		listener = new BasicListener(connector, 500);
		resultLstn = new ResultListener(connector, this);
	}
	
	public void setGameState(Locale locale, String language, String lessonID, String exerciseID) {
		try {
			game.setLocale(locale);
		}
		catch (Exception e) {
			System.err.println(" [E] Error while setting Locale.");
			e.printStackTrace();
		}
		try {
			game.setProgrammingLanguage(language);
		}
		catch (Exception e) {
			System.err.println(" [E] Error while setting Prog. Language.");
			e.printStackTrace();
		}
		game.switchLesson(lessonID, false);
		game.switchExercise(exerciseID);
		// Bind listener to game
		listener.setWorld(game.getSelectedWorld());
		resultLstn.setGame(game);
	}
	
	public void setCode(String code) {
	    ((Exercise) game.getCurrentLesson().getCurrentExercise()).getSourceFile(game.getProgrammingLanguage(), 0).setBody(code);
	}
	
	public void startGame(long timeout) {
		game.startExerciseExecution();
		try {
			if(!endExercise.tryAcquire(timeout, java.util.concurrent.TimeUnit.SECONDS)) {
				game.stopExerciseExecution();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setProperties(BasicProperties properties) {
		listener.setProps(properties);
		resultLstn.setProps(properties);
	}
	
	public void sendStream() {
		listener.send();
	}
	
	public void free() {
		endExercise.release();
	}
}