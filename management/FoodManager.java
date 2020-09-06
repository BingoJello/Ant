package management;
import process.Map;

/**
 * This class helps the core in the management of Food elements.
 * 
 * @author Edson De Carvalho 
 */
public class FoodManager {
	
	public FoodManager() {

	}
	
	public void moreFoods(Map map) {
		int frequency=map.getMapCharacteristics().getFrequencyFood();
		if(map.getMapCharacteristics().getTime()%frequency==0 && map.getMapCharacteristics().getNbrTotalFood()<map.getMapCharacteristics().getNbrMAXTotalFood()) {
			map.addFood();
		}
	}
}
