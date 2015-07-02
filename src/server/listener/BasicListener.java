package server.listener;

import java.io.IOException;
import java.util.List;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import plm.universe.Entity;
import plm.universe.IWorldView;
import plm.universe.World;
import server.parser.StreamMsg;

public class BasicListener implements IWorldView {

	private World currWorld = null;
	Channel channel;
	String sendTo;
	BasicProperties properties;
	
	public BasicListener(Channel c, String s) {
		channel = c;
		sendTo = s;
	}
	
	public void setProps(BasicProperties p) {
		properties = p;
	}
	
	public void setWorld(World w) {
		if(currWorld != null)
			currWorld.removeWorldUpdatesListener(this);
		currWorld = w;
		currWorld.addWorldUpdatesListener(this);
	}
	
	@Override
	public BasicListener clone() {
		BasicListener res = new BasicListener(channel, sendTo);
		res.setWorld(currWorld);
		return res;
	}
	
	@Override
	public void worldHasMoved() {
		List<Entity> l = currWorld.getEntities();
		for(Entity element : l) {
			if(element.isReadyToSend()) {
				StreamMsg streamMsg = new StreamMsg(currWorld, element.getOperations());
				String message = streamMsg.toJSON();
				element.getOperations().clear();
				element.setReadyToSend(false);
				send(message);
			}
		}
	}

	@Override
	public void worldHasChanged() {
		// TODO explain why it's empty.
	}

	private void send(String message) {
		try {
			channel.basicPublish("", sendTo, properties, message.getBytes("UTF-8"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println(" [D] Sent stream message (" + properties.getCorrelationId() + ")");
	}
}
