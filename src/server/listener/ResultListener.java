package server.listener;

import java.io.IOException;

import org.xnap.commons.i18n.I18n;

import server.Main;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import plm.core.GameStateListener;
import plm.core.model.Game;
import plm.core.model.Game.GameState;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import server.parser.ReplyMsg;

/**
 * The {@link GameStateListener} implementation. It reacts when the game state changes, notifying the {@link Main} class and the {@link BasicListener}.
 * @author Tanguy
 *
 */
public class ResultListener implements GameStateListener {

	private Game currGame;
	Channel channel;
	String sendTo;
	BasicProperties properties;
	BasicListener listener;
	
	/**
	 * The {@link ResultListener} constructor.
	 * @param channel Channel the basicListener shoud push to.
	 * @param sendTo The channel name. It should be the same that the one used while creating channel
	 * @param lstn The basic listener to activate for stream end.
	 */
	public ResultListener(Channel channel, String sendTo, BasicListener lstn) {
		this.channel = channel;
		this.sendTo = sendTo;
		this.listener = lstn;
	}
	
	public void setProps(BasicProperties p) {
		properties = p;
	}
	
	public void setGame(Game g) {
		if(currGame != null)
			currGame.removeGameStateListener(this);
		currGame = g;
		g.addGameStateListener(this);
	}
	
	@Override
	public ResultListener clone() {
		ResultListener res = new ResultListener(channel, sendTo, listener);
		res.setGame(currGame);
		return res;
	}

	@Override
	public void stateChanged(GameState state) {
		switch(state) {
			case DEMO_ENDED :
			case EXECUTION_ENDED :
				Exercise e = (Exercise) currGame.getCurrentLesson().getCurrentExercise();
				listener.send();
				send(e.lastResult, currGame.i18n);
				Main.freeMain();
				break;
			default:
				break;
		}
	}
	
	public void send(ExecutionProgress exPro, I18n i18n) {
		ReplyMsg replyMsg = new ReplyMsg(exPro, i18n);
		String message = replyMsg.toJSON();
		try {
			channel.basicPublish("", sendTo, properties, message.getBytes("UTF-8"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println(" [D] Sent end comm. message (" + properties.getCorrelationId() + ")");
	}
}
