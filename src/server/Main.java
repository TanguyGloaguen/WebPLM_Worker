package server;

import java.util.Locale;

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
	public static boolean state;

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
			System.out.println(request.getLessonID());
			game.switchLesson(request.getLessonID(), false);
			game.switchExercise(request.getExerciseID());
			// Bind listener to game
			state = true;
			listener.setWorld(game.getSelectedWorld());
			resultLstn.setGame(game);
			// Put code in compiler.
		    ((Exercise) game.getCurrentLesson().getCurrentExercise()).getSourceFile(game.getProgrammingLanguage(), 0).setBody(request.getCode());
			// Start the game.
			game.startExerciseExecution();
			// Delete the game instance.
			while(state)
				try {
				    Thread.sleep(1000);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			System.out.println("End compil");
			game = null;
		}
	}
}
