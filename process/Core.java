package process;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import log.LoggerUtility;
import management.*;

/**
 * This class is the core of the program that is used to manage the elements and update the map.
 * 
 * @author Edson De Carvalho 
 *
 */
public class Core{
	
	private PredatorManager predatorsManager;
	private AntManager antManager;
	private FoodManager foodManager;
	private static Logger logger = LoggerUtility.getLogger(ElementFactory.class, "html");
	public Core () {
		this.antManager=new AntManager();
		this.predatorsManager=new PredatorManager();
		this.foodManager=new FoodManager();
	}
	
	/**
	 * This method launches the fundamentals of the program?.
	 * 
	 * @param map
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void process(Map map) throws IOException, URISyntaxException {
		
		mapCharacteristiquesUpdate(map);
		antManager.liveManager(map);
		antManager.moreAnts(map);
		foodManager.moreFoods(map);
		antManager.mouvementManager(map); 
		predatorsManager.liveManager(map);
		predatorsManager.mouvementManager(map);
		
		map.inputInformationCollector();
		if(map.getMapCharacteristics().getTime()%100==0) {
			map.getInformationCollector().process(map.getMapCharacteristics());
			System.out.println(map.getInformationCollector().getInformationCollection());
		}
	}
	
	public void mapCharacteristiquesUpdate(Map map) {
		if(map.getMapCharacteristics().getTime()==0) {
			map.getMapCharacteristics().setNbrAntsInTheMap(map.getMapCharacteristics().getNbrAntsInTheMap());
			map.getMapCharacteristics().setNbrBigFood(map.getMapCharacteristics().getNbrMAXBigFood());
			map.getMapCharacteristics().setNbrSmallFood(map.getMapCharacteristics().getNbrMAXSmallFood());
		}
		map.getMapCharacteristics().incrementationTime();
		map.mapUpdate();
	}
}

