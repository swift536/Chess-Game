package piece;

import chessBoard.Position;
import piece.Piece.PieceType;
import piece.Piece.Team;

public class Queen extends Piece {

	public Queen(PieceType pieceType, Team team) {
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
		int smallX, largeX;
		int smallY, largeY;
		
		if (newX < oldX) {
			smallX = newX;
			largeX = oldX;
		} else {
			smallX = oldX;
			largeX = newX;
		}
		if (newY < oldY) {
			smallY = newY;
			largeY = oldY;
		} else {
			smallY = oldY;
			largeY = newY;
		}
		
		//Check diagonal
		if (Math.abs(newX - oldX) == Math.abs(newY - oldY)) {
			//Diagonal to bottom right
			if (newX > oldX && newY>oldY) {
				for (int i=1; i<Math.abs(newX-oldX); i++) {
					if (board[oldX+i][oldY+i] instanceof Piece && !(board[oldX+i][oldY+i] instanceof NullPiece)) {
						moveable = false;
					}
				}
			//Diagonal to bottom left
			} else if (newX < oldX && newY>oldY) {
				for (int i=1; i<Math.abs(newX-oldX); i++) {
					if (board[oldX-i][oldY+i] instanceof Piece && !(board[oldX-i][oldY+i] instanceof NullPiece)) {
						moveable = false;
					}
				}
			//Diagonal to top left
			} else if (newX < oldX && newY<oldY) {
				for (int i=1; i<Math.abs(newX-oldX); i++) {
					if (board[oldX-i][oldY-i] instanceof Piece && !(board[oldX-i][oldY-i] instanceof NullPiece)) {
						moveable = false;
					}
				}
			//Diagonal to top right
			} else {
				for (int i=1; i<Math.abs(newX-oldX); i++) {
					if (board[oldX+i][oldY-i] instanceof Piece && !(board[oldX+i][oldY-i] instanceof NullPiece)) {
						moveable = false;
					}
				}
			}
		//Check vertical
		} else if (newX == oldX) {
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
		//Check horizontal
		} else if (newY == oldY) {
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

}
