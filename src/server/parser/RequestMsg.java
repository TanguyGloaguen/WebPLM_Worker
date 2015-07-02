package server.parser;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class RequestMsg {

	private String lessonID;
	private String exerciseID;
	private String loc;
	private String lang;
	private String code;
	
	private RequestMsg() {
		// NO OP
	}
	
	public static RequestMsg readMessage(String s) {
		RequestMsg replyData = new RequestMsg();
		JSONParser p = new JSONParser();
		try {
			JSONObject replyJSON = (JSONObject) p.parse(s);
			replyData.lessonID = (String) replyJSON.get("lesson");
			replyData.exerciseID = (String) replyJSON.get("exercise");
			replyData.loc = (String) replyJSON.get("localization");
			replyData.lang = (String) replyJSON.get("language");
			replyData.code = (String) replyJSON.get("code");
		} catch (ParseException e) {
			System.err.println("Parse exception : message in queue didn't fit the expected format.");
			e.printStackTrace();
		}
		return replyData;
	}

	public String getLessonID() {
		return lessonID;
	}

	public String getExerciseID() {
		return exerciseID;
	}

	public String getLocale() {
		return loc;
	}
	
	public String getLanguage() {
		return lang;
	}

	public String getCode() {
		return code;
	}
}
