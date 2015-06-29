package client;

import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Main {
	private final static String QUEUE_NAME_REQUEST = "worker_in";
	private final static String QUEUE_NAME_REPLY = "worker_out";
	private static String corrId = java.util.UUID.randomUUID().toString();

	public static void main(String[] argv) throws Exception {

		// We begin by building the basic properties for a connection.
		BasicProperties props = new BasicProperties.Builder()
				.correlationId(corrId).replyTo(QUEUE_NAME_REPLY).build();
		System.out.println(corrId);

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channelOut = connection.createChannel(),
				channelIn = connection.createChannel();
		// Message sending
		channelOut.queueDeclare(QUEUE_NAME_REQUEST, false, false, false, null);
		channelIn.queueDeclare(QUEUE_NAME_REPLY, false, false, false, null);
		String message = "{"
				+ "\"user\":\"test\","
				+ "\"lesson\":\"lessons.welcome\","
				+ "\"exercise\":\"loopfor.LoopFor\","
				+ "\"language\":\"Java\","
				+ "\"localization\":\"" + Locale.FRENCH.toLanguageTag() + "\","
				+ "\"code\":\"public void run() { avance(); }\"}";
		channelOut.basicPublish("", QUEUE_NAME_REQUEST, props,
				message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");
		
		
		
		
		// Receive part
		QueueingConsumer consumer = new QueueingConsumer(channelIn);
		channelIn.basicConsume(QUEUE_NAME_REPLY, true, consumer);

		boolean state = true;
		while (state) {
			// retrieve a message.
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			// Is this message ours ? If not, send it back to the queue.
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
				message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				// Is the message an end-of-compil one ? If yes, retrieve data.
				JSONParser p = new JSONParser();
				try {
					JSONObject replyJSON = (JSONObject) p.parse(message);
					String r = replyJSON.get("msgType").toString();
					if(r.equals("0") || r.equals("1")) {
						state = false;
					}
				} catch (ParseException e) {
					// NO OP
				}
			}
		}
		channelOut.close();
		channelIn.close();
		connection.close();
	}
}
