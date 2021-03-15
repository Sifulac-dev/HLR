package fr.sifulac.plugin.Object;

import fr.sifulac.plugin.Main;

public class HopperObject {

	private String id;
	
	private int locationX;
	private int locationY;
	private int locationZ;
	
	private int chunkX;
	private int chunkZ;
	
	private int numberCactus;
	
	private int n;
	
	private String world;
	
	public HopperObject(int locX, int locY, int locZ, int chunkX, int chunkZ, String world, int number) {		
		this.id  = String.valueOf(locX+locY+locZ);
		this.locationX = locX;
		this.locationY = locY;
		this.locationZ = locZ;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.world = world;
		this.numberCactus = number;
		this.n = number/1000;
	}
	
	//SETTER
	public void setId(String id) {
		this.id = id;
	}	
	
	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}
	
	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
	
	public void setLocationZ(int locationZ) {
		this.locationZ = locationZ;
	}
	
	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}
	
	public void setChunkZ(int chunkZ) {
		this.chunkZ = chunkZ;
	}
	
	public void setWorld(String world) {
		this.world = world;
	}
	
	//GETTER
	public String getId() {
		return id;
	}
	
	public int getLocationX() {
		return locationX;
	}
	
	public int getLocationY() {
		return locationY;
	}
	
	public int getLocationZ() {
		return locationZ;
	}
	
	public int getChunkX() {
		return chunkX;
	}
	
	public int getChunkZ() {
		return chunkZ;
	}
	
	public String getWorld() {
		return world;
	}

	public int getNumberCactus() {
		return numberCactus;
	}

	public void setNumberCactus(int numberCactus) {
		this.numberCactus = numberCactus;
	}
		
	public void addCactus(int number) {
		this.numberCactus += number;
		saveCactus();
	}	
	
	public void removeCactus(int number) {
		this.numberCactus -= number;
		saveCactus();
	}	
	
	private Boolean saveCactus() {
		String number = String.valueOf(this.numberCactus);
		
		if(number.length() >= 4) {
			char c = number.charAt(number.length() - 4);
			if(Integer.parseInt(String.valueOf(c)) != n) {
				Main.getInstance().database.saveHopper(this.locationX, this.locationY, this.locationZ, this.numberCactus);
				this.n = Integer.parseInt(String.valueOf(c));
				return true;
			}
		}
		
		return false;
	}
	
}
