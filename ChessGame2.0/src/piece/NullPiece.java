package piece;

import chessBoard.Position;
import javafx.scene.layout.StackPane;

public class NullPiece extends Piece {

	public NullPiece(PieceType pieceType, Team team) {
		super(pieceType, team);
	}

	@Override
	public boolean checkMove(Position newPosition) {
		return false;
	}

	@Override
	public boolean checkMove(int newX, int newY, Piece[][] board) {
		return false;
	}

}
