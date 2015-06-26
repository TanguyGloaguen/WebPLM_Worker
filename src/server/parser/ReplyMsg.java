package server.parser;

import org.json.simple.JSONObject;

public class ReplyMsg {

	String result;
	
	public ReplyMsg(int type, String msg) {
		JSONObject res = new JSONObject();
		res.put("msgType", type);
		res.put("msg", msg);
		result = res.toJSONString();
	}

	public String toJSON() {
		// TODO Auto-generated method stub
		return result;
	}

}
