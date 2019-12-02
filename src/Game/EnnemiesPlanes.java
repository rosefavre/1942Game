package Game;

import java.io.FileNotFoundException;

public class EnnemiesPlanes extends Planes {

    public int lives;

    public EnnemiesPlanes(int x, int y, int width, int height, int numberOfLives, String pathToImage) throws FileNotFoundException {
        super(x, y,width,height, pathToImage);
        this.lives = numberOfLives;
    }

    public void moveDown(int step) {
        this.setPosition(this.getX(), this.getY()+step);
    }

    public int getLives(){
        return this.lives;
    }

    public void setLives(int lives){
        this.lives = lives;
    }

}
