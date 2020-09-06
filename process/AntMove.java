package process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

import data.Cell;
import data.Element;
import data.Position;
import log.LoggerUtility;

/**
 * This class is used to move an ant.
 * 
 * @author Arthur Mimouni
 *
 */
public class AntMove {
	
	private ArrayList<Position> homepath=new ArrayList<Position>();
	private boolean iswaiting=false;
	private Map map;
	private Cell[][] grid;
	private boolean stay=false;
	private Ant ant;
	private ArrayList<Element>foods;
	private static Logger logger = LoggerUtility.getLogger(Ant.class, "html");
	
	public AntMove (Ant ant) {
		this.ant=ant;
	}
	
	
	/**
	 * 
	 * @param
	 * @return void 
	 */
	
	public void move (Map map,int xmax, int ymax){
		this.map=map;
		this.grid = map.getGrid();
		this.foods = map.getFoods();
		int x;
		int y;
		Position positionPhero=null;
		
		if(homepath.isEmpty()==false) { 
			backToHome();
			map.setGrid(grid);
			return;
		}
				
		ArrayList<Position> positions= InsectVision.visionOfAnt(ant,grid,xmax,ymax);
		
		if(!positions.isEmpty()) {
			Iterator<Position> it =positions.iterator();
			int xphero=-1;
			int yphero=-1;
			Boolean pheromones = false;
		
			while(it.hasNext()) {
				Position p = it.next();
				x=p.getX();
				y=p.getY();
					
				if(grid[x][y].getTypeElement()==6) {					
					goToBigFood(x,y);
					map.setGrid(grid);
					return;
				}
					
				else if(grid[x][y].getTypeElement()==5) {
					goToSmallFood(x,y);
					map.setGrid(grid);
					return;
				}
				
				else if(grid[x][y].getQuantitePheromones()>0) {
					xphero=x;
					yphero=y;
					pheromones=true;
					positionPhero=p;
				}
			}
					
			if(pheromones==true) {	
				it=positions.iterator();
			
				while(it.hasNext()) {
					Position p = it.next();
					x=p.getX();
					y=p.getY();
					if(grid[x][y].getQuantitePheromones()!=0) {
						if(grid[x][y].getQuantitePheromones()< grid[xphero][yphero].getQuantitePheromones()) {
							xphero=x;
							yphero=y;
							positionPhero=p;
						}
					}
				}
				
				if(grid[ant.getPosition().getX()][ant.getPosition().getY()].getTypeElement()!=1) {
					grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(0);
					grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(null);
				}
				
				ant.getPosition().setX(xphero);
				ant.getPosition().setY(yphero);
				ant.setDirection(positionPhero.getDirection());
	
				grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(2);
				grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(ant);
				map.setGrid(grid);
				
				return;			
			}
			randomMove(positions);
		}
	}	
	
	/**
	 * This method returns the shortest way from the ant cell to the nest cell. This class is inspired by the algorithm of width course(parcours en largeur).
	 * 
	 * @return void 
	 */
	private void backToHome() {
		
		if(!homepath.isEmpty()) {
			int x1 = ant.getPosition().getX();
			int y1=ant.getPosition().getY();
			
			if(grid[x1][y1].getTypeElement()!=5 && grid[x1][y1].getTypeElement()!=6) {	
				grid[x1][y1].setTypeElement(0);	
				grid[x1][y1].setElement(null);
				grid[x1][y1].setQuantitePheromones("max");
			}
			
			ant.getPosition().setX(homepath.get(0).getX());
			ant.getPosition().setY(homepath.get(0).getY());
			
			homepath.remove(0);	
		}
	}
		
	/**
	 * 
	 * @param
	 * @return void 
	 */
	private void goToBigFood(int x, int y) {
		
		if(grid[x][y].isOccupied()==true && iswaiting==false) {
			grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(0);
			grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(null);
			
			ant.getPosition().setX(x);
			ant.getPosition().setY(y);
			
			Element food = grid[x][y].getElement();
			food.setQuantity(-1);
	
			grid[x][y].setTypeElement(0);
	
			BackHome backhome = new BackHome();
			homepath =backhome.solve(map, ant);
			
			if(homepath==null) {
				
			}
			
			else {
				Collections.reverse(homepath);
			}
			
			if(food.getQuantity()==0) {
				foods.remove(grid[x][y].getElement());
				grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(ant);
				grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(2);
			}
			
			else {
				grid[x][y].setTypeElement(food.getType());
			}
			
			grid[x][y].setHomepath(homepath);
			grid[x][y].setOccupied(false);
			map.setGrid(grid);
			ant.getMemory().incrementeCapturedBigFood();
			map.getMapCharacteristics().incrementationNbrCapturedFood();
			
			return;
		}
		
		else {
			
			if(stay==false) {
				grid[x][y].setOccupied(true);
				stay=true;
			}
			
			if(grid[x][y].isOccupied()==false) {
				iswaiting=false;
				stay=false;
				grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(0);
				grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(null);
				BackHome backhome = new BackHome();
				homepath =backhome.solve(map, ant);
			
				if(homepath!=null) {
					Collections.reverse(homepath);
				}
				
				map.setGrid(grid);
				return;
			}
			
			iswaiting=true;
			map.setGrid(grid);
			return;	
		}
	}
	
	/**
	 * 
	 * @param
	 * @return void 
	 */
	private void goToSmallFood(int x,int y) {
		
		grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(0);
		grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(null);
		
		ant.getPosition().setX(x);
		ant.getPosition().setY(y);
		
		Element food = grid[x][y].getElement();
		food.setQuantity(-1);
		logger.info("An ant has captured food");
		
		ant.getMemory().incrementeCapturedSmallFood();
		map.getMapCharacteristics().incrementationNbrCapturedFood();
		grid[x][y].setTypeElement(0);

		BackHome backhome = new BackHome();
		homepath =backhome.solve(map, ant);
		
		if(homepath!=null) {
			Collections.reverse(homepath);
		}
		
		else {
			
		}
		
		if(food.getQuantity()==0) {
			foods.remove(grid[x][y].getElement());
			grid[x][y].setElement(null);
			grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(2);
			grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(ant);
		}
		
		else {
			grid[x][y].setTypeElement(food.getType());
		}
	}
	
	/**
	 * 
	 * @param
	 * @return void 
	 */
	private void randomMove(ArrayList<Position> positions) {
		
		Random r = new Random();
		int valeur = 0 + r.nextInt(positions.size() - 0);
		int x=positions.get(valeur).getX();
		int y=positions.get(valeur).getY();
	
		if(grid[ant.getPosition().getX()][ant.getPosition().getY()].getTypeElement()!=1) {
			grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(0);
			grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(null);
		}
	
		ant.getPosition().setX(x);
		ant.getPosition().setY(y);
		ant.setDirection(positions.get(valeur).getDirection());
		
		grid[ant.getPosition().getX()][ant.getPosition().getY()].setTypeElement(2);	
		grid[ant.getPosition().getX()][ant.getPosition().getY()].setElement(ant);
	}
	
	/**
	 * 
	 * @param
	 * @return void 
	 */
	
	public int howManyPositionsToHome(ArrayList<Position> homepath) {
		Iterator<Position> it = homepath.iterator();
		int number=0;
		while (it.hasNext()){ 
			number++;
		}
		return number;
	}

	public Map getMap() {
		return map;
	}
}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
		

