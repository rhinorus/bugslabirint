package com.ecato.bugslabirint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.reflect.Field;


public class GameScreen implements Screen {
    final OurGame game;
    OrthographicCamera camera;
    static int SCALE = 20;
    int GAME_SPEED = 150;

    Player player;

    BitmapFont font;

    Texture BugImg;
    Texture WallImg;
    Texture DoorImg;

    Texture StartBtn;
    Texture StopBtn;
    Texture ClearBtn;

    Texture MscOnBtn;
    Texture MscOffBtn;

    Texture spx1on;
    Texture spx1off;
    Texture spx2on;
    Texture spx2off;
    Texture spx3on;
    Texture spx3off;
    Texture spx4on;
    Texture spx4off;

    Sound NewRecord;
    Sound Oops;
    Music bgMusic;

    int record;
    int Scores;
    int touchedX;
    int touchedY;
    int fieldX;
    int fieldY;
    int playerX;
    int playerY;
    int [][] GameField;
    double ScreensConflictX;
    double ScreensColflictY;
    long lastTouchTime;
    long lastTurningTime;
    boolean isPlaying;
    boolean isMusicOn;
    String result;


    public GameScreen(final OurGame gam){

        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);
        font = new BitmapFont();

        GameField = new int[25][15];

        record = game.prefs.getInteger("record",0);
        if(game.prefs.getString("labirint",null) != null){
            GameField = GetLabirint(game.prefs.getString("labirint",null));
        }

        ScreensConflictX = (double) 800 / Gdx.graphics.getWidth();
        ScreensColflictY = (double) 480 / Gdx.graphics.getHeight();

        BugImg = new Texture("bug.png");
        WallImg = new Texture("wall.png");
        DoorImg = new Texture("door.png");

        StartBtn = new Texture("startbtn.png");
        StopBtn = new Texture("stopbtn.png");
        ClearBtn = new Texture("clearbtn.png");

        MscOffBtn = new Texture("mscoffbtn.png");
        MscOnBtn = new Texture("msconbtn.png");

        spx1on = new Texture("speedx1on.png");
        spx1off = new Texture("speedx1off.png");
        spx2on = new Texture("speedx2on.png");
        spx2off = new Texture("speedx2off.png");
        spx3on = new Texture("speedx3on.png");
        spx3off = new Texture("speedx3off.png");
        spx4on = new Texture("speedx4on.png");
        spx4off = new Texture("speedx4off.png");


        NewRecord = Gdx.audio.newSound(Gdx.files.internal("newRecord.mp3"));
        Oops = Gdx.audio.newSound(Gdx.files.internal("oops.mp3"));
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bgmusic.mp3"));

        bgMusic.setLooping(true);
        bgMusic.play();

        isPlaying = false;
        isMusicOn = true;
        lastTouchTime = TimeUtils.millis();
        player = new Player();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        if(GAME_SPEED == 150){
            game.batch.draw(spx1on,680,395,30,30);
            game.batch.draw(spx2off,680,355,30,30);
            game.batch.draw(spx3off,680,315,30,30);
            game.batch.draw(spx4off,680,275,30,30);
        }
        if(GAME_SPEED == 50){
            game.batch.draw(spx1off,680,395,30,30);
            game.batch.draw(spx2on,680,355,30,30);
            game.batch.draw(spx3off,680,315,30,30);
            game.batch.draw(spx4off,680,275,30,30);
        }
        if(GAME_SPEED == 10){
            game.batch.draw(spx1off,680,395,30,30);
            game.batch.draw(spx2off,680,355,30,30);
            game.batch.draw(spx3on,680,315,30,30);
            game.batch.draw(spx4off,680,275,30,30);
        }
        if(GAME_SPEED == 0){
            game.batch.draw(spx1off,680,395,30,30);
            game.batch.draw(spx2off,680,355,30,30);
            game.batch.draw(spx3off,680,315,30,30);
            game.batch.draw(spx4on,680,275,30,30);
        }

        font.draw(game.batch,"Record: " + record,130,455);
        font.draw(game.batch,"Current: " + Scores,130,65);
        font.draw(game.batch,"created by ecato games (c) 2017", 280,20);
        // AROUND
        for(int i = 0;i<27;i++) {
            game.batch.draw(WallImg,130 + i * 20,405,SCALE,SCALE);
            game.batch.draw(WallImg,130 + i * 20,85,SCALE,SCALE);
        }
        for(int i = 0; i< 15; i++){
            game.batch.draw(WallImg,130,105 + i * 20, SCALE,SCALE);
            game.batch.draw(WallImg,650,105 + i * 20, SCALE,SCALE);
        }
        game.batch.draw(ClearBtn,450,45); // CLEAR
        game.batch.draw(DoorImg,630,105,SCALE,SCALE);
        // AROUND AND DOOR IS THERE
        for(int i = 0; i < 25;i++){
            for(int j = 0; j< 15;j++)
            {
                if(GameField[i][j] == 1){
                    game.batch.draw(WallImg,i*20 + 150,j*20 + 105,SCALE,SCALE);
                }
            }
        } // GAME FIELD IS THERE

        if(isMusicOn){
            game.batch.draw(MscOnBtn,640,435,30,30);
        }
        else{
            game.batch.draw(MscOffBtn,640,435,30,30);
        }
        if(isPlaying) {
            game.batch.draw(StopBtn, 570, 45);
            if((TimeUtils.millis() - lastTurningTime > GAME_SPEED) && GAME_SPEED != 0)
            {
                Scores++;
                result = player.findWay(1);

                if (result == "down")
                {
                    playerY -=1;
                }

                if (result == "right")
                {
                    playerX +=1;
                }

                if (result == "up")
                {
                    playerY +=1;
                }

                if (result == "left")
                {
                    playerX -=1;
                }

                if (result == "good") {
                    isPlaying = false;
                    if (Scores > record) {
                        // NEW RECORD!
                        NewRecord.play();
                        record = Scores;
                        game.prefs.putInteger("record", Scores);
                        game.prefs.flush();
                    }
                } else // not good
                {
                    game.batch.draw(BugImg, playerX * 20 + 150, playerY * 20 + 105, SCALE, SCALE);
                    lastTurningTime = TimeUtils.millis();
                }
            }
            else{
                if(GAME_SPEED == 0) {
                    Scores = Integer.parseInt(player.findWay(3));
                    isPlaying = false;
                    if (Scores > record) {
                        // NEW RECORD!
                        NewRecord.play();
                        record = Scores;
                        game.prefs.putInteger("record", Scores);
                        game.prefs.flush();
                    }
                }
                game.batch.draw(BugImg, playerX * 20 + 150, playerY * 20 + 105, SCALE, SCALE);
            }
        }
        else{
            game.batch.draw(BugImg,150,385,SCALE,SCALE); // BUG
            game.batch.draw(StartBtn,570,45);
        }
        game.batch.end();


        if(Gdx.input.isTouched())
        {
            if(TimeUtils.millis() - lastTouchTime > 200)
            {
                touchedX = (int) (Gdx.input.getX() * ScreensConflictX);
                touchedY = (int) (480 - Gdx.input.getY() * ScreensColflictY);

                if(touchedX > 680 && touchedX < 710 && touchedY > 395 && touchedY < 425){ //SPEED X1
                    GAME_SPEED = 150;
                }
                if(touchedX > 680 && touchedX < 710 && touchedY > 355 && touchedY < 385){ // SPEED X2
                    GAME_SPEED = 50;
                }
                if(touchedX > 680 && touchedX < 710 && touchedY > 315 && touchedY < 345){ // SPEED X3
                    GAME_SPEED = 10;
                }
                if(touchedX > 680 && touchedX < 710 && touchedY > 275 && touchedY < 305){ // SPEED X4
                    GAME_SPEED = 0;
                }

                if(touchedX > 630 && touchedX < 660 && touchedY < 465 && touchedY > 435) // MUSIC ON/OFF
                {
                    if(isMusicOn) {
                        isMusicOn = false;
                        bgMusic.pause();
                    }
                    else{
                        isMusicOn = true;
                        bgMusic.play();
                    }
                }
                if (isPlaying)
                {
                    if (touchedX < 670 && touchedX > 570 && touchedY < 75 && touchedY > 45)  // STOP BUTTON
                    {
                        isPlaying = false;
                        if (Scores > record) {
                            // NEW RECORD!
                            NewRecord.play();
                            record = Scores;
                            game.prefs.putInteger("record", Scores);
                            game.prefs.flush();
                        }
                    }
                }
            else // DON'T PLAYING NOW
                {
                    if(touchedX < 550 && touchedX > 450 && touchedY <75 && touchedY > 45) // CLEAR
                    {
                        for(int i = 0; i < 25; i++){
                            for(int j = 0; j < 15;j++)
                                GameField[i][j] = 0;
                        }
                        game.prefs.putString("labirint",GenerateStringFromLabirint(GameField));
                        game.prefs.flush();
                    }
                    if (touchedX < 670 && touchedX > 570 && touchedY < 75 && touchedY > 45) // START BUTTON
                    {
                        Scores = 0;
                        player.GetReady();
                        playerX = 0;
                        playerY = 14;
                        player.Labirint = GameField;
                        isPlaying = true;
                    }

                    if(touchedX <= 650 && touchedX >= 630 && touchedY >=105 && touchedY <=125 ) // DOOR
                    {
                    Oops.play();
                    }
                    else
                    {
                        if(touchedX < 650 && touchedX > 150 && touchedY < 405 && touchedY > 105) // GAMEFIELD TOUCHED
                        {
                            if (touchedX >= 150 && touchedX <= 170 && touchedY >= 385 && touchedY <= 405) // BUG TOUCHED!
                            {
                                Oops.play();
                            }
                            else {
                                fieldX = (touchedX - 130) / 20 - 1;
                                fieldY = (touchedY - 85) / 20 - 1;
                                if (GameField[fieldX][fieldY] == 0) {
                                    player.GetReady();
                                    playerX = 0;
                                    playerY = 0;
                                    GameField[fieldX][fieldY] = 1;
                                    player.Labirint = GameField;
                                    if (player.findWay(2) != "good") {
                                        GameField[fieldX][fieldY] = 0;
                                        Oops.play();
                                    }
                                    game.prefs.putString("labirint",GenerateStringFromLabirint(GameField));
                                    game.prefs.flush();
                                }
                                else GameField[fieldX][fieldY] = 0;

                            }
                        }
                        }
                    }
                lastTouchTime = TimeUtils.millis();
                }
            }

        }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();

        WallImg.dispose();
        DoorImg.dispose();
        BugImg.dispose();

        StartBtn.dispose();
        StopBtn.dispose();
        ClearBtn.dispose();

        MscOnBtn.dispose();
        MscOffBtn.dispose();

        NewRecord.dispose();
        Oops.dispose();
        bgMusic.dispose();

        spx1off.dispose();
        spx1on.dispose();
        spx2off.dispose();
        spx2on.dispose();
        spx3off.dispose();
        spx3on.dispose();
        spx4off.dispose();
        spx4on.dispose();
    }
    private int [][] GetLabirint(String strLab)
    {
        int Labirint[][] = new int [25][15];
        String [] lines = strLab.split("\n");
        String [] line;
        for(int i = 0; i<25;i++){
            line = lines[i].split(" ");
            for(int j = 0; j< 15; j++){
                if(Integer.parseInt(line[j]) == 0)
                    Labirint[i][j] = 0;
                else
                    Labirint[i][j] = 1;
            }
        }
        return Labirint;
    }

    private String GenerateStringFromLabirint(int[][] Labirint)
    {
        String s = "";
        for(int i = 0; i< 25; i++){
            for( int j = 0; j< 15; j++){
                if(j != 14)
                s += Labirint[i][j] + " ";
                else
                    s += Labirint[i][j] + "";
            }
            s += "\n";
        }
        return s;
    }

}
