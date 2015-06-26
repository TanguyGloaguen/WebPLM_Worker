package client;

import java.util.Locale;

import org.json.simple.parser.JSONParser;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Main {
	private final static String QUEUE_NAME_REQUEST = "worker_in";
	private final static String QUEUE_NAME_REPLY = "worker_out";
	private static String corrId = java.util.UUID.randomUUID().toString();
	
	public static void main(String[] argv)
			throws Exception {

		// We begin by building the basic properties for a connection.
		BasicProperties props = new BasicProperties
				.Builder()
				.correlationId(corrId)
				.replyTo(QUEUE_NAME_REPLY)
				.build();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channelOut = connection.createChannel(),
				channelIn = connection.createChannel();

		// Message sending
		channelOut.queueDeclare(QUEUE_NAME_REQUEST, false, false, false, null);
		channelIn.queueDeclare(QUEUE_NAME_REPLY, false, false, false, null);
		String message = "{\"user\":\"test\", \"lesson\":\"lessons.welcome\", \"exercise\":\"forLoop\", \"language\":\"Java\", \"localization\":\""+Locale.FRENCH.toLanguageTag()+"\", \"code\":\"avance();avance();\"}";
		channelOut.basicPublish("", QUEUE_NAME_REQUEST, null, message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");
		QueueingConsumer consumer = new QueueingConsumer(channelIn);

		while(true) {
			channelIn.basicConsume(QUEUE_NAME_REPLY, true, consumer);
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			message = new String(delivery.getBody(),"UTF-8");
			System.out.println(" [x] Received '" + message + "'");
			JSONParser p = new JSONParser();
			
		}
		
/*
		channelOut.close();
		channelIn.close();
		connection.close();
		*/
	}
}
