package server.parser;

import org.json.simple.JSONObject;
import org.xnap.commons.i18n.I18n;

import plm.core.model.lesson.ExecutionProgress;

public class ReplyMsg {

	String result;
	
	@SuppressWarnings("unchecked")
	public ReplyMsg(ExecutionProgress lastResult, I18n i18n) {
		int type = lastResult.outcome == ExecutionProgress.outcomeKind.PASS ? 1 : 0;
		String msg = lastResult.getMsg(i18n);
		JSONObject res = new JSONObject();
		JSONObject gitReply = new JSONObject();
		// Create the GIT replies object.
		gitReply.put("lang", lastResult.language.toString());
		if (lastResult.outcome != null) {
			switch (lastResult.outcome) {
				case COMPILE:  gitReply.put("outcome", "compile");  break;
				case FAIL:     gitReply.put("outcome", "fail");     break;
				case PASS:     gitReply.put("outcome", "pass");     break;
				default:       gitReply.put("outcome", "UNKNOWN");  break;
			}
		}
		
		if (lastResult.totalTests > 0) {
			gitReply.put("passedtests", lastResult.passedTests + "");
			gitReply.put("totaltests", lastResult.totalTests + "");
		}
		
		if (lastResult.feedbackDifficulty != null)
			gitReply.put("exoDifficulty", lastResult.feedbackDifficulty);
		if (lastResult.feedbackInterest != null)
			gitReply.put("exoInterest", lastResult.feedbackInterest);
		if (lastResult.feedback != null)
			gitReply.put("exoComment", lastResult.feedback);
		if (lastResult.compilationError != null)
			gitReply.put("compilError", lastResult.compilationError);
		if (lastResult.executionError != null)
			gitReply.put("execError", lastResult.executionError);
		// create the log data.
		res.put("msgType", type);
		res.put("msg", msg);
		res.put("git_logs", gitReply);
		result = res.toJSONString();
	}
	
	public String toJSON() {
		return result;
	}

}
