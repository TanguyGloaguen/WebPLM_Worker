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
	public static final LogHandler logger = new ServerLogHandler();
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
		logger.log(0, "Attempting to connect to " + host + ":" + port);
		connector.init(host, Integer.parseInt(port));
	}
	
	/**
	 * This is the main loop of the system.
	 */
	public static void mainLoop() {
		logger.log(0, "Retrieving request handler.");
		connector.prepDelivery();
		logger.log(0, "Waiting for request.");
		while (true) {
			try {
				logger.log(0, "Creating game.");
				gest = new GameGest(connector, logger);
			}
			catch (Exception e) {
				logger.log(2, "Error while creating game. Aborting...");
			}
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
			logger.log(0, "Received request from '" + props.getCorrelationId() + "'.");
			RequestMsg request = RequestMsg.readMessage(message);
			logger.log(0, "Setting game properties.");
			// Set game state
			gest.setGameState(Locale.forLanguageTag(request.getLocale()), request.getLanguage(), request.getLessonID(), request.getExerciseID());
			// Setting return data
			gest.setProperties(replyProps);
			// Put code in compiler.
			logger.log(0, "Starting compilation.");
			gest.setCode(request.getCode());
			logger.log(0, "Starting execution.");
			// Start the game.
			gest.startGame(30);
			logger.log(0, "Ended compilation.");
			gest.stop();
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
