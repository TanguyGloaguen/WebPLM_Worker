package server;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import com.rabbitmq.client.AMQP.BasicProperties;

import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.universe.World;
import server.listener.BasicListener;
import server.listener.ResultListener;

public class GameGest {
	private Game game;
	private BasicListener listener;
	private ResultListener resultLstn;
	private ListenerOutStream listenerOut;
	public Semaphore endExercise = new Semaphore(0);
	private ArrayList<BasicListener> lCumul = new ArrayList<BasicListener>();
	
	public GameGest(Connector connector, LogHandler logger) {
		game = new Game("test", logger, Locale.FRENCH,"Java" , false);
		listener = new BasicListener(connector, 1000);
		resultLstn = new ResultListener(connector, this);
		listenerOut = new ListenerOutStream(System.out, listener);
		PrintStream outStream = new PrintStream(listenerOut, true);  //Direct to MyOutputStream, autoflush
        System.setOut(outStream); 
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
		lCumul = new ArrayList<BasicListener>();
		// Bind listener to game
		listener.setWorld(game.getSelectedWorld());
		for(World w : ((Exercise) game.getCurrentLesson().getCurrentExercise()).getWorlds(WorldKind.CURRENT)) {
			BasicListener l = listener.clone();
			l.setWorld(w);
			lCumul.add(l);
		}
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
		for(BasicListener l : lCumul)
			l.setProps(properties);
		resultLstn.setProps(properties);
	}
	
	public void sendStream() {
		listener.send();
		for(BasicListener l : lCumul)
			l.send();
	}
	
	public void free() {
		endExercise.release();
	}
	
	public void stop() {
		game.quit();
	}
}
