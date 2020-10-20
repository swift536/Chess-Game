package chessBoard;

public class Position {
	private int positionX;
	private int positionY;
	
	public Position (int x, int y) {
		positionX = x;
		positionY = y;
	}
	
	public Position (int[] coordinates) {
		if (coordinates.length != 2) {
			//throw error
			return;
		} else {
			positionX = coordinates[0];
			positionY = coordinates[1];
		}
		
	}
	
	public Position (Position obj) {
		int[] coordinates = obj.getPosition();
		positionX = coordinates[0];
		positionY = coordinates[1];
	}
	
	public int[] getPosition () {
		return new int[]{positionX, positionY};
	}
	
	public int getX () {
		return positionX;
	}
	
	public int getY () {
		return positionY;
	}
	
	public void setPosition (int x, int y) {
		positionX = x;
		positionY = y;
	}
	
	public void setPosition (Position obj) {
		int[] coordinates = obj.getPosition();
		positionX = coordinates[0];
		positionY = coordinates[1];
	}
	
	public boolean isEqual (Position newPosition) {
		int[] coordinates = newPosition.getPosition();
		if (coordinates[0]==positionX && coordinates[1]==positionY) {
			return true;
		} else {
			return false;
		}
	}
}
