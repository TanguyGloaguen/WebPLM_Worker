package test;

import java.util.Locale;
import java.lang.reflect.*;

import server.Connector;
import server.GameGest;
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
	private static GameGest gest = null;
	private static ConnectorTest connector = new ConnectorTest();
	private static int testID = 1;
	public void init() {
		connector.init("kappa", 1000);
		gest = new GameGest(connector);
		BasicProperties replyProps = new BasicProperties
                .Builder()
                .correlationId("TestCorrID")
                .build();
		gest.setProperties(replyProps);
	}
	
	public int initWithParams(String lessID, String exID, String code) {
		logInfos("Test "+ (testID++), lessID, exID, code);
		init();
		gest.setGameState(Locale.FRENCH, "Java", lessID, exID);
		gest.setCode(code);
		gest.startGame(30);
		return 1;
	}

	public static void main(String[] args) {
		System.out.println(" [T] Starting tests.");
		new ValidTests().initWithParams("lessons.welcome", "welcome.lessons.welcome.environment.Environment", "avance();");
		new ValidTests().initWithParams("lessons.welcome", "welcome.lessons.welcome.environment.Environment", "avance();");
		new ValidTests().initWithParams("lessons.welcome", "welcome.lessons.welcome.environment.Environment", "avance();");
		new ValidTests().initWithParams("lessons.welcome", "welcome.lessons.welcome.environment.Environment", "avance();");
		new ValidTests().initWithParams("lessons.welcome", "welcome.lessons.welcome.environment.Environment", "avance();");
		new ValidTests().initWithParams("lessons.welcome", "welcome.lessons.welcome.environment.Environment", "avance();");
	}
	
	public static void logInfos(String testName, String lessID, String exID, String code) {
		System.out.println("\n----------------------------");
		System.out.println(" [T] " + testName);
		System.out.println(" [T] lesson\t: " + lessID);
		System.out.println(" [T] exercise\t: " + exID);
		System.out.println(" [T] " + code);
		System.out.println("----------------------------");
	}
}
