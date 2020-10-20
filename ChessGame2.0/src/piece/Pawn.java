package piece;

import chessBoard.Position;

public class Pawn extends Piece {

	public Pawn(PieceType pieceType, Team team) {
		super(pieceType, team);
	}

	@Override
	public boolean checkMove(Position newPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkMove(int newX, int newY, Piece board[][]) {
		boolean moveable = false;
		int oldX = this.position.getX();
		int oldY = this.position.getY();
		
		if (this.team == Team.White) {
			if ((newX == oldX) && (newY == (oldY+1))) {
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
			if ((newX == oldX) && (newY == (oldY-1))) {
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
