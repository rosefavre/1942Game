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
       // Planes airplane = new Planes(0,0);
    }

    Missile missile = null;

    public void start(Stage theStage) throws FileNotFoundException, InterruptedException {
        theStage.setTitle("1942 - Game");
        theStage.setHeight(1000);
        theStage.setWidth(1000);

        Group root = new Group();

        Scene theScene = new Scene (root);
        theStage.setScene(theScene);
        Canvas canvas = new Canvas( 1000, 1000 );
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        List<Paintable> paintables = new CopyOnWriteArrayList<>();

        List<Missile> missilesList = new CopyOnWriteArrayList<>();

        List<EnnemiesPlanes> ennemiesList = new CopyOnWriteArrayList<>();

        Background background = new Background();
        paintables.add(background);

        Base base = new Base (0, (int)canvas.getHeight());

        for (int i=0; i<= canvas.getWidth()-base.getWidth(); i+= base.getWidth()) {
            paintables.add(new Base(i, (int)canvas.getHeight()-148));
        }


        UserAirplane userAirplane = new UserAirplane((int) canvas.getWidth()/2,  (int) (3*(canvas.getHeight())/4-50));
        paintables.add(userAirplane);

        double start = System.nanoTime();

        new AnimationTimer(){

            double frequency = 3000000000.0;
            double creationTime = frequency;
            double creationTimeBigEnnemy = 15000000000.0;

            @Override
            public void handle(long now) {

                int userLives = 8;

                for (Paintable obj:paintables) {
                    obj.paint(gc);
                }


                for (Missile missile:missilesList) {
                    if(missile.getY() > -40){
                        missile.moveUp(4);
                    }
                    else {
                        paintables.remove(missile);
                    }
                }


                for (EnnemiesPlanes ennemy:ennemiesList) {
                    if(ennemy.getY() < canvas.getHeight()){
                        ennemy.moveDown(2);
                    }
                    else{
                        paintables.remove(ennemy);
                        userLives -= 1;
                    }
                }

                //System.out.println("lives : " + userLives);
                gc.fillText("Lives: " + userLives, 800, 50);
                gc.setFill( Color.RED );
                Font theFont = Font.font( "Product Sans", FontWeight.BOLD, 50 );
                gc.setFont(theFont);


                if(userLives <=0) {
                    GameOver textGameOver = new GameOver();
                    textGameOver.paint(gc);
                    this.stop();
                }


                double time = (now - start);
                //System.out.println(time);


                if(time > 30000000000.0){
                    frequency = 2000000000.0;
                }

                if(time > 60000000000.0){
                    frequency = 1000000000.0;
                }

                Random rand = new Random();
                int ennemyXPosition = rand.nextInt((int)canvas.getWidth()-100 - 0 + 1);

                EnnemiesPlanes littleEnnemyPlane = null;

                if(time > creationTime){
                    try {
                        littleEnnemyPlane = new EnnemiesPlanes(ennemyXPosition,-100, 100, 100, 1,"src/Game/images/littleEnnemyPlane.png");
                        paintables.add(littleEnnemyPlane);
                        ennemiesList.add(littleEnnemyPlane);
                        creationTime +=frequency;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                Random rand2 = new Random();
                int bigEnnemyXPosition = rand2.nextInt((int)canvas.getWidth()-100 - 0 + 1);
                EnnemiesPlanes bigEnnemyPlane = null;

                if(time > creationTimeBigEnnemy){
                    try{
                        bigEnnemyPlane = new EnnemiesPlanes(bigEnnemyXPosition, -100, 150, 150, 2,"src/Game/images/bigEnnemyPlane.png");
                        paintables.add(bigEnnemyPlane);
                        ennemiesList.add(bigEnnemyPlane);
                        creationTimeBigEnnemy += 15000000000.0;
                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }


                theScene.setOnKeyPressed((KeyEvent event)-> {

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


                for (Missile missile:missilesList) {
                    for (EnnemiesPlanes ennemy: ennemiesList) {
                        if (missile.getX() > (ennemy.getX()-10) && missile.getX() < (ennemy.getX()+100) && missile.getY() > ennemy.getY() && missile.getY() < (ennemy.getY()+100)){
                            if(ennemy.getLives() == 1){
                                paintables.remove(missile);
                                missilesList.remove(missile);
                                paintables.remove(ennemy);
                                ennemiesList.remove(ennemy);
                            }
                            else{
                                ennemy.setLives(ennemy.getLives()-1);
                                paintables.remove(missile);
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
