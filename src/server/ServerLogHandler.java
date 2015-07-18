package server;

import plm.core.model.LogHandler;
/**
 * This is a dummy {@link LogHandler}, intended to not process any output from the PLM {@link plm.core.model.Game Game} engine.
 * @see LogHandler
 */
public class ServerLogHandler extends LogHandler {

	@Override
	public void log(int type, String message) {
		// TODO write log handler
	}

	@Override
	public void send(int type, String message) {
		// TODO write log handler
	}

}
