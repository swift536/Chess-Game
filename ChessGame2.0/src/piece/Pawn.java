package piece;

import chessBoard.Position;

public class Pawn extends Piece {

	boolean pieceMoved;
	
	public Pawn(PieceType pieceType, Team team) {
		super(pieceType, team);
		pieceMoved = false;
	}

	@Override
	public boolean checkMove(Position newPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setPieceMoved () {
		pieceMoved = true;
	}

	@Override
	public boolean checkMove(int newX, int newY, Piece board[][]) {
		boolean moveable = false;
		int oldX = this.position.getX();
		int oldY = this.position.getY();
		
		
		if (this.team == Team.White) {
			if (((newX == oldX) && (newY == (oldY+1))) 
					|| (newX == oldX && ((newY == (oldY+2) && !pieceMoved)))) {
				moveable =  true;
				if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)) {
					moveable = false;
				}
			} else if (((newX == oldX-1) && (newY == (oldY+1))) || ((newX == oldX+1) && (newY == (oldY+1)))) {
				if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)) {
					moveable = true;
				}
			}
		} else if (this.team == Team.Black) {
			if ((newX == oldX) && (newY == (oldY-1))
					|| (newX == oldX && ((newY == (oldY-2) && !pieceMoved)))) {
				moveable =  true;
				if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)) {
					moveable = false;
				}
			} else if (((newX == oldX-1) && (newY == (oldY-1))) || ((newX == oldX+1) && (newY == (oldY-1)))) {
				if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)) {
					moveable = true;
				}
			}
		}
		
		return moveable;
	}

}
