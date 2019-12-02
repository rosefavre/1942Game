package Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Background implements Paintable{

    private Image sand;

    public Background() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("src/Game/images/sand.jpg");
        this.sand = new Image(inputStream, 1000, 1000, false, false);
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.drawImage(this.sand, 0,0);
    }
}
