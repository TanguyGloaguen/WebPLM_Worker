package generator.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import generator.Generator;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.universe.World;

public class DataParser {

	/**
	 * Generate data from a lesson collection to a file.
	 * @param cLess the lesson collection.
	 */
	public static void generateData(Collection<Lesson> cLess) {
		Iterator<Lesson> ite = cLess.iterator();
		try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(Generator.dataPath + "lessonList.json"));
	        bw.write("[");
	        try {
		        bw.write(lessonData((Lesson) ite.next()));
		        while(true) {
		        	Lesson s = (Lesson) ite.next();
		        	bw.write(",");
		        	bw.write(lessonData(s));
		        }
	        }
	        catch(NoSuchElementException e) {
	        	
	        }
	        finally {
		        bw.write("]");
		        bw.close();
	        }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Generates data for a single lesson.<br />
	 * Use {@link generateData(Collection<Lesson> cLess)} instead.
	 * @param next The lesson to convert
	 * @return A String with data in it.
	 */
	@SuppressWarnings("unchecked")
	private static String lessonData(Lesson next) {
		JSONObject res = new JSONObject();
		res.put("id", next.getId());
		res.put("imgUrl", "assets/images/lessonIcons/" + next.getId() + "-icon.png");
		return res.toJSONString();
	}

	/**
	 * Generate data from a lessonID and its exercises to a file.
	 * @param lessID The lesson ID.
	 * @param cExo The exercise collection.
	 */
	public static void generateData(String lessID, Collection<Lecture> cExo) {
		Iterator<Lecture> ite = cExo.iterator();
		try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(Generator.dataPath + lessID + "-list.json"));
	        bw.write("[");
	        try {
		        bw.write(lectureData(lessID, (Lecture) ite.next()));
		        while(true) {
		        	Lecture s = (Lecture) ite.next();
		        	bw.write(",");
		        	bw.write(lectureData(lessID, s));
		        }
	        }
	        catch(NoSuchElementException e) {
	        }
	        finally {
		        bw.write("]");
		        bw.close();
	        }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Implementation hider. Takes a root lecture and lesson ID and returns a JSONArray containing all exercise data about it.<br />
	 * Use {@link generateData(String lessID, Collection<Lecture> cExo)} instead
	 * @param lessID The root lesson ID
	 * @param lecture the root lecture.
	 * @return A JSONArray with everything in it.
	 */
	private static String lectureData(String lessID, Lecture lecture) {
		JSONArray a = new JSONArray();
		lectureDataRec(lessID, lecture, a);
		return a.toJSONString();
	}
	
	/**
	 * Recursive function to go through all lectures from a lesson.<br />
	 * Use {@link generateData(String lessID, Collection<Lecture> cExo)} instead
	 * @param originName The ID of the parent
	 * @param lecture The current lecture
	 * @param a The parent {@link JSONArray} to put data into.
	 */
	@SuppressWarnings("unchecked")
	private static void lectureDataRec(String originName, Lecture lecture, JSONArray a) {
		JSONObject res = new JSONObject();
		res.put("id", lecture.getId());
		res.put("parent", originName);
		res.put("api", ((Exercise) lecture).getWorlds(WorldKind.INITIAL).firstElement().getClass().getCanonicalName());
		res.put("toolbox", lecture.getToolbox());
		a.add(res);
		for(Lecture l : lecture.getDependingLectures()) {
			lectureDataRec(lecture.getId(), l, a);
		}
	}

	/**
	 * Generate data for a world list snapshot, linked to an execise, to a file.
	 * @param exo The name of the exercise
	 * @param worlds A world list to generate data from.
	 */
	@SuppressWarnings("unchecked")
	public static void generateData(String exo, List<World> worlds) {
		JSONObject r = new JSONObject();
		for(World w : worlds) {
			JSONObject wj = WorldParser.toJSON(w);
			if(wj != null)
				r.putAll(wj);
		}
		try {
	        BufferedWriter bw = new BufferedWriter(new FileWriter(Generator.worldPath + exo + ".json"));
	        bw.write(r.toJSONString());
	        bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
