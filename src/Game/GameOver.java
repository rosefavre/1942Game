package Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameOver implements Paintable {

    String gameOverString;

    public GameOver(){
        gameOverString = "Game Over";
    }


    @Override
    public void paint(GraphicsContext gc) {
        gc.fillText(gameOverString, 400, 500);
        gc.setFill( Color.RED );
        Font theFont = Font.font( "Product Sans", FontWeight.BOLD, 50);
        gc.setFont(theFont);
    }
}
