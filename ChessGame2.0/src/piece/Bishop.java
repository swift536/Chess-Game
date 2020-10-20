package piece;

import chessBoard.Position;
import piece.Piece.PieceType;
import piece.Piece.Team;

public class Bishop extends Piece {

	public Bishop(PieceType pieceType, Team team) {
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
		} else {
			smallX = oldX;
		}
		if (newY < oldY) {
			smallY = newY;
		} else {
			smallY = oldY;
		}
		
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
		} else {
			moveable = false;
		}
		return moveable;
	}

}