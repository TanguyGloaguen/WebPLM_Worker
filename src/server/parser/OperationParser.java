package server.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.universe.Operation;
import lessons.sort.baseball.operations.*;
import plm.universe.bat.*;
import plm.universe.bugglequest.*;
import lessons.sort.dutchflag.operations.*;
import plm.universe.GridWorldCellOperation;
import lessons.recursion.hanoi.operations.*;
import lessons.sort.pancake.universe.operations.*;
import plm.universe.sort.operations.*;

/**
 * The {@link Operation} to {@link JSONObject} conversion tool.
 * @author Tanguy
 *
 */
@SuppressWarnings("unchecked")
public abstract class OperationParser {
// Entry point
	/**
	 * The {@link OperationParser} entry point.
	 * @param operation {@link Operation} to give to the OperationParser.
	 * @return the result {@link JSONObject}.
	 */
	public static JSONObject toJSON(Operation operation) {
		return Router.toJSON(operation);
	}
// Operation routing
	/**
	 * The {@link Operation} parser.
	 * @author Tanguy
	 *
	 */
	private static class Router {
		public static JSONObject toJSON(Operation o) {
			JSONObject r;
			if(o instanceof BaseballOperation)
				r = Baseball.toJSON((BaseballOperation) o);
			else if(o instanceof BatOperation)
				r = Bat.toJSON((BatOperation) o);
			else if(o instanceof BuggleOperation)
				r = Buggle.toJSON((BuggleOperation) o);
			else if(o instanceof DutchFlagOperation)
				r = DutchFlag.toJSON((DutchFlagOperation) o);
			else if(o instanceof GridWorldCellOperation)
				r = toJSON((GridWorldCellOperation) o);
			else if(o instanceof HanoiOperation)
				r = Hanoi.toJSON((HanoiOperation) o);
			else if(o instanceof PancakeOperation)
				r = Pancake.toJSON((PancakeOperation) o);
			else if(o instanceof SortOperation)
				r = Sort.toJSON((SortOperation) o);
			else r = new JSONObject();
			r.put("type", o.getName());
			r.put("msg", o.getMsg());
			return r;
		}
// Baseball operations
		private static class Baseball {
			private static JSONObject toJSON(BaseballOperation o) {
				JSONObject res;
				if(o instanceof MoveOperation)
					res = toJSON((MoveOperation) o);
				else
					res = new JSONObject();
				res.put("baseballID", o.getEntity().getName());
				return res;
			}
			
			private static JSONObject toJSON(MoveOperation o) {
				JSONObject res = new JSONObject();
				res.put("base", o.getBase());
				res.put("position", o.getPosition());
				res.put("oldBase", o.getOldBase());
				res.put("oldPosition", o.getOldPosition());
				return res;
			}
		}
// Bat operations
		private static class Bat {
			private static JSONObject toJSON(BatOperation o) {
				JSONObject res = new JSONObject();
				JSONArray resArray = new JSONArray();
				for(Object t : o.getBatWorld().getTests().toArray()) {
					JSONObject resElem = new JSONObject();
					resElem.put("test", ((BatTest) t).formatAsString());
					resElem.put("answered", ((BatTest) t).isAnswered());
					resElem.put("correct", ((BatTest) t).isCorrect());
					resElem.put("visible", ((BatTest) t).isVisible());
					resArray.add(resElem);
				}
				res.put("type", "BatWorld");
				res.put("batTests", resArray);
				return res;
			}
		}
// BuggleWorldCell operations
		/**
		 * The {@link BuggleWorldCell} parser.
		 * @author Tanguy
		 *
		 */
		private static class BuggleWorldCell {
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
		}
		
// Buggle operations
		/**
		 * The {@link BuggleOperation} parser.
		 * @author Tanguy
		 *
		 */
		private static class Buggle {
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
// DutchFlag operations
		private static class DutchFlag {
			private static JSONObject toJSON(DutchFlagOperation o) {
				JSONObject res;
				if(o instanceof DutchFlagSwap)
					res = toJSON((DutchFlagSwap) o);
				else
					res = new JSONObject();
				res.put("dutchFlagID", o.getEntity().getName());
				return res;
			}
			
			private static JSONObject toJSON(DutchFlagSwap o) {
				JSONObject res = new JSONObject();
				res.put("destination", o.getDestination());
				res.put("source", o.getSource());
				return res;
			}
		}
// GridWorldCell operation
		private static JSONObject toJSON(GridWorldCellOperation o) {
			JSONObject r;
			if(o instanceof BuggleWorldCellOperation)
				r = BuggleWorldCell.toJSON((BuggleWorldCellOperation) o);
			else r = new JSONObject();
			JSONObject cell = new JSONObject();
			cell.put("x", o.getCell().getX());
			cell.put("y", o.getCell().getY());
			r.put("cell", cell);
			return r;
		}
// Hanoi operation
		private static class Hanoi {
			private static JSONObject toJSON(HanoiOperation o) {
				JSONObject res;
				if(o instanceof HanoiMove)
					res = toJSON((HanoiMove) o);
				else
					res = new JSONObject();
				res.put("hanoiID", o.getEntity().getName());
				return res;
			}
			
			private static JSONObject toJSON(HanoiMove o) {
				JSONObject res = new JSONObject();
				res.put("source", o.getSource());
				res.put("destination", o.getDestination());
				return res;
			}
		}
// Pancake operation
		private static class Pancake {
			private static JSONObject toJSON(PancakeOperation o) {
				JSONObject res;
				if(o instanceof FlipOperation)
					res = toJSON((FlipOperation) o);
				else
					res = new JSONObject();
				res.put("pancakeID", o.getEntity().getName());
				return res;
			}
			private static JSONObject toJSON(FlipOperation o) {
				JSONObject res = new JSONObject();
				res.put("number", o.getNumber());
				return res;
			}
		}
// Sort operations
		private static class Sort {
			private static JSONObject toJSON(SortOperation o) {
				JSONObject res;
				if(o instanceof SetValOperation)
					res = toJSON((SetValOperation) o);
				else if(o instanceof SwapOperation)
					res = toJSON((SwapOperation) o);
				else if(o instanceof CopyOperation)
					res = toJSON((CopyOperation) o);
				else if(o instanceof CountOperation)
					res = toJSON((CountOperation) o);
				else if(o instanceof GetValueOperation)
					res = toJSON((GetValueOperation) o);
				else res = new JSONObject();
				res.put("sortID", o.getEntity().getName());
				return res;
			}
			
			private static JSONObject toJSON(SetValOperation o) {
				JSONObject res = new JSONObject();
				res.put("value", o.getValue());
				res.put("oldValue",  o.getOldValue());
				res.put("position", o.getPosition());
				return res;
			}
			
			private static JSONObject toJSON(SwapOperation o) {
				JSONObject res = new JSONObject();
				res.put("destination", o.getDestination());
				res.put("source", o.getSource());
				return res;
			}
			
			private static JSONObject toJSON(CopyOperation o) {
				JSONObject res = new JSONObject();
				res.put("destination", o.getDestination());
				res.put("source", o.getSource());
				res.put("oldValue", o.getOldValue());
				return res;
			}
			
			private static JSONObject toJSON(CountOperation o) {
				JSONObject res = new JSONObject();
				res.put("read", o.getRead());
				res.put("write", o.getWrite());
				res.put("oldRead", o.getOldRead());
				res.put("oldWrite", o.getOldWrite());
				return res;
			}
			
			private static JSONObject toJSON(GetValueOperation o) {
				JSONObject res = new JSONObject();
				res.put("position", o.getPosition());
				return res;
			}
		}
	}
}
