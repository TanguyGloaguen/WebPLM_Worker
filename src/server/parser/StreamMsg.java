package server.parser;

import java.util.List;

import org.json.simple.JSONObject;

import plm.universe.Operation;
import plm.universe.World;

public class StreamMsg {

	String result = "";
	public StreamMsg(World currWorld, List<Operation> operations) {
		JSONObject obj = new JSONObject();
		obj.put("world", currWorld.getName());
/*		for(Operation op : operations) {
			switch(buggleOperation) {
		      case MoveBuggleOperation =>
		        json = moveBuggleOperationWrite(moveBuggleOperation)
		      case ChangeBuggleDirection =>
		        json = changeBuggleDirectionWrite(changeBuggleDirection)
		      case ChangeBuggleCarryBaggle =>
		        json = changeBuggleCarryBaggleWrite(changeBuggleCarryBaggle)
		      case ChangeBuggleBrushDown =>
		        json = changeBuggleBrushDownWrite(changeBuggleBrushDown)  
		      case _ =>
		        json = Json.obj()
		    }
		} */
	}

	public String toJSON() {
		// TODO Auto-generated method stub
		return "Stream";
	}

}
