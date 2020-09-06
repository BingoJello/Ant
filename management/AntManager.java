package management;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import log.LoggerUtility;
import process.Ant;
import process.ElementFactory;
import process.Map;

/**
 * This class helps the core in the management of Ant type elements.
 * 
 * @author Edson De Carvalho 
 */
public class AntManager {	
	
	private ArrayList<Ant> ants;
	private static Logger logger = LoggerUtility.getLogger(AntManager.class, "console");
	
	public AntManager() {
		this.ants= new	ArrayList<Ant>();
	}
	
	public void  liveManager(Map map) throws IOException, URISyntaxException {
		ants=map.getAnts();
		
		for(int i=0;i<ants.size();i++){
			ants.get(i).lessLive();
		}
			
		for(int i=0;i<ants.size();i++) {
			if(ants.get(i).getHealth()<=0) {
				map.removeDeadAnt(i,ants.get(i));
			}	
		}	
	}
	
	public void mouvementManager(Map map) {
		int xmax=map.getXmax();
		int ymax=map.getYmax();
		
		for(int i=0;i<ants.size();i++) {
			map=ants.get(i).move(xmax, ymax, map);
			ants.get(i).getMemory().incrementeNumberOfSteps();
			map.getAnts().set(i, ants.get(i));
		}
	}
	
	public void moreAnts(Map map) throws IOException, URISyntaxException {
		if(map.getMapCharacteristics().getTime()%30==0 && map.getMapCharacteristics().getNbrAntsInTheMap()<map.getMapCharacteristics().getNbrMaxAnts()) {
			map.addAnt();
			map.getMapCharacteristics().incrementationNbrAntsInTheMap();
			logger.info("Adding an ant to the Map");
		}
	}
}