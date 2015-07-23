package test;

import java.util.Locale;
import server.GameGest;
import com.rabbitmq.client.AMQP.BasicProperties;


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
		new ValidTests().initWithParams("lessons.sort.basic", "lessons.sort.basic", "public void bubbleSort()  {boolean stop = false; while(!stop) { stop = true; for(int i = 0; i < getValueCount()-1;i++) { if(isSmaller(i+1, i)) { swap(i+1, i); stop = false;}}}}");
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
