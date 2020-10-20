package custom_rectangle;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CustomRectangle extends Rectangle {
	Color baseColor;
	
	public CustomRectangle(double x, double y, Color color) {
		super (x,y,color);
		baseColor = color;
	}

	public void setBaseColor (Color color) {
		baseColor = color;
	}
	
	public void revertToBase () {
		this.setFill(baseColor);
	}
}
