package server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Exercise;
import server.listener.BasicListener;
import server.listener.ResultListener;
import server.parser.*;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * The main class. This should be the entry point of the Judge.
 * @author Tanguy
 *
 */
public class Main {
	private static String host;
	private static String port;
	
	private static GameGest gest;
	private static Connector connector = new Connector();
	
	/**
	 * Release the game execution semaphore.
	 * This function should be called ONLY when the GameStateListener has his state set to ENDED.
	 */

	/**
	 * Initialize the connection with the message queue, as well as the {@link Game} instance.
	 */
	public static void initData() {
		System.out.println(" [D] Attempting to connect to " + host + ":" + port);
		connector.init(host, Integer.parseInt(port));
		try {
			System.out.println(" [D] Creating game.");
			gest = new GameGest(connector);
		}
		catch (Exception e) {
			System.err.println(" [E] Error while creating game. Aborting...");
		}
	}
	
	/**
	 * This is the main loop of the system.
	 */
	public static void mainLoop() {
		System.out.println(" [D] Retrieving request handler.");
		connector.prepDelivery();
		System.out.println(" [D] Waiting for request.");
		while (true) {
			QueueingConsumer.Delivery delivery = connector.getDelivery();
			BasicProperties props = delivery.getProperties();
		    BasicProperties replyProps = new BasicProperties
                    .Builder()
                    .correlationId(props.getCorrelationId())
                    .build();
			String message = "";
			try {
				message = new String(delivery.getBody(),"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			System.out.println(" [D] Received request from '" + props.getCorrelationId() + "'.");
			RequestMsg request = RequestMsg.readMessage(message);
			// Setting return data
			gest.setProperties(replyProps);
			// Set game state
			gest.setGameState(Locale.forLanguageTag(request.getLocale()), request.getLanguage(), request.getLessonID(), request.getExerciseID());
			// Put code in compiler.
			System.out.println(" [D] Starting compilation.");
			gest.setCode(request.getCode());
			// Start the game.
			gest.startGame(30);
			System.out.println(" [D] Ended compilation.");
		}
	}

	public static void main(String[] argv) {
		host = System.getenv("MESSAGEQ_PORT_5672_TCP_ADDR");
		port = System.getenv("MESSAGEQ_PORT_5672_TCP_PORT");
		host = host != null ? host : "localhost";
		port = port != null ? port : "5672";
		initData();
		mainLoop();
	}
}
