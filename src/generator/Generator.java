package generator;

import java.io.File;
import java.util.Collection;
import java.util.Locale;
import org.apache.commons.cli.*;
import com.rabbitmq.client.AMQP.BasicProperties;
import server.Main;

public class Generator {
	private GameGest gest = null;
	private ConnectorDummy connector = new ConnectorDummy();
	private static int testID = 1;
	public static String demoPath = "lessonDemos";
	public static String worldPath = "lessonWorlds";
	public static String dataPath = "webPLMData";
	
	public Generator init() {
		connector.init("kappa", 1000);
		gest = new GameGest(connector, Main.logger);
		BasicProperties replyProps = new BasicProperties
                .Builder()
                .correlationId("TestCorrID")
                .build();
		gest.setProperties(replyProps);
		return this;
	}
	
	public int run() {
		Collection<String> cLess = gest.getLessons();
		for(String lessID : cLess) {
			Collection<String> cExo = gest.getExercises(lessID);
			for(String exID : cExo) {
				init();
				logInfos("Generation "+ (testID++), lessID, exID);
				gest.setGameState(Locale.FRENCH, "Java", lessID, exID);
				gest.startGame();
			}
		}
		return 1;
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		CommandLine commandLine;
        Option option_wp = OptionBuilder.withArgName("wp").hasArg().withDescription("The world path").create("w");
        Option option_op = OptionBuilder.withArgName("op").hasArg().withDescription("The demo operation path").create("o");
        Option option_dp = OptionBuilder.withArgName("dp").hasArg().withDescription("The data path").create("d");
        Options options = new Options().addOption(option_wp).addOption(option_op).addOption(option_dp);
		Main.logger.log(-1, "Starting generator.");
		Generator g = new Generator().init();
        try {
        	commandLine = (new GnuParser()).parse(options, args);
        	if(commandLine.hasOption("w"))
        		worldPath = commandLine.getOptionValue("w");
        	if(commandLine.hasOption("o"))
        		demoPath = commandLine.getOptionValue("o");
        	if(commandLine.hasOption("d"))
        		dataPath = commandLine.getOptionValue("d");
        }
        catch(ParseException e) {
        	e.printStackTrace();
        }
		checkFolders();
        g.run();
	}
	
	public static void checkFolders() {
		File worldFolder = new File(worldPath);
		File demoFolder = new File(demoPath);
		File dataFolder = new File(dataPath);
		try {
			if(!worldFolder.exists() || !worldFolder.isDirectory())
				worldFolder.mkdir();
			if(!demoFolder.exists() || !demoFolder.isDirectory())
				demoFolder.mkdir();
			if(!dataFolder.exists() || !dataFolder.isDirectory())
				dataFolder.mkdir();
		}
		catch(SecurityException e) {
			e.printStackTrace();
		}
		if(!worldPath.endsWith("//"))
			worldPath += "//";
		if(!demoPath.endsWith("//"))
			demoPath += "//";
		if(!dataPath.endsWith("//"))
			dataPath += "//";
		Main.logger.log(-1,"Checked (" + demoPath + ")");
	}
	
	public static void logInfos(String testName, String lessID, String exID) {
		Main.logger.log(-1, "----------------------------");
		Main.logger.log(-1, testName);
		Main.logger.log(-1, "lesson\t: " + lessID);
		Main.logger.log(-1, "exercise\t: " + exID);
		Main.logger.log(-1, "----------------------------");
	}
	
// All about that file generation.
	
}
