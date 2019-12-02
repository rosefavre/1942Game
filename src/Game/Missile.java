package Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Missile implements Paintable {

    private int xPos;
    private int yPos;
    Image missile;

    public Missile(int x, int y) throws FileNotFoundException {
        this.xPos = x;
        this.yPos = y;
        FileInputStream inputStream = new FileInputStream("src/Game/images/missile.png");
        this.missile = new Image(inputStream, 40, 40, true, false);
    }

    public int getX() {
        return this.xPos;
    }

    public int getY(){
        return this.yPos;
    }

    public void setPosition(int x, int y){
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.drawImage(this.missile, this.getX(), this.getY());
    }


    public void moveUp(int step) {
        this.setPosition(this.getX(), this.getY()-step);
    }

}
