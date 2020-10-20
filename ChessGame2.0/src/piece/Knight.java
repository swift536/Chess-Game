package piece;

import chessBoard.Position;
import piece.Piece.PieceType;
import piece.Piece.Team;

public class Knight extends Piece {

	private boolean pieceMoved;
	
	public Knight(PieceType pieceType, Team team) {
		super(pieceType, team);
		pieceMoved = false;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkMove(Position newPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkMove(int newX, int newY, Piece[][] board) {
		boolean moveable = false;
		int oldX = this.position.getX();
		int oldY = this.position.getY();
		int smallX, largeX;
		int smallY, largeY;
		
		//Check for L-shaped move
		if ((Math.abs(oldX-newX)==1 && Math.abs(oldY-newY)==2) || (Math.abs(oldX-newX)==2 && Math.abs(oldY-newY)==1)) {
			moveable = true;
		}
		return moveable;
	}
	
	public void setPieceMoved() {
		pieceMoved = true;
	}
	
	public boolean getPieceMoved () {
		return pieceMoved;
	}

}