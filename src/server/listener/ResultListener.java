package server.listener;

import java.io.IOException;

import server.Main;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import plm.core.GameStateListener;
import plm.core.model.Game;
import plm.core.model.Game.GameState;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.Exercise;
import server.parser.ReplyMsg;

public class ResultListener implements GameStateListener {

	private Game currGame;
	Channel channel;
	String sendTo;
	BasicProperties properties;
	Main main;
	
	public ResultListener(Channel c, String s, BasicProperties p) {
		channel = c;
		sendTo = s;
		properties = p;
	}
	
	public void setGame(Game g) {
		currGame = g;
		g.addGameStateListener(this);
	}
	
	@Override
	public ResultListener clone() {
		ResultListener res = new ResultListener(channel, sendTo, properties);
		res.setGame(currGame);
		return res;
	}

	@Override
	public void stateChanged(GameState state) {
		switch(state) {
			case DEMO_ENDED :
			case EXECUTION_ENDED :
				Exercise e = (Exercise) currGame.getCurrentLesson().getCurrentExercise();
				int msgType = e.lastResult.outcome == ExecutionProgress.outcomeKind.PASS ? 1 : 0;
				String msg = e.lastResult.getMsg(currGame.i18n);
				send(msgType, msg);
				notifyMain();
				break;
			default:
				break;
		}
	}
	
	public void notifyMain() {
		main.endExercise.release();
	}
	
	public void send(int type, String msg) {
		ReplyMsg replyMsg = new ReplyMsg(type, msg);
		String message = replyMsg.toJSON();
		try {
			channel.basicPublish("", sendTo, properties, message.getBytes("UTF-8"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println(" [D] Sent end comm. message (" + properties.getCorrelationId() + ")");
	}
}
