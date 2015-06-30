package server.parser;

import org.json.simple.JSONObject;

import plm.universe.Operation;
import plm.universe.bugglequest.BuggleOperation;
import plm.universe.bugglequest.ChangeBuggleBrushDown;
import plm.universe.bugglequest.ChangeBuggleCarryBaggle;
import plm.universe.bugglequest.ChangeBuggleDirection;
import plm.universe.bugglequest.MoveBuggleOperation;

@SuppressWarnings("unchecked")
public class OperationParser {
	// Master function
	public static JSONObject toJSON(Operation o) {
		JSONObject r;
		if(o instanceof BuggleOperation)
			r = toJSON((BuggleOperation) o);
		else r = new JSONObject();
		r.put("type", o.getName());
		r.put("msg", o.getMsg());
		return r;
	}
	
	
	
// Buggle operations
	public static JSONObject toJSON(BuggleOperation o) {
		JSONObject r;
		if(o instanceof MoveBuggleOperation)
			r = toJSON((MoveBuggleOperation) o);
		else if(o instanceof ChangeBuggleCarryBaggle)
			r = toJSON((ChangeBuggleCarryBaggle) o);
		else if(o instanceof ChangeBuggleBrushDown)
			r = toJSON((ChangeBuggleBrushDown) o);
		else if(o instanceof ChangeBuggleDirection)
			r = toJSON((ChangeBuggleDirection) o);
		else
			r = new JSONObject();
		r.put("buggleID", o.getBuggle().getName());
		return r;
	}
	
	public static JSONObject toJSON(MoveBuggleOperation o) {
		JSONObject res = new JSONObject();
		res.put("oldX", o.getOldX());
		res.put("oldY", o.getOldY());
		res.put("newX", o.getNewX());
		res.put("newY", o.getNewY());
		res.put("operation", "MoveBuggleOperation");
		return res;
	}
	public static JSONObject toJSON(ChangeBuggleCarryBaggle o) {
		JSONObject res = new JSONObject();
		res.put("oldCarryBaggle", o.getOldCarryBaggle());
		res.put("newCarryBaggle", o.getNewCarryBaggle());
		res.put("operation", "ChangeBuggleCarryBaggle");
		return res;
	}
	public static JSONObject toJSON(ChangeBuggleBrushDown o) {
		JSONObject res = new JSONObject();
		res.put("oldBrushDown", o.getOldBrushDown());
		res.put("newBrushDown", o.getNewBrushDown());
		res.put("operation", "ChangeBuggleBrushDown");
		return res;
	}
	public static JSONObject toJSON(ChangeBuggleDirection o) {
		JSONObject res = new JSONObject();
		res.put("oldDirection", o.getOldDirection());
		res.put("newDirection", o.getNewDirection());
		res.put("operation", "ChangeBuggleDirection");
		return res;
	}
}
