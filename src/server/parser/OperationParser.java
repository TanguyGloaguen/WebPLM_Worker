package server.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.universe.GridWorldCellOperation;
import plm.universe.Operation;
import plm.universe.bugglequest.*;

@SuppressWarnings("unchecked")
public class OperationParser {
// Operation routing
	public static JSONObject toJSON(Operation o) {
		JSONObject r;
		if(o instanceof BuggleOperation)
			r = toJSON((BuggleOperation) o);
		else if(o instanceof GridWorldCellOperation)
			r = toJSON((GridWorldCellOperation) o);
		else r = new JSONObject();
		r.put("type", o.getName());
		r.put("msg", o.getMsg());
		return r;
	}
// GridWorldCell routing
	private static JSONObject toJSON(GridWorldCellOperation o) {
		JSONObject r;
		if(o instanceof BuggleWorldCellOperation)
			r = toJSON((BuggleWorldCellOperation) o);
		else r = new JSONObject();
		JSONObject cell = new JSONObject();
		cell.put("x", o.getCell().getX());
		cell.put("y", o.getCell().getY());
		r.put("cell", cell);
		return r;
	}
// BuggleWorldCell operations
	private static JSONObject toJSON(BuggleWorldCellOperation o) {
		JSONObject r;
		if(o instanceof ChangeCellColor)
			r = toJSON((ChangeCellColor) o);
		else if(o instanceof ChangeCellHasBaggle)
			r = toJSON((ChangeCellHasBaggle) o);
		else if(o instanceof ChangeCellHasContent)
			r = toJSON((ChangeCellHasContent) o);
		else if(o instanceof ChangeCellContent)
			r = toJSON((ChangeCellContent) o);
		else
			r = new JSONObject();
		return r;
	}
	private static JSONObject toJSON(ChangeCellColor o) {
		JSONObject res = new JSONObject();
		JSONArray oldCol = new JSONArray();
			oldCol.add(o.getOldColor().getRed());
			oldCol.add(o.getOldColor().getGreen());
			oldCol.add(o.getOldColor().getBlue());
			oldCol.add(o.getOldColor().getAlpha());
		JSONArray newCol = new JSONArray();
			newCol.add(o.getNewColor().getRed());
			newCol.add(o.getNewColor().getGreen());
			newCol.add(o.getNewColor().getBlue());
			newCol.add(o.getNewColor().getAlpha());
		res.put("oldColor", oldCol);
		res.put("newColor", newCol);
		res.put("operation", "ChangeCellColor");
		return res;
	}
	private static JSONObject toJSON(ChangeCellHasBaggle o) {
		JSONObject res = new JSONObject();
		res.put("oldHasBaggle", o.getOldHasBaggle());
		res.put("newHasBaggle", o.getNewHasBaggle());
		res.put("operation", "ChangeCellHasBaggle");
		return res;
	}
	private static JSONObject toJSON(ChangeCellHasContent o) {
		JSONObject res = new JSONObject();
		res.put("oldHasContent", o.getOldHasContent());
		res.put("newHasContent", o.getNewHasContent());
		res.put("operation", "ChangeCellHasContent");
		return res;
	}
	private static JSONObject toJSON(ChangeCellContent o) {
		JSONObject res = new JSONObject();
		res.put("oldContent", o.getOldContent());
		res.put("newContent", o.getNewContent());
		res.put("operation", "ChangeCellContent");
		return res;
	}
	
// Buggle operations
	private static JSONObject toJSON(BuggleOperation o) {
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
	private static JSONObject toJSON(MoveBuggleOperation o) {
		JSONObject res = new JSONObject();
		res.put("oldX", o.getOldX());
		res.put("oldY", o.getOldY());
		res.put("newX", o.getNewX());
		res.put("newY", o.getNewY());
		res.put("operation", "MoveBuggleOperation");
		return res;
	}
	private static JSONObject toJSON(ChangeBuggleCarryBaggle o) {
		JSONObject res = new JSONObject();
		res.put("oldCarryBaggle", o.getOldCarryBaggle());
		res.put("newCarryBaggle", o.getNewCarryBaggle());
		res.put("operation", "ChangeBuggleCarryBaggle");
		return res;
	}
	private static JSONObject toJSON(ChangeBuggleBrushDown o) {
		JSONObject res = new JSONObject();
		res.put("oldBrushDown", o.getOldBrushDown());
		res.put("newBrushDown", o.getNewBrushDown());
		res.put("operation", "ChangeBuggleBrushDown");
		return res;
	}
	private static JSONObject toJSON(ChangeBuggleDirection o) {
		JSONObject res = new JSONObject();
		res.put("oldDirection", o.getOldDirection().intValue());
		res.put("newDirection", o.getNewDirection().intValue());
		res.put("operation", "ChangeBuggleDirection");
		return res;
	}
}
