package Game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Base implements Paintable {
    private int xPos;
    private int yPos;
    public final double height = 100;
    public final double width = 100;
    Image base;

    public Base (int x, int y) throws FileNotFoundException {
        this.xPos = x;
        this.yPos = y;
        FileInputStream inputStream = new FileInputStream("src/Game/images/base.png");
        this.base = new Image(inputStream, width, height, true, false);
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public int getX() {
        return this.xPos;
    }

    public int getY() {
        return this.yPos;
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.drawImage(this.base, this.getX(), this.getY());
    }
}
