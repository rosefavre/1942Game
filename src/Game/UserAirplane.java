package Game;

import java.io.FileNotFoundException;

public class UserAirplane extends Planes{

    public UserAirplane(int x, int y) throws FileNotFoundException {
        super(x, y, 100,100,"src/Game/images/userAirplane.png");
    }

    public void moveRight(int step) {
        this.setPosition(this.getX()+step, this.getY());
    }

    public void moveLeft (int step) {
        this.setPosition(this.getX()-step, this.getY());
    }

    public void moveDown(int step) {this.setPosition(this.getX(), this.getY()+step);}

    public void moveUp(int step) {this.setPosition(this.getX(), this.getY()-step);}
}
