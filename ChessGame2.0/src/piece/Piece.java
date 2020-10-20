package piece;

import chessBoard.Position;
import custom_rectangle.CustomRectangle;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public abstract class Piece extends StackPane{
	public static enum PieceType {
		Rook,
		Knight,
		Bishop,
		King,
		Queen,
		Pawn,
		NullPiece
	}
	
	public static enum Team {
		White,
		Black,
		NullTeam
	}
	
	protected Position position;
	protected Image picture;
	protected ImageView image;
	protected PieceType pieceType;
	protected Team team;
	
	public Piece (PieceType pieceType, Team team) {
		this.pieceType = pieceType;
		this.team = team;
		if (pieceType != PieceType.NullPiece) {
			ImageFactory imageFact = this.new ImageFactory(pieceType, team);
		}
		position = new Position(0,0);
	}
	
	/*
	 * CheckMove will check whether the piece can move there. The method canMove in
	 * board class purely checks whether the place is occupied by a piece of the same
	 * team. OPTIMIZE THIS SILLY FUNCTIONALITY OUT LATER
	 */
	public abstract boolean checkMove (Position newPosition);
	
	public abstract boolean checkMove (int newX, int newY, Piece[][] board);
	
	public void setPosition (int x, int y) {
		position.setPosition(x,y);
	}
	
	public Position getPosition () {
		return position;
	}
	
	public Team getTeam () {
		return this.team;
	}
	
	//Constructs the image data for the piece
	public class ImageFactory {
		
		double sqrDim = (800/8);
		
		int[][] resourceViewportData = {
			{80,2,20,20},//Rook
			{60,2,20,20},//Knight
			{40,2,20,20},//Bishop
			{0,0,20,20},//King
			{20,2,20,20},//Queen
			{100,4,20,20},//Pawn
			{0,0,0,0}//NullPiece
		};
		
		int[] viewPortData = new int[4];
		
		public ImageFactory (PieceType pieceType, Team team) {
			
			//Sets up the background to the piece image
			Circle circle = new Circle (50,50,40,Color.DIMGREY);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(1);
			Piece.this.getChildren().add(circle);
			
			switch(Piece.this.pieceType) {
				case Rook:
					viewPortData = resourceViewportData[0];
					break;
				case Knight:
					viewPortData = resourceViewportData[1];
					break;
				case Bishop:
					viewPortData = resourceViewportData[2];
					break;
				case King:
					viewPortData = resourceViewportData[3];
					break;
				case Queen:
					viewPortData = resourceViewportData[4];
					break;
				case Pawn:
					viewPortData = resourceViewportData[5];
					break;
				case NullPiece:
					viewPortData = resourceViewportData[6];
					break;
			}
			
			//Pulls the image file and sets the ImageView
			Piece.this.picture = new Image ("file:C:\\Users\\Nick's PC\\eclipse-workspace\\Chess\\src\\Images\\ChessPieceImages.png", false);
			Piece.this.image = new ImageView(Piece.this.picture);
			Piece.this.image.setFitHeight(60);
			Piece.this.image.setFitWidth(60);
			
			if (team == Team.White) {
				//Adjusts the viewport per each piece type + adjusts the color scheme for white pieces.
				Piece.this.image.setViewport(new Rectangle2D (viewPortData[0],viewPortData[1],viewPortData[2],viewPortData[3]));
				Piece.this.image.setCache(true);
				ColorAdjust colorAdjust = new ColorAdjust ();
				colorAdjust.setBrightness(1.0);
				Piece.this.image.setEffect(colorAdjust);
				Piece.this.getChildren().add(Piece.this.image);
			} else {
				Piece.this.image.setViewport(new Rectangle2D (viewPortData[0],viewPortData[1],viewPortData[2],viewPortData[3]));
				Piece.this.image.setCache(true);
				Piece.this.getChildren().add(Piece.this.image);
			}
		}
		
	}
}
