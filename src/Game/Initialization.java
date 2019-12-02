package Game;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509IssuerSerial;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import sun.print.BackgroundLookupListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ConcurrentModificationException;
import java.util.concurrent.CopyOnWriteArrayList;


public class Initialization extends Application{

    public static void main(String[] args)
    {
        launch(args);
    }

    Missile missile = null;

    public void start(Stage theStage) throws FileNotFoundException, InterruptedException {
        theStage.setTitle("1942 - Game");
        theStage.setHeight(1000);
        theStage.setWidth(1000);        //definition of the stage

        Group root = new Group();

        Scene theScene = new Scene (root);
        theStage.setScene(theScene);
        Canvas canvas = new Canvas( 1000, 1000 );       //definition of the canvas
        root.getChildren().add(canvas);
    GraphicsContext gc = canvas.getGraphicsContext2D();         //definition of the graphic contest

        List<Paintable> paintables = new CopyOnWriteArrayList<>();      //creation of a list of paintable objects

        List<Missile> missilesList = new CopyOnWriteArrayList<>();      //creation of a list of objects missiles

        List<EnnemiesPlanes> ennemiesList = new CopyOnWriteArrayList<>();      //creation of a list of ennemies planes

        Background background = new Background();       //creation of background
        paintables.add(background);

        Base base = new Base (0, (int)canvas.getHeight());

        for (int i=0; i<= canvas.getWidth()-base.getWidth(); i+= base.getWidth()) {       //creation of several bases on the whole width of canvas
            paintables.add(new Base(i, (int)canvas.getHeight()-148));
        }


        UserAirplane userAirplane = new UserAirplane((int) canvas.getWidth()/2,  (int) (3*(canvas.getHeight())/4-50)); //creation of user plane
        paintables.add(userAirplane);

        double start = System.nanoTime();

        new AnimationTimer(){

            double frequency = 3000000000.0;        //frequence in which ennemies planes appear
            double creationTime = frequency;        //time of creation of little ennemies planes
            double creationTimeBigEnnemy = 15000000000.0;   //time of creation of big ennemies planes

            @Override
            public void handle(long now) {

                int userLives = 8;  //define initial amount of user lives

                for (Paintable obj:paintables) {       //draw all paintable objects
                    obj.paint(gc);
                }


                for (Missile missile:missilesList) {
                    if(missile.getY() > -40){      //check if the whole missile is still in the canvas
                        missile.moveUp(4);      //make it move
                    }
                    else {
                        paintables.remove(missile);     //remove missile from paintable objects if it's not in the canvas anymore
                        missilesList.remove(missile);   //remove missile from missiles list if it's not in the canvas anymore
                    }
                }


                for (EnnemiesPlanes ennemy:ennemiesList) {
                    if(ennemy.getY() < canvas.getHeight()){   //check if ennemy plane is still in the canvas
                        ennemy.moveDown(2);     //make it move
                    }
                    else{                               //if the plane is not in the canvas anymore
                        if(ennemy.getLives()==1){       //check if the ennemy plane has 1 or 2 lives (if it is a big or a little)
                            userLives -= 1;             //if it is a little we remove one life to the user
                        }
                        else{
                            userLives -= 2;             //if it is a big one, we remove two lives to the user
                        }
                        paintables.remove(ennemy);      //finally we remove the ennemy plane from the paintable objects
                    }
                }


                gc.fillText("Lives: " + userLives, 800, 50);    //make number of remaining user lives appear on the screen
                gc.setFill( Color.RED );
                Font theFont = Font.font( "Product Sans", FontWeight.BOLD, 50 );
                gc.setFont(theFont);


                if(userLives <=0) {     //check if the user doesn't have lives anymore
                    GameOver textGameOver = new GameOver(); //make game over appear on the screen
                    textGameOver.paint(gc);
                    this.stop();    //stop the game
                }


                double time = (now - start);
                //System.out.println(time);


                if(time > 30000000000.0){       //check if the current time is greater than 30 secondes
                    frequency = 2000000000.0;   //if yes, create a new ennemy plane each 2 seconds
                }

                if(time > 60000000000.0){       //check if the current time is greater than 60 secondes
                    frequency = 1000000000.0;   //if yes, create a new ennemy plane each second
                }

                Random rand = new Random();
                int ennemyXPosition = rand.nextInt((int)canvas.getWidth()-100 - 0 + 1); //create a random X position for the little ennemy plane

                EnnemiesPlanes littleEnnemyPlane = null;

                if(time > creationTime){    //check if it is the time to create a new little ennemy plane
                    try {
                        littleEnnemyPlane = new EnnemiesPlanes(ennemyXPosition,-100, 100, 100, 1,"src/Game/images/littleEnnemyPlane.png");
                        paintables.add(littleEnnemyPlane);
                        ennemiesList.add(littleEnnemyPlane);
                        creationTime +=frequency;   //increase creation time
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                Random rand2 = new Random();
                int bigEnnemyXPosition = rand2.nextInt((int)canvas.getWidth()-100 - 0 + 1); //create a random X position for big ennemy plane
                EnnemiesPlanes bigEnnemyPlane = null;

                if(time > creationTimeBigEnnemy){   //check if it is time to create a new big ennemy plane
                    try{
                        bigEnnemyPlane = new EnnemiesPlanes(bigEnnemyXPosition, -100, 150, 150, 2,"src/Game/images/bigEnnemyPlane.png");
                        paintables.add(bigEnnemyPlane);
                        ennemiesList.add(bigEnnemyPlane);
                        creationTimeBigEnnemy += 15000000000.0; //create a new big ennemy plane each 15 seconds
                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }


                theScene.setOnKeyPressed((KeyEvent event)-> {   //user keyboard input

                    if(event.getCode()!=null) {
                        switch (event.getCode()){
                            case LEFT : userAirplane.moveLeft(15);
                                        break;
                            case RIGHT : userAirplane.moveRight(15);
                                        break;
                            case DOWN : userAirplane.moveDown(15);
                                        break;
                            case UP : userAirplane.moveUp(15);
                                        break;
                            case SPACE :
                                         try {
                                            missile = new Missile(userAirplane.getX(), userAirplane.getY());
                                             paintables.add(missile);
                                             missilesList.add(missile);


                                         } catch (FileNotFoundException e) {
                                         e.printStackTrace();
                                         System.out.println("error");
                                         }

                        }
                    }

                });


                for (Missile missile:missilesList) {        //this loop checks if an ennemy plane has been touched by a missile
                    for (EnnemiesPlanes ennemy: ennemiesList) {
                        if (missile.getX() > (ennemy.getX()-20) && missile.getX() < (ennemy.getX()+100)
                                && missile.getY() > ennemy.getY() && missile.getY() < (ennemy.getY()+100)){ //check if missile position is in the ennemy plane area
                            if(ennemy.getLives() == 1){     //check if the ennemy plane has only one life
                                paintables.remove(missile); //if yes, remove the given missile and the given ennemy plane from the canvas
                                missilesList.remove(missile);
                                paintables.remove(ennemy);
                                ennemiesList.remove(ennemy);
                            }
                            else{
                                ennemy.setLives(ennemy.getLives()-1);   //if the ennemy plane had 2 lives, remove 1 life from it
                                paintables.remove(missile); //remove missile from canvas
                                missilesList.remove(missile);
                            }

                        }
                    }
                }


            }
        }.start();

        theStage.show();
    }

}
