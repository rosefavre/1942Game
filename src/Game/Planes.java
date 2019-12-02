package Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Planes implements Paintable {

    private int xPos;
    private int yPos;
    private Image plane;

    public Planes (int x, int y, int width, int height, String pathToImage) throws FileNotFoundException {
        this.xPos = x;
        this.yPos = y;
        FileInputStream inputStream = new FileInputStream(pathToImage);
        this.plane = new Image(inputStream, width, height, true, false);
    }

    public int getX() {
        return this.xPos;
    }

    public int getY() {
        return this.yPos;
    }

    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }


    @Override
    public void paint(GraphicsContext gc) {
        gc.drawImage(this.plane, this.getX(), this.getY());
    }
}
