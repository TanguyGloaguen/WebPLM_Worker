package server;

import java.util.Locale;
import java.util.concurrent.Semaphore;

import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import server.listener.BasicListener;
import server.listener.ResultListener;
import server.parser.*;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class Main {
	private final static String QUEUE_NAME_REQUEST = "worker_in";
	private final static String QUEUE_NAME_REPLY = "worker_out";
	private final static LogHandler logger = new ServerLogHandler();
	private static Game game = null;
	public static Semaphore endExercise = new Semaphore(0);

	public static void main(String[] argv) throws Exception {
		String host = argv.length >= 1 ? argv[0] : "localhost";
		
		System.out.println("Started Worker on queue server at : " + host);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		Connection connection = factory.newConnection();
		Channel channelIn = connection.createChannel(),
				channelOut = connection.createChannel();
		channelIn.queueDeclare(QUEUE_NAME_REQUEST, false, false, false, null);
		channelOut.queueDeclare(QUEUE_NAME_REPLY, false, false, false, null);
		// Create game.
		System.out.println(" [D] Creating game.");
		game = new Game("test", logger, Locale.FRENCH,"Java" , false);
		BasicListener listener = new BasicListener(channelOut,  QUEUE_NAME_REPLY);
		ResultListener resultLstn = new ResultListener(channelOut, QUEUE_NAME_REPLY);
		System.out.println(" [D] Waiting for request.");

		QueueingConsumer consumer = new QueueingConsumer(channelIn);
		channelIn.basicConsume(QUEUE_NAME_REQUEST, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			BasicProperties props = delivery.getProperties();
		    BasicProperties replyProps = new BasicProperties
                    .Builder()
                    .correlationId(props.getCorrelationId())
                    .build();
			String message = new String(delivery.getBody(),"UTF-8");
			System.out.println(" [D] Received request from '" + props.getCorrelationId() + "'.");
			RequestMsg request = RequestMsg.readMessage(message);
			// Setting return data
			listener.setProps(replyProps);
			resultLstn.setProps(replyProps);
			// Set game state
			try {
				Locale lang = Locale.forLanguageTag(request.getLocale());
				game.setLocale(lang);
			}
			catch (Exception e) {
				System.err.println(" [E] Error while setting Locale. (original message : '" + message + "'");
				e.printStackTrace();
			}
			ExecutionProgress exPro;
			try {
				game.setProgrammingLanguage(request.getLanguage());
			}
			catch (Exception e) {
				System.err.println(" [E] Error while setting Prog. Language. (original message : '" + message + "'");
				e.printStackTrace();
			}
			game.switchLesson(request.getLessonID(), false);
			game.switchExercise(request.getExerciseID());
			// Bind listener to game
			listener.setWorld(game.getSelectedWorld());
			resultLstn.setGame(game);
			// Put code in compiler.
			System.out.println(" [D] Starting compilation.");
		    ((Exercise) game.getCurrentLesson().getCurrentExercise()).getSourceFile(game.getProgrammingLanguage(), 0).setBody(request.getCode());
			// Start the game.
			game.startExerciseExecution();
			// Delete the game instance.
			endExercise.acquire();
			System.out.println(" [D] Ended compilation.");
		}
	}
}
