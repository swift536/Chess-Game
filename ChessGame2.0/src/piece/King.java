package piece;

import chessBoard.Position;
import piece.Piece.PieceType;
import piece.Piece.Team;

public class King extends Piece {

	boolean pieceMoved;
	
	public King(PieceType pieceType, Team team) {
		super(pieceType, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkMove(Position newPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCheckMated(Piece[][] board) {
		/*
		 * 1)Brute force method is to run through all possible piece movements in reverse from King position.
		 * 2)Potentially better method is to keep track of available opponent pieces and only check the movements
		 * of the pieces they have left.
		 * 3)Another method would be to check to see if you have a piece to block the current route, or if the king
		 * can move away from check position.
		 * 4)Best solution is probably check possible move positions for King then validate in loop, stopping upon first
		 * successful move position.
		 */
		
		Position[] possibleMoves = new Position[8];
		int length = 0;
		boolean mated = true;
		int tempX;
		int tempY;
		
		for (int i=-1; i<2; i++) {
			for (int j=-1; j<2; j++) {
				tempX = this.position.getX()+i;
				tempY = this.position.getY()+j;
				if ((tempX > 0 && tempX < 7)
						&& (tempY > 0 && tempY < 7)) {
					if (board[tempX][tempY].getTeam() != this.team) {
						if (this.checkMove(tempX,tempY,board)) {
							if (i==0 && j==0) {
								continue;
							} 
							possibleMoves[length] = new Position(tempX,tempY);
							length++;	
						}
					}

				}
			}
		}
		
		for (int i=0; i<length; i++) {
			//check no other pieces can attack here.
			if (checkSafeKingPosition(possibleMoves[i].getX(), possibleMoves[i].getY(),board)) {
				mated = false;
				break;
			}
		}
		
		return mated;
	}

	@Override
	public boolean checkMove(int newX, int newY, Piece[][] board) {
		boolean moveable = false;
		int oldX = this.position.getX();
		int oldY = this.position.getY();
		
		if (board[newX][newY] instanceof Rook && !(((Rook) board[newX][newY]).getPieceMoved())) {
			if (!(this.getPieceMoved())) {
				moveable = true;
			}
		} else if (Math.abs(newX-oldX) <= 1 && Math.abs(newY-oldY) <= 1) {
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
	
	private boolean checkSafeKingPosition(int x, int y, Piece[][] board) {
		/*
		 * Returns true is the space is safe for the king and false if not
		 * 
		 * No need to check pawns specifically because they cross the path of other pieces
		 * 
		 * Another way to achieve this would be to just check positions in row, column, diagonal,
		 * and knight move and then call those positions .checkMove() but would be siginifcantly
		 * slower. Much easier to debug and understand though.
		 * 
		 * NOT OPTIMIZED --- GO BACK AND ELIMINATE SEARCHING PAST COLLISIONS WITH OWN TEAM
		 */
		int newX = x;
		int newY = y;
		boolean moveable = true;
		boolean checks[] = new boolean[16];
		for (int i=0; i<16; i++) {
			checks[i] = true;
		}
		
		//check Vertical
		//Up
		while (newY>=0) {
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Rook 
						|| board[newX][newY] instanceof Queen 
						|| board[newX][newY] instanceof King) {
					checks[0] = false;
				}
				
			}
			newY--;
		}
		newY = y;
		
		//Down
		while (newY<=7) {
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Rook 
						|| board[newX][newY] instanceof Queen 
						|| board[newX][newY] instanceof King) {
					checks[1] = false;
				}
				
			}
			newY++;
		}
		newY = y;
		
		//check horizontal
		//
		while (newX>=0) {
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Rook 
						|| board[newX][newY] instanceof Queen 
						|| board[newX][newY] instanceof King) {
					checks[2] = false;
				}
				
			}
			newX--;
		}
		newX = x;
		
		while (newX<=7) {
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Rook 
						|| board[newX][newY] instanceof Queen 
						|| board[newX][newY] instanceof King) {
					checks[3] = false;
				}
				
			}
			newX++;
		}
		newX = x;

		//check diagonal
		//Top right
		while ((newX >= 0 && newX <= 7) && (newY >= 0 && newY <= 7)) {
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Bishop 
						|| board[newX][newY] instanceof Queen 
						|| board[newX][newY] instanceof King) {
					//moveable = false;
					checks[4] = false;
				}
				
			}
			newX--;
			newY--;
		}
		newX = x;
		newY = y;
		
		//Top left
		while ((newX >= 0 && newX <= 7) && (newY >= 0 && newY <= 7)) {
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Bishop 
						|| board[newX][newY] instanceof Queen 
						|| board[newX][newY] instanceof King) {
					//moveable = false;
					checks[5] = false;
				}
				
			}
			newX--;
			newY++;
		}
		newX = x;
		newY = y;
		
		//Bottom right
		while ((newX >= 0 && newX <= 7) && (newY >= 0 && newY <= 7)) {
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Bishop 
						|| board[newX][newY] instanceof Queen 
						|| board[newX][newY] instanceof King) {
					//moveable = false;
					checks[6] = false;
				}
			
			}
			newX++;
			newY++;
		}
		newX = x;
		newY = y;
		
		//Bottom left
		while ((newX >= 0 && newX <= 7) && (newY >= 0 && newY <= 7)) {
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if ((board[newX][newY] instanceof Bishop 
						|| board[newX][newY] instanceof Queen 
						|| board[newX][newY] instanceof King)) {
					//moveable = false;
					checks[7] = false;
				}
			
			}
			newX--;
			newY++;
		}

		
		newX = x;
		newY = y;
		//check knight move
		Position[] knightMoves = {new Position(newX+=2, newY+=1),
				new Position(newX+=2, newY-=1),
				new Position(newX-=2, newY+=1),
				new Position(newX-=2, newY-=1),
				new Position(newX+=1, newY+=2),
				new Position(newX+=1, newY-=2),
				new Position(newX-=1, newY+=2),
				new Position(newX-=1, newY-=2)
				};
		
		for (int i=0; i<knightMoves.length; i++) {
			if ((knightMoves[i].getX()<0 || knightMoves[i].getX()>7)
					|| knightMoves[i].getY()<0 || knightMoves[i].getY()>7) {
				continue;
			}
			
			if (board[knightMoves[i].getX()][knightMoves[i].getY()] instanceof Knight 
					&& board[knightMoves[i].getX()][knightMoves[i].getY()].getTeam() != this.team) {
				//moveable = false;
				checks[8+i] = false;
			}
		}
		
		for (int i=0; i<16; i++) {
			if (checks[i] == false) {
				moveable = false;
			}
		}
		
		if (!moveable) {
			System.out.println("blocked!");
		}
		
		return moveable;
	}
}