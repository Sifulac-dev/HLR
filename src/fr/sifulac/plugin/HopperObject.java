package fr.sifulac.plugin;

public class HopperObject {

	private int locationX;
	private int locationY;
	private int locationZ;
	
	private int chunkX;
	private int chunkZ;
	
	private String world;
	
	public HopperObject() {		
	}
	
	//SETTER
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
		
}
