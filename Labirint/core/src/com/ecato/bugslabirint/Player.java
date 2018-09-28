package com.ecato.bugslabirint;


import com.badlogic.gdx.utils.TimeUtils;

public class Player {

    int X;
    int Y;
    long StartTime;
    boolean isRigthFree;
    boolean isDownFree;
    boolean isLeftFree;
    boolean isTopFree;
    boolean looping;
    int NumOfWays;
    int [][] WayNums;
    int CurrentWay;
    public int [][] Labirint;
    int [][] Paths;
    int Current;

    public Player(){
        Paths = new int[25][15];
        WayNums = new int[4][2];
        Current = 0;
    }
    public void GetReady(){
        X = 0;
        Y = 14;
        Paths = new int[25][15];
        Current = 0;
    }
    public String findWay(int State){
        if(State != 1)
            StartTime = TimeUtils.millis();
        looping = true;
        // STATE 1: ONLY ONE TURN
        // STATE 2 : TO THE END!
        // STATE 3: RETURNS NUMBER
        while (looping)
        {
            Current++;
            // 0 - down, 1 - right, 2 - top, 3 - left
            NumOfWays = 0;
            WayNums[0][0] = 228133700;
            WayNums[0][1] = 0;
            WayNums[1][0] = 228133700;
            WayNums[1][1] = 1;
            WayNums[2][0] = 228133700;
            WayNums[2][1] = 2;
            WayNums[3][0] = 228133700;
            WayNums[3][1] = 3;

            Paths[X][Y] += 1;


            if (State != 1) {
                if(TimeUtils.millis() - StartTime > 200) {
                       return "bad";
                }
            }

            if(Y > 0)
            {
                if(Labirint[X][Y-1] == 0)
                {
                    isDownFree = true;
                    WayNums[0][0] = Paths[X][Y-1];
                    NumOfWays++;
                }
            }
            else
                isDownFree = false;

            if(X < 24)
            {
                if(Labirint[X+1][Y] == 0)
                {
                    isRigthFree = true;
                    WayNums[1][0] = Paths[X+1][Y];
                    NumOfWays++;
                }
            }
            else
                isRigthFree = false;

            if(Y < 14)
            {
                    if (Labirint[X][Y + 1] == 0) {
                        isTopFree = true;
                        WayNums[2][0] = Paths[X][Y + 1];
                        NumOfWays++;
                    }
            }
            else
                isTopFree = false;

            if(X > 0)
            {
                if(Labirint[X-1][Y] == 0)
                {
                    isLeftFree = true;
                    WayNums[3][0] = Paths[X-1][Y];
                    NumOfWays++;
                }
            }
            else
                isLeftFree = false;

            if(NumOfWays == 0)
                continue;


            for(int i =0 ;i<3;i++)
            {
                for(int j = i+1;j<4;j++)
                {
                    if(WayNums[i][0] > WayNums[j][0]){
                        int temp1 = WayNums[i][0];
                        int temp2 = WayNums[i][1];
                        WayNums[i][0] = WayNums[j][0];
                        WayNums[i][1] = WayNums[j][1];
                        WayNums[j][0] = temp1;
                        WayNums[j][1] = temp2;
                    }
                }
            } // SORT

            if(WayNums[0][0] == WayNums[1][0]){
                if(WayNums[0][1] < WayNums[1][1]) {
                    CurrentWay = WayNums[0][1];
                }
                else {
                    CurrentWay = WayNums[1][1];
                }
            }
            else
                CurrentWay = WayNums[0][1];


            // 0 - down, 1 - right, 2 - top, 3 - left
            if(CurrentWay == 0){
                Y --;
                if(X == 24 && Y == 0){
                    if(State == 3){
                        return Current + "";
                    }
                    else
                    return "good";
                }
                else
                    if(State == 1){
                        return "down";
                    }
                continue;
            }
            if(CurrentWay == 1){
                X++;
                if(X == 24 && Y == 0){
                    if(State == 3){
                        return Current + "";
                    }
                    else
                    return "good";
                }
                else
                if(State == 1){
                    return "right";
                }
                continue;
            }
            if(CurrentWay == 2){
                Y++;
                if(X == 24 && Y == 0){
                    if(State == 3){
                        return Current + "";
                    }
                    else
                    return "good";
                }
                else
                if(State == 1){
                    return "up";
                }
                continue;
            }
            if(CurrentWay == 3){
                X--;
                if(X == 24 && Y == 0){
                    if(State == 3){
                        return Current + "";
                    }
                    else
                    return "good";
                }
                else
                if(State == 1){
                    return "left";
                }
                continue;
            }
        }
        return "bad";
    }
}
