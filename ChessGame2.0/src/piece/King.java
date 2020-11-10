package piece;

import java.util.Vector;

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

	public boolean isCheckMated(int attackerX, int attackerY, Piece[][] board) {
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
		
		//check for blocks
		if (mated==true) {
			if (blockAttacker(attackerX, attackerY, board)) {
				mated = false;
			} else if (takeAttacker(attackerX, attackerY, board)) {
				mated = false;
			}
		}
		
		return mated;
	}
	
	private boolean blockAttacker (int attackerX, int attackerY, Piece[][] board) {
		boolean result = false;
		Vector<Position> blockablePositions = new Vector<Position>();
		Vector<Piece> pieces = new Vector<Piece>();
		
		//Cannot block a knight or pawn, so skip this logic
		if (!(board[attackerX][attackerY] instanceof Knight || board[attackerX][attackerY] instanceof Pawn)) {
			
			int currentX = this.position.getX();
			int currentY = this.position.getY();
			
			for (int i=0;i <8; i++) {
				for (int j=0; j<8; j++) {
					if ((board[i][j].getTeam() == this.team) && !(board[i][j] instanceof King)) {
						pieces.add(board[i][j]);
					}
				}
			}
			
			if (board[attackerX][attackerY] instanceof Bishop) {
				blockablePositions = getPositionsBetweenDiagonal(currentX, currentY, attackerX, attackerY);
			} else if (board[attackerX][attackerY] instanceof Rook) {
				blockablePositions = getPositionsBetweenHorVert(currentX, currentY, attackerX, attackerY);
			} else if (board[attackerX][attackerY] instanceof Queen) {
				if (currentX==attackerX || currentY==attackerY) {
					blockablePositions = getPositionsBetweenHorVert(currentX, currentY, attackerX, attackerY);
				} else {
					blockablePositions = getPositionsBetweenDiagonal(currentX, currentY, attackerX, attackerY);
				}
			}
		}
		
		for (Piece i:pieces) {
			for (Position j: blockablePositions) {
				if (i.checkMove(j.getX(), j.getY(), board)) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	public static Vector<Position> getPositionsBetweenDiagonal (int x1, int y1, int x2, int y2) {
		Vector<Position> result = new Vector<Position>();
		
		if (x1 < x2) {
			if (y1 < y2) {
				x1++;
				y1++;
				while (y1 < y2) {
					result.add(new Position(x1, y1));
					x1++;
					y1++;
				}
			} else {
				x1++;
				y1--;
				while (y1 > y2) {
					result.add(new Position(x1, y1));
					x1++;
					y1--;
				}
			}
		} else {
			if (y1 < y2) {
				x1--;
				y1++;
				while (y1 < y2) {
					result.add(new Position(x1, y1));
					x1--;
					y1++;
				}
			} else {
				x1--;
				y1--;
				while (y1 > y2) {
					result.add(new Position(x1, y1));
					x1--;
					y1--;
				}
			}
		}
		
		return result;
	}
	
	public static Vector<Position> getPositionsBetweenHorVert (int x1, int y1, int x2, int y2) {
		Vector<Position> result = new Vector<Position>();
		
		if (x1==x2) {
			int largeY, smallY;
			if (y1 < y2) {
				largeY = y2;
				smallY = y1;
			} else {
				largeY = y1;
				smallY = y2;
			}
			for (int i=smallY; i<largeY; i++) {
				result.add(new Position(x1, i));
			}
		} else {
			int largeX, smallX;
			if (x1 < x2) {
				largeX = x2;
				smallX = x1;
			} else {
				largeX = x1;
				smallX = x2;
			}
			for (int i=smallX; i<largeX; i++) {
				result.add(new Position(i, y1));
			}
		}
		
		return result;
	}
	
	
	private boolean takeAttacker (int attackerX, int attackerY, Piece[][] board) {
		boolean result = false;
		
		for (int i=0;i <8; i++) {
			for (int j=0; j<8; j++) {
				if (board[i][j].getTeam() == this.team) {
					if (board[i][j].checkMove(attackerX,attackerY,board)) {
						result = true;
						break;
					}
				}
			}
		}
		
		return result;
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
			if (checkSafeKingPosition(newX, newY, board)) {
				moveable = true;
			}
			
		}
		
		return moveable;
	}
	
	public void setPieceMoved() {
		pieceMoved = true;
	}
	
	public boolean getPieceMoved () {
		return pieceMoved;
	}
	
	public boolean checkSafeKingPosition(int x, int y, Piece[][] board) {

		/*
		 * Returns true if the space is safe for the king and false if not
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
			if (board[newX][newY].getTeam() == this.team) {
				break;
			}
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Rook 
						|| board[newX][newY] instanceof Queen 
						|| (board[newX][newY] instanceof King && newY==this.position.getY()+1)) {
					checks[0] = false;
				}
				
			}
			newY--;
		}
		newY = y;
		
		//Down
		while (newY<=7) {
			if (board[newX][newY].getTeam() == this.team) {
				break;
			}
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Rook 
						|| board[newX][newY] instanceof Queen 
						|| (board[newX][newY] instanceof King && newY==this.position.getY()-1)) {
					checks[1] = false;
				}
				
			}
			newY++;
		}
		newY = y;
		
		//check horizontal
		//right
		while (newX>=0) {
			if (board[newX][newY].getTeam() == this.team) {
				break;
			}
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Rook 
						|| board[newX][newY] instanceof Queen 
						|| (board[newX][newY] instanceof King && newX==this.position.getX()+1)) {
					checks[2] = false;
				}
				
			}
			newX--;
		}
		newX = x;
		
		//Left
		while (newX<=7) {
			if (board[newX][newY].getTeam() == this.team) {
				break;
			}
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Rook 
						|| board[newX][newY] instanceof Queen 
						|| (board[newX][newY] instanceof King && newX==this.position.getX()+1)) {
					checks[3] = false;
				}
				
			}
			newX++;
		}
		newX = x;

		//check diagonal
		//Top right
		while ((newX >= 0 && newX <= 7) && (newY >= 0 && newY <= 7)) {
			if (board[newX][newY].getTeam() == this.team) {
				break;
			}
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Bishop 
						|| board[newX][newY] instanceof Queen 
						|| (board[newX][newY] instanceof King && (newX==this.position.getX()+1) && newY==this.position.getY()-1)) {
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
			if (board[newX][newY].getTeam() == this.team) {
				break;
			}
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Bishop 
						|| board[newX][newY] instanceof Queen 
						|| (board[newX][newY] instanceof King && (newX==this.position.getX()-1) && newY==this.position.getY()-1)) {
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
			if (board[newX][newY].getTeam() == this.team) {
				break;
			}
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if (board[newX][newY] instanceof Bishop 
						|| board[newX][newY] instanceof Queen 
						|| (board[newX][newY] instanceof King && (newX==this.position.getX()+1) && newY==this.position.getY()+1)) {
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
			if (board[newX][newY].getTeam() == this.team) {
				break;
			}
			if (board[newX][newY] instanceof Piece && !(board[newX][newY] instanceof NullPiece)
					&& board[newX][newY].getTeam() != this.team) {
				
				if ((board[newX][newY] instanceof Bishop 
						|| board[newX][newY] instanceof Queen 
						|| (board[newX][newY] instanceof King && (newX==this.position.getX()-1) && newY==this.position.getY()+1))) {
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