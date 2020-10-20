package piece;

import chessBoard.Position;

public class Rook extends Piece {

	boolean pieceMoved;
	
	public Rook(PieceType pieceType, Team team) {
		super(pieceType, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkMove(Position newPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkMove(int newX, int newY, Piece[][] board) {
		boolean moveable = true;
		int oldX = this.position.getX();
		int oldY = this.position.getY();
		int largeNum;
		int smallNum;
		
		//If move is vertical
		if (newX == oldX) {
			//Up
			if (oldY < newY) {
				
				for (int i=1; i<Math.abs(oldY-newY);i++) {
					if (board[newX][oldY+i] instanceof Piece && !(board[newX][oldY+i] instanceof NullPiece)) {
						moveable = false;
					}
				}
			//Down
			} else {
				
				for (int i=1; i<Math.abs(oldY-newY);i++) {
					if (board[newX][oldY-i] instanceof Piece && !(board[newX][oldY-i] instanceof NullPiece)) {
						moveable = false;
					}
				}
			}
		//If move is horizontal
		} else if ( newY == oldY) {
			//Left
			if (oldX > newX) {
				
				for (int i=1; i<Math.abs(oldX-newX);i++) {
					if (board[oldX-i][newY] instanceof Piece && !(board[oldX-i][newY] instanceof NullPiece)) {
						moveable = false;
					}
				}
			//Right
			} else {
				
				for (int i=1; i<Math.abs(oldX-newX);i++) {
					if (board[oldX+i][newY] instanceof Piece && !(board[oldX+i][newY] instanceof NullPiece)) {
						moveable = false;
					}
				}
			}
		} else {
			moveable = false;
		}
		
		return moveable;
	}
	
	public void setPieceMoved(boolean a) {
		pieceMoved = a;
	}
	
	public boolean getPieceMoved () {
		return pieceMoved;
	}

}
