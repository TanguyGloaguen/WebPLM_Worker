package server;

import java.util.Locale;
import java.util.concurrent.Semaphore;

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
import com.rabbitmq.client.QueueingConsumer;

public class Main {
	private final static String QUEUE_NAME_REQUEST = "worker_in";
	private final static String QUEUE_NAME_REPLY = "worker_out";
	private final static LogHandler logger = new ServerLogHandler();
	private static Game game = null;
	public static Semaphore endExercise = new Semaphore(0);

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channelIn = connection.createChannel(),
				channelOut = connection.createChannel();

		channelIn.queueDeclare(QUEUE_NAME_REQUEST, false, false, false, null);
		channelOut.queueDeclare(QUEUE_NAME_REPLY, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channelIn);
		channelIn.basicConsume(QUEUE_NAME_REQUEST, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			BasicProperties props = delivery.getProperties();
			System.out.println(props.getCorrelationId());
		    BasicProperties replyProps = new BasicProperties
                    .Builder()
                    .correlationId(props.getCorrelationId())
                    .build();
			String message = new String(delivery.getBody(),"UTF-8");
			System.out.println(" [x] Received '" + message + "'");
			RequestMsg request = RequestMsg.readMessage(message);
			// Create game.
			game = new Game(request.getUserUUID(), logger, Locale.forLanguageTag(request.getLocale()), request.getLanguage(), false);
			BasicListener listener = new BasicListener(channelOut,  QUEUE_NAME_REPLY,  replyProps);
			ResultListener resultLstn = new ResultListener(channelOut, QUEUE_NAME_REPLY, replyProps);
			// Set game state
			game.switchLesson(request.getLessonID(), false);
			game.switchExercise(request.getExerciseID());
			// Bind listener to game
			listener.setWorld(game.getSelectedWorld());
			resultLstn.setGame(game);
			// Put code in compiler.
		    ((Exercise) game.getCurrentLesson().getCurrentExercise()).getSourceFile(game.getProgrammingLanguage(), 0).setBody(request.getCode());
			// Start the game.
			game.startExerciseExecution();
			// Delete the game instance.
			endExercise.acquire();
			System.out.println("End compil");
			game = null;
		}
	}
}
