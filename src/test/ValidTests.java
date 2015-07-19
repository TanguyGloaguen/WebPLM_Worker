package test;

import java.util.Locale;
import java.lang.reflect.*;

import server.Main;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;

import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise;
import server.ServerLogHandler;
import server.listener.BasicListener;
import server.listener.ResultListener;

public class ValidTests {
	private final static LogHandler logger = new ServerLogHandler();
	private static Game game = null;
	private static BasicListener listener;
	private static ResultListener resultLstn;
	private static Channel channelOut = new ChannelTest();
	public void init() {
		game = new Game("test", logger, Locale.FRENCH, "Java" , false);
		listener = new BasicListener(channelOut,  "chanOut", 500);
		resultLstn = new ResultListener(channelOut, "chanOut", listener);
		BasicProperties replyProps = new BasicProperties
                .Builder()
                .correlationId("TestCorrID")
                .build();
		
		listener.setProps(replyProps);
		resultLstn.setProps(replyProps);
	}
	
	public int initWithParams(String lessID, String exID, String code) {
		init();
		game.switchLesson(lessID, false);
		game.switchExercise(exID);
		listener.setWorld(game.getSelectedWorld());
		resultLstn.setGame(game);
	    ((Exercise) game.getCurrentLesson().getCurrentExercise()).getSourceFile(game.getProgrammingLanguage(), 0).setBody(code);
		game.startExerciseExecution();
		// Stop the game instance after 30s or wait until it stops
		try {
			if(!Main.endExercise.tryAcquire(30, java.util.concurrent.TimeUnit.SECONDS)) {
				game.stopExerciseExecution();
			}
				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public static void main(String[] args) {
		new ValidTests().initWithParams("lessons.welcome", "welcome.lessons.welcome.environment.Environment", "avance();");
	}
}
