package chessBoard;

import java.util.Collections;
import java.util.Vector;

import custom_rectangle.CustomRectangle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import piece.*;
import piece.Piece.PieceType;
import piece.Piece.Team;


/* 
 *   0 1 2 3 4 5 6 7  X
 *  0[][][][][][][][]
 *  1[][][][][][][][]
 *  2[][][][][][][][]
 *  3[][][][][][][][]
 *  4[][][][][][][][]
 *  5[][][][][][][][]
 *  6[][][][][][][][]
 *  7[][][][][][][][]
 *  
 *  Y
 */
public class Board extends Application {

	private GridPane pane;
	private Scene scene;
	
	double sqrDim = (800/8);
	
	//CoverSquares is the interactive component that lays over the top of the pieces
	private CustomRectangle[][] coverSquares;
	//boardSquares are the component below the pieces (black and white squares)
	private CustomRectangle[][] boardSquares;
	/*
	 * logic board will hold the references to pieces and will be used to determine
	 * whether a piece can move or not.
	*/
	private Piece logicBoard[][];
	private Position whiteKingPosition;
	private Position blackKingPosition;
	private Position selectedPosition;
	private Vector<Position> checkBlockPositions;
	boolean selected;
	boolean checked;
	Team turn;
	
	Color hoverColor = Color.web("#7cfd95");
	Color clickedColor = Color.web ("#00FF00");
	Color cantMoveColor = Color.DARKRED;
	
	RadialGradient hoverGradient = new RadialGradient (0,0,.5,.5,1,true,CycleMethod.NO_CYCLE,new Stop[] {
			new Stop(0, Color.TRANSPARENT), 
			new Stop(.75,hoverColor)
	});
	RadialGradient clickedGradient = new RadialGradient (0,0,.5,.5,1,true,CycleMethod.NO_CYCLE,new Stop[] {
			new Stop(0, Color.TRANSPARENT), 
			new Stop(.95,clickedColor)
	});
	RadialGradient cantMoveGradient = new RadialGradient (0,0,.5,.5,1,true,CycleMethod.NO_CYCLE,new Stop[] {
			new Stop(0, Color.TRANSPARENT), 
			new Stop(.95,cantMoveColor)
	});
	
	/*
	 * BEGIN EVENT HANDLERS
	 */
	EventHandler<MouseEvent> rightClicked = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			boardSquares[selectedPosition.getX()][selectedPosition.getY()].revertToBase();
			selected = false;
		}
		
	};
	
	EventHandler<MouseEvent> mouseEnter = new EventHandler<MouseEvent>() {
		@Override
		public void handle (MouseEvent e) {
			int x = (int) ((Rectangle) e.getSource()).getX();
			int y = (int) ((Rectangle) e.getSource()).getY();
			if (!(x == selectedPosition.getX() && y == selectedPosition.getY())) {
				if (selected) {
					if (canMove(x,y)) {
						boardSquares[x][y].setFill(hoverGradient);
					} else {
						boardSquares[x][y].setFill(cantMoveGradient);
					}
				} else {
					boardSquares[x][y].setFill(hoverGradient);
				}
			}
			e.consume();
		}
	};
	
	EventHandler<MouseEvent> mouseExit = new EventHandler<MouseEvent>() {
		@Override
		public void handle (MouseEvent e) {
			int x = (int) ((Rectangle) e.getSource()).getX();
			int y = (int) ((Rectangle) e.getSource()).getY();
			if (boardSquares[x][y].getFill() != clickedGradient) {
				boardSquares[x][y].revertToBase();
			}
			e.consume();
		}
	};
	
	EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
		@Override
		public void handle (MouseEvent e) {
			if (e.getButton() == MouseButton.SECONDARY)
			{
				boardSquares[selectedPosition.getX()][selectedPosition.getY()].revertToBase();
				selectedPosition.setPosition(-1,-1);
				selected = false;
			} else if (e.getButton() == MouseButton.PRIMARY) {
				int x = (int) ((Rectangle) e.getSource()).getX();
				int y = (int) ((Rectangle) e.getSource()).getY();
				/*//If King is in check
					if (checked) {
						//White turn
						if (turn == Team.White) {
							//If the white king is not selected
							if (!(selectedPosition.getX()==whiteKingPosition.getX() && selectedPosition.getY()==whiteKingPosition.getY())) {
								//Then check for blocking check
								//if not blocking check then consume the event (not valid move)
							}
						//Black turn
						} else {
							//If the black king is not selected
							if (!(selectedPosition.getX()==whiteKingPosition.getX() && selectedPosition.getY()==whiteKingPosition.getY())) {
								//Then check for blocking check
								//if not blocking check then consume the event (not valid move)
							}
						}
					}*/
					if (selected && canMove(x,y)) {
						move(x,y);
						//Check if moved piece has placed king in check
						if (kingChecked(x,y)) {
							checked = true;
							checkBlockPositions = getCheckBlockPositions(x,y);
							if (turn == Team.White && ((King) logicBoard[blackKingPosition.getX()][blackKingPosition.getY()]).isCheckMated(x,y,logicBoard)) {
								endGame(Team.White);
							} else if (((King) logicBoard[whiteKingPosition.getX()][whiteKingPosition.getY()]).isCheckMated(x,y,logicBoard)){
								endGame(Team.Black);
							}
							
						}
						
						if (turn == Team.White) {
							turn = Team.Black;
						} else {
							turn = Team.White;
						}
					} else if (logicBoard[x][y] instanceof Piece && !(logicBoard[x][y] instanceof NullPiece)) {
						if (logicBoard[x][y].getTeam() == turn) {
							selectedPosition.setPosition(x,y);
							selected=true;
							boardSquares[x][y].setFill(clickedGradient);
						}
					}
			}
			e.consume();
		}
	};
	
	/*
	 * BEGIN FUNCTIONS
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setResizable(false);
		pane = new GridPane();
		scene = new Scene(pane);
		//scene.addEventHandler(MouseEvent.MOUSE_CLICKED, rightClicked);
		
		selectedPosition = new Position (-1,-1);
		selected = false;
		setupBoardState();
		turn = Team.White;
		
		stage.setTitle ("Chess Game 1.0");
		stage.setScene(scene);
		stage.show();
		
	}
	
	public boolean canMove(int newX, int newY) {
		if (logicBoard[newX][newY] instanceof Piece) {
			if (logicBoard[newX][newY].getTeam() == turn && !(logicBoard[newX][newY] instanceof Rook)) {
				return false;
			}
		}
		
		if (checked) {
			if (legalMoveWhileInCheck(newX, newY)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (logicBoard[selectedPosition.getX()][selectedPosition.getY()].checkMove(newX, newY, logicBoard)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void move(int newX, int newY) {
		/*
		 * Add clause for Kings castling (i.e. check both havnt moved, also dont forget to set
		 * the pieceMoved value in the move() method when they do move).
		 */
		selected = false;
		checked = false;
		
		/*
		 * Sets special pieces "pieceMoved" attribute which will remove their special movement
		 * abilities
		 */
		if (logicBoard[selectedPosition.getX()][selectedPosition.getY()] instanceof Pawn) {
			((Pawn) logicBoard[selectedPosition.getX()][selectedPosition.getY()]).setPieceMoved();
		} else if (logicBoard[selectedPosition.getX()][selectedPosition.getY()] instanceof Rook) {
			((Rook) logicBoard[selectedPosition.getX()][selectedPosition.getY()]).setPieceMoved();
		} else if (logicBoard[selectedPosition.getX()][selectedPosition.getY()] instanceof King) {
			((King) logicBoard[selectedPosition.getX()][selectedPosition.getY()]).setPieceMoved();
		}
		
		//swap logically
		Piece temp = logicBoard[newX][newY];
		logicBoard[newX][newY] = logicBoard[selectedPosition.getX()][selectedPosition.getY()];
		logicBoard[selectedPosition.getX()][selectedPosition.getY()] = temp;
		
		Node clickedNode = getNodeByPosition(selectedPosition);
		Node newPositionNode = getNodeByPosition(new Position(newX, newY));
		
		ObservableList<Node> children = FXCollections.observableArrayList(pane.getChildren());
		
		int col1 = pane.getColumnIndex(clickedNode);
		int col2 = pane.getColumnIndex(newPositionNode);
		int row1 = pane.getRowIndex(clickedNode);
		int row2 = pane.getRowIndex(newPositionNode);
		
		pane.setColumnIndex(clickedNode, col2);
		pane.setRowIndex(clickedNode, row2);
		pane.setColumnIndex(newPositionNode, col1);
		pane.setRowIndex(newPositionNode, row1);
		
		((Piece) clickedNode).setPosition(newX,newY);
		((Piece) newPositionNode).setPosition(selectedPosition.getX(),selectedPosition.getY());
		
		if ((clickedNode instanceof King && !(newPositionNode instanceof NullPiece)) && newPositionNode instanceof Rook) {
			//If castling your king, do not remove the piece
		} else if (newPositionNode instanceof Piece && !(newPositionNode instanceof NullPiece)) {
			pane.getChildren().remove(newPositionNode);
			logicBoard[selectedPosition.getX()][selectedPosition.getY()] = new NullPiece(PieceType.NullPiece,Team.NullTeam);
			pane.add(logicBoard[selectedPosition.getX()][selectedPosition.getY()],selectedPosition.getX(), selectedPosition.getY());
			logicBoard[selectedPosition.getX()][selectedPosition.getY()].toBack();
		}
		
		//Alter selection and board square coloring
		boardSquares[selectedPosition.getX()][selectedPosition.getY()].revertToBase();
		if (logicBoard[newX][newY] instanceof King) {
			((King) logicBoard[newX][newY]).setPieceMoved();
			logicBoard[newX][newY].setPosition(newX, newY);
			if (((Piece) clickedNode).getTeam() == Team.White) {
				whiteKingPosition.setPosition(newX, newY);
			} else {
				blackKingPosition.setPosition(newX, newY);
			}
		} else if (logicBoard[newX][newY] instanceof Knight) {
			((Knight) logicBoard[newX][newY]).setPieceMoved();
			logicBoard[newX][newY].setPosition(newX, newY);
		}
		selectedPosition.setPosition(-1,-1);
		

	}
	
	public void setupBoardState () {
		
		//Colored Squares of board
		boardSquares = new CustomRectangle[8][8];
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (Math.abs(j-i)%2 == 0) {
					boardSquares[i][j] = new CustomRectangle (sqrDim, sqrDim, Color.GREY);
					boardSquares[i][j].maxHeight(sqrDim);
					boardSquares[i][j].maxWidth(sqrDim);
					pane.add(boardSquares[i][j], i, j);
				} else {
					boardSquares[i][j] = new CustomRectangle (sqrDim, sqrDim, Color.WHITE);
					pane.add(boardSquares[i][j], i, j);
				}
			}
		}
		
		logicBoard = new Piece[8][8];
		
		/*
		 * Place pieces on board.
		 * Since gridpane cannot lookup children by row/column we use
		 * logicBoard to keep a reference to their location to update in the future.
		 */
		for (int i=0; i<8; i++) {
			logicBoard[i][1] = new Pawn(PieceType.Pawn, Team.White);
			logicBoard[i][6] = new Pawn(PieceType.Pawn, Team.Black);
			logicBoard[i][1].setPosition(i, 1);
			logicBoard[i][6].setPosition(i, 6);
			logicBoard[i][1].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
			logicBoard[i][6].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
			pane.add(logicBoard[i][1],i, 1);
			pane.add(logicBoard[i][6],i, 6);
			switch (i) {
				case 0:
					logicBoard[i][0] = new Rook(PieceType.Rook, Team.White);
					logicBoard[i][7] = new Rook(PieceType.Rook, Team.Black);
					logicBoard[i][0].setPosition(i, 0);
					logicBoard[i][7].setPosition(i, 7);
					logicBoard[i][0].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					logicBoard[i][7].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					pane.add(logicBoard[i][0], i, 0);
					pane.add(logicBoard[i][7], i, 7);
					break;
				case 1:
					logicBoard[i][0] = new Knight(PieceType.Knight, Team.White);
					logicBoard[i][7] = new Knight(PieceType.Knight, Team.Black);
					logicBoard[i][0].setPosition(i, 0);
					logicBoard[i][7].setPosition(i, 7);
					logicBoard[i][0].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					logicBoard[i][7].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					pane.add(logicBoard[i][0], i, 0);
					pane.add(logicBoard[i][7], i, 7);
					break;
				case 2:
					logicBoard[i][0] = new Bishop(PieceType.Bishop, Team.White);
					logicBoard[i][7] = new Bishop(PieceType.Bishop, Team.Black);
					logicBoard[i][0].setPosition(i, 0);
					logicBoard[i][7].setPosition(i, 7);
					logicBoard[i][0].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					logicBoard[i][7].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					pane.add(logicBoard[i][0], i, 0);
					pane.add(logicBoard[i][7], i, 7);
					break;
				case 3:
					logicBoard[i][0] = new Queen(PieceType.Queen, Team.White);
					logicBoard[i][7] = new Queen(PieceType.Queen, Team.Black);
					logicBoard[i][0].setPosition(i, 0);
					logicBoard[i][7].setPosition(i, 7);
					logicBoard[i][0].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					logicBoard[i][7].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					pane.add(logicBoard[i][0], i, 0);
					pane.add(logicBoard[i][7], i, 7);
					break;
				case 4:
					logicBoard[i][0] = new King(PieceType.King, Team.White);
					logicBoard[i][7] = new King(PieceType.King, Team.Black);
					logicBoard[i][0].setPosition(i, 0);
					logicBoard[i][7].setPosition(i, 7);
					logicBoard[i][0].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					logicBoard[i][7].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					pane.add(logicBoard[i][0], i, 0);
					pane.add(logicBoard[i][7], i, 7);
					whiteKingPosition = new Position (i,0);
					blackKingPosition = new Position (i,7);
					break;
				case 5:
					logicBoard[i][0] = new Bishop(PieceType.Bishop, Team.White);
					logicBoard[i][7] = new Bishop(PieceType.Bishop, Team.Black);
					logicBoard[i][0].setPosition(i, 0);
					logicBoard[i][7].setPosition(i, 7);
					logicBoard[i][0].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					logicBoard[i][7].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					pane.add(logicBoard[i][0], i, 0);
					pane.add(logicBoard[i][7], i, 7);
					break;
				case 6:
					logicBoard[i][0] = new Knight(PieceType.Knight, Team.White);
					logicBoard[i][7] = new Knight(PieceType.Knight, Team.Black);
					logicBoard[i][0].setPosition(i, 0);
					logicBoard[i][7].setPosition(i, 7);
					logicBoard[i][0].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					logicBoard[i][7].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					pane.add(logicBoard[i][0], i, 0);
					pane.add(logicBoard[i][7], i, 7);
					break;
				case 7:
					logicBoard[i][0] = new Rook(PieceType.Rook, Team.White);
					logicBoard[i][7] = new Rook(PieceType.Rook, Team.Black);
					logicBoard[i][0].setPosition(i, 0);
					logicBoard[i][7].setPosition(i, 7);
					logicBoard[i][0].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					logicBoard[i][7].getChildren().add(new CustomRectangle(sqrDim, sqrDim, Color.TRANSPARENT));
					pane.add(logicBoard[i][0], i, 0);
					pane.add(logicBoard[i][7], i, 7);
					break;
			}
		}
		
		for (int i=0; i<8; i++) {
			for (int j=2; j<6; j++) {
				logicBoard[i][j] = new NullPiece(PieceType.NullPiece, Team.NullTeam);
				logicBoard[i][j].setPosition(i, j);
				pane.add(logicBoard[i][j], i, j);
			}
		}
		
		//removeInteractions();
		
		
		
		//Cover squares for highlighting
		coverSquares = new CustomRectangle[8][8];
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				coverSquares[i][j] = new CustomRectangle (sqrDim, sqrDim, Color.TRANSPARENT);
				coverSquares[i][j].setX(i);
				coverSquares[i][j].setY(j);
				coverSquares[i][j].addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnter);
				coverSquares[i][j].addEventHandler(MouseEvent.MOUSE_EXITED, mouseExit);
				coverSquares[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
			}
		}
		
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				pane.add(coverSquares[i][j], i, j);
			}
		}
	}
	
	public Node getNodeByPosition (Position pos) {

	    Node result = null;
	    ObservableList<Node> children = pane.getChildren();
	    for (Node node : children) {
	    	if (node instanceof Piece) {
	    		if (((Piece) node).getPosition().isEqual(pos)) {
	    			result = node;
	    		}
	    	}
	    }

	    return result;
	}
	
	public boolean kingChecked (int x, int y) {
		Position pos;
		
		if (turn == Team.White) {
			pos = blackKingPosition;
		} else {
			pos = whiteKingPosition;
		}
		
		if (logicBoard[x][y].checkMove(pos.getX(),pos.getY(), logicBoard)) {
			System.out.println ("check");
			return true;
		} else {
			System.out.println ("not check");
			return false;
		}
		
	}

	public boolean legalMoveWhileInCheck(int newX, int newY) {
		boolean result = false;

		//If King is trying to move, test the move for safety
		if (logicBoard[selectedPosition.getX()][selectedPosition.getY()] instanceof King) {
			if (((King) logicBoard[selectedPosition.getX()][selectedPosition.getY()]).checkSafeKingPosition(newX,newY,logicBoard)
					&& logicBoard[selectedPosition.getX()][selectedPosition.getY()].checkMove(newX,newY,logicBoard)) {
				result = true;
			}
		//If some piece other than King is trying to move, check to see if it is moving to a position in the blockings positions vector
		} else {
			for (Position i:checkBlockPositions) {
				if (newX==i.getX() && newY==i.getY()) {
					result = true;
					break;
				}
			}
			if (!logicBoard[selectedPosition.getX()][selectedPosition.getY()].checkMove(newX,newY,logicBoard)) {
				result = false;
			}
		}
		
		return result;
	}
	
	public Vector<Position> getCheckBlockPositions (int attackerX, int attackerY) {
		Vector<Position> result = new Vector<Position>();
		
		/*
		 * Piece just moved to new position
		 * passed its x and y
		 * 
		 * if black turn==black check to white king position, if turn==white check to black king position
		 * check for if (piece instanceof rook, bishop or queen (cant block pawn or knight + cant be king).
		 */
		if (turn==Team.White) {
			if (logicBoard[attackerX][attackerY] instanceof Bishop) {
				result = King.getPositionsBetweenDiagonal(attackerX, attackerY, blackKingPosition.getX(), blackKingPosition.getY());
			} else if (logicBoard[attackerX][attackerY] instanceof Rook) {
				result = King.getPositionsBetweenHorVert(attackerX, attackerY, blackKingPosition.getX(), blackKingPosition.getY());
			} else if (logicBoard[attackerX][attackerY] instanceof Queen) {
				if (attackerX==blackKingPosition.getX() || attackerY==blackKingPosition.getY()) {
					result = King.getPositionsBetweenHorVert(attackerX, attackerY, blackKingPosition.getX(), blackKingPosition.getY());
				} else {
					result = King.getPositionsBetweenDiagonal(attackerX, attackerY, blackKingPosition.getX(), blackKingPosition.getY());
				}
			}
		} else {
			if (logicBoard[attackerX][attackerY] instanceof Bishop) {
				result = King.getPositionsBetweenDiagonal(attackerX, attackerY, whiteKingPosition.getX(), whiteKingPosition.getY());
			} else if (logicBoard[attackerX][attackerY] instanceof Rook) {
				result = King.getPositionsBetweenHorVert(attackerX, attackerY, whiteKingPosition.getX(), whiteKingPosition.getY());
			} else if (logicBoard[attackerX][attackerY] instanceof Queen) {
				if (attackerX==whiteKingPosition.getX() || attackerY==whiteKingPosition.getY()) {
					result = King.getPositionsBetweenHorVert(attackerX, attackerY, whiteKingPosition.getX(), whiteKingPosition.getY());
				} else {
					result = King.getPositionsBetweenDiagonal(attackerX, attackerY, whiteKingPosition.getX(), whiteKingPosition.getY());
				}
			}
		}
		
		return result;
	}
	
	public void endGame (Team team) {
		if (team == Team.White) {
			System.out.println("White team has won!");
		} else {
			System.out.println("Black team has won!");
		}
	}
}
