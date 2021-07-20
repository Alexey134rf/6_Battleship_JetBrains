package com.company;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;

public class Battleship {
    private static final int SIZE_GAME_MAP = 10;
    private static Scanner sc = new Scanner(System.in);

    public static void main (String[] args) {
       startProgram();
    }

    private static void startProgram() {
        System.out.println("Player 1, place your ships on the game field");
        char[][] playMap1 = createPlayMap();
        printGameMap(playMap1, true);
        deliverAllShip(playMap1);
        clearScreen();

        System.out.println("Player 2, place your ships on the game field");
        char[][] playMap2 = createPlayMap();
        printGameMap(playMap2, true);
        deliverAllShip(playMap2);
        clearScreen();

        char[][] playMapFogOfWar1 = createPlayMap();
        char[][] playMapFogOfWar2 = createPlayMap();
        while(true) {
            printGameMap(playMapFogOfWar2, false);
            System.out.print("\n---------------------");
            printGameMap(playMap1, true);
            System.out.println("\nPlayer 1, it's your turn:\n");
            attackOnShip(playMap2, playMapFogOfWar2);
            if (checkAllSankShips(playMap2)) {
                System.out.println("\nPlayer 1 won");
                break;
            }
            clearScreen();

            printGameMap(playMapFogOfWar1, false);
            System.out.print("\n---------------------");
            printGameMap(playMap2, true);
            System.out.println("\nPlayer 2, it's your turn:\n");
            attackOnShip(playMap1, playMapFogOfWar1);
            if (checkAllSankShips(playMap1)) {
                System.out.println("\nPlayer 2 won");
                break;
            }
            clearScreen();

        }
    }

    private static char[][] createPlayMap() {
        char[][] playMap = new char[SIZE_GAME_MAP][SIZE_GAME_MAP];
        for (int i = 0; i < SIZE_GAME_MAP; i++) {
            for (int j = 0; j < SIZE_GAME_MAP; j++) {
                playMap[i][j] = '~';
            }
        }
        return playMap;
    }

    private static void printGameMap(char[][] playMap, boolean space) {
        char symbol = 65;

        for (int i = 0; i < 11; i ++) {
            if (i == 0) {
                System.out.print("\n  ");
            } else {
                System.out.print(i + " ");
            }
        }

        for (int i = 0; i < SIZE_GAME_MAP; i++) {
            System.out.println("");
            System.out.print(symbol + " ");
            symbol+= 1;
            for (int j = 0; j < SIZE_GAME_MAP; j++) {
                System.out.print(playMap[i][j] + " ");
            }
        }
        if (space) {
            System.out.println("\n");
        }

    }

    private static void clearScreen() {
        System.out.println("Press Enter and pass the move to another player");
        String tmp = sc.nextLine();
    }

    private static void deliverAllShip(char[][] playMap) {
        deliverShip(playMap, 5, false);
        deliverShip(playMap, 4, false);
        deliverShip(playMap, 3, true);
        deliverShip(playMap, 3, false);
        deliverShip(playMap, 2, false);
    }

    private static int[] readUserCoordinate() {
        boolean flag = true;
        String[] userCoordinate = null;
        int[] userCoordinateToInt = new int[4];

        while (flag) {
            userCoordinate = sc.nextLine().split("\\s");
            int isNumber = 0;
            flag = userCoordinate.length == 2 ? true : false;

            for (int i = 0; i < 2 && flag; i++) {
                flag = flag && userCoordinate[i].charAt(0) >= 65 && userCoordinate[i].charAt(0) <= 74;
                if (userCoordinate[i].substring(1).matches("(\\d+)")) {
                    isNumber = Integer.parseInt(userCoordinate[i].substring(1));
                    flag = flag && isNumber > 0 && isNumber < 11;
                } else {
                    flag = false;
                }
            }

            if (!flag) {
                System.out.println("\nError! Wrong ship location! Try again:\n");
                flag = true;
            } else {
                userCoordinateToInt[0] =  userCoordinate[0].charAt(0) - 65;
                userCoordinateToInt[1] =  Integer.parseInt(userCoordinate[0].substring(1)) -1;
                userCoordinateToInt[2] =  userCoordinate[1].charAt(0) - 65;
                userCoordinateToInt[3] =  Integer.parseInt(userCoordinate[1].substring(1)) -1;
                flag = false;
            }
        }
        return userCoordinateToInt;
    }
    private static int[] readUserCoordinateHitShip() {
        boolean flag = true;
        String[] userCoordinate = null;
        int[] userCoordinateToInt = new int[2];

        while (flag) {
            userCoordinate = sc.nextLine().split("\\s");
            int isNumber = 0;

            if (userCoordinate.length == 1) {
                flag = flag && userCoordinate[0].charAt(0) >= 65 && userCoordinate[0].charAt(0) <= 74;
            }

            if (userCoordinate[0].substring(1).matches("(\\d+)")) {
                isNumber = Integer.parseInt(userCoordinate[0].substring(1));
                flag = flag && isNumber > 0 && isNumber < 11;
            } else {
                flag = false;
            }

            if (!flag) {
                System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
                flag = true;
            } else {
                userCoordinateToInt[0] = userCoordinate[0].charAt(0) - 65;
                userCoordinateToInt[1] = Integer.parseInt(userCoordinate[0].substring(1)) - 1;
                flag = false;
            }
        }

        return userCoordinateToInt;
    }

    private static void deliverShip (char[][] playMap, int numberOfShipCells, boolean submarineTrue) {
        int[] userCoordinate = null;

        switch (numberOfShipCells) {
            case 5:
                System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):\n");
                while (true) {
                    userCoordinate = readUserCoordinate();
                    if (!chekShipLocation(userCoordinate)) {
                        System.out.println("\nError! Wrong ship location! Try again:\n");
                        continue;
                    }
                    if (!chekNumberOfShipCells(userCoordinate,numberOfShipCells)) {
                        System.out.println("\nError! Wrong length of the Aircraft Carrier! Try again: \n");
                        continue;
                    }
                    if (!chekPlacementShip(playMap, userCoordinate)) {
                        System.out.println("\nError! You placed it too close to another one. Try again:\n");
                        continue;
                    }
                    placeShip(playMap, userCoordinate);
                    printGameMap(playMap, true);
                    Ships  aircraftCarrier = Ships.AIRCRAFT_CARRIER;
                    aircraftCarrier.shipCoordinate = userCoordinate;
                    break;
                }
                break;
            case 4:
                System.out.println("Enter the coordinates of the Battleship (4 cells):\n");
                while (true) {
                    userCoordinate = readUserCoordinate();
                    if (!chekShipLocation(userCoordinate)) {
                        System.out.println("\nError! Wrong ship location! Try again:\n");
                        continue;
                    }
                    if (!chekNumberOfShipCells(userCoordinate,numberOfShipCells)) {
                        System.out.println("\nError! Wrong length of the Battleship! Try again: \n");
                        continue;
                    }
                    if (!chekPlacementShip(playMap, userCoordinate)) {
                        System.out.println("\nError! You placed it too close to another one. Try again:\n");
                        continue;
                    }
                    placeShip(playMap, userCoordinate);
                    printGameMap(playMap, true);
                    Ships  battleship = Ships.BATTLESHIP;
                    battleship.shipCoordinate = userCoordinate;
                    break;
                }
                break;
            case 3:
                System.out.println(submarineTrue ? "Enter the coordinates of the Submarine (3 cells):\n" : "Enter the coordinates of the Cruiser (3 cells):\n");
                while (true) {
                    if (submarineTrue) {

                        userCoordinate = readUserCoordinate();
                        if (!chekShipLocation(userCoordinate)) {
                            System.out.println("\nError! Wrong ship location! Try again:\n");
                            continue;
                        }
                        if (!chekNumberOfShipCells(userCoordinate,numberOfShipCells)) {
                            System.out.println("\nError! Wrong length of the Submarine! Try again: \n");
                            continue;
                        }
                    } else {
                        userCoordinate = readUserCoordinate();
                        if (!chekShipLocation(userCoordinate)) {
                            System.out.println("\nError! Wrong ship location! Try again:\n");
                            continue;
                        }
                        if (!chekNumberOfShipCells(userCoordinate,numberOfShipCells)) {
                            System.out.println("\nError! Wrong length of the Cruiser! Try again: \n");
                            continue;
                        }
                    }

                    if (!chekPlacementShip(playMap, userCoordinate)) {
                        System.out.println("\nError! You placed it too close to another one. Try again:\n");
                        continue;
                    }
                    placeShip(playMap, userCoordinate);
                    printGameMap(playMap, true);
                    if (submarineTrue) {
                        Ships  submarine = Ships.SUBMARINE;
                        submarine.shipCoordinate = userCoordinate;
                    } else {
                        Ships  cruiser = Ships.CRUISER;
                        cruiser.shipCoordinate = userCoordinate;
                    }
                    break;
                }
                break;
            case 2:
                System.out.println("Enter the coordinates of the Destroyer (2 cells):\n");
                while (true) {
                    userCoordinate = readUserCoordinate();
                    if (!chekShipLocation(userCoordinate)) {
                        System.out.println("\nError! Wrong ship location! Try again:\n");
                        continue;
                    }
                    if (!chekNumberOfShipCells(userCoordinate,numberOfShipCells)) {
                        System.out.println("\nError! Wrong length of the Destroyer! Try again: \n");
                        continue;
                    }
                    if (!chekPlacementShip(playMap, userCoordinate)) {
                        System.out.println("\nError! You placed it too close to another one. Try again:\n");
                        continue;
                    }
                    placeShip(playMap, userCoordinate);
                    printGameMap(playMap, true);
                    Ships  destroyer = Ships.DESTROYER;
                    destroyer.shipCoordinate = userCoordinate;
                    break;
                }
                break;
        }
    }
    private static boolean chekNumberOfShipCells(int[] userCoordinate, int numberOfShipCells) {
        return Math.abs(userCoordinate[0] - userCoordinate[2] + userCoordinate[1] - userCoordinate[3]) == numberOfShipCells - 1 ? true : false;

    }
    private static boolean chekShipLocation(int[] userCoordinate) {
        return userCoordinate[0] == userCoordinate[2] || userCoordinate[1] == userCoordinate[3] ? true : false;

    }
    private static boolean chekPlacementShip(char[][] playMap, int[] userCoordinate) {
        boolean flag = true;
        int i = 0;
        int condition = 0;
        int j = 0;
        int condition1 = 0;

        if (userCoordinate[0] == userCoordinate[2]) {
            i = userCoordinate[1] < userCoordinate[3] ? userCoordinate[1] : userCoordinate[3];
            condition = i + Math.abs(userCoordinate[1] - userCoordinate[3]);
            j = i - 1 >= 0 ? i - 1 : i;
            condition1 = condition + 1 <= 9 ? condition + 1 : condition;

            for (; j <= condition1 && flag && userCoordinate[0] - 1 >= 0; j++) {
                flag = flag && playMap[userCoordinate[0] - 1][j] == '~';
            }
            j = i - 1 >= 0 ? i - 1 : i;
            for (; j <= condition1 && flag; j++) {
                flag = flag && playMap[userCoordinate[0]][j] == '~';
            }
            j = i - 1 >= 0 ? i - 1 : i;
            for (; j <= condition1 && flag && userCoordinate[0] + 1 <= 9; j++) {
                flag = flag && playMap[userCoordinate[0] + 1][j] == '~';
            }
        } else {
            i = userCoordinate[0] < userCoordinate[2] ? userCoordinate[0] : userCoordinate[2];
            condition = i + Math.abs(userCoordinate[0] - userCoordinate[2]);
            j = i - 1 >= 0 ? i - 1 : i;
            condition1 = condition + 1 <= 9 ? condition + 1 : condition;

            for (; j <= condition1 && userCoordinate[1] - 1 >= 0; j++) {
                flag = flag && playMap[j][userCoordinate[1] - 1] == '~';
            }
            j = i - 1 >= 0 ? i - 1 : i;
            for (; j <= condition1; j++) {
                flag = flag && playMap[j][userCoordinate[1]] == '~';
            }
            j = i - 1 >= 0 ? i - 1 : i;
            for (; j <= condition1 && userCoordinate[1] + 1 <= 9; j++) {
                flag = flag && playMap[j][userCoordinate[1] + 1] == '~';
            }
        }
        return flag;
    }
    private static void placeShip(char[][] playMap, int[] userCoordinate) {
        int i = 0;
        int condition = 0;

        if (userCoordinate[0] == userCoordinate[2]) {
            i = userCoordinate[1] < userCoordinate[3] ? userCoordinate[1] : userCoordinate[3];
            condition = i + Math.abs(userCoordinate[1] - userCoordinate[3]);

            for (; i <= condition; i++) {
                playMap[userCoordinate[0]][i] = 'O';
            }
        } else {
            i = userCoordinate[0] < userCoordinate[2] ? userCoordinate[0] : userCoordinate[2];
            condition = i + Math.abs(userCoordinate[0] - userCoordinate[2]);

            for (; i <= condition; i++) {
                playMap[i][userCoordinate[1]] = 'O';
            }
        }
    }

    private static void attackOnShip(char[][] playMap, char[][] playMapFogOfWar) {
        int[] userCoordinate = null;

        userCoordinate = readUserCoordinateHitShip();
        if (playMap[userCoordinate[0]][userCoordinate[1]] == '~') {
            playMap[userCoordinate[0]][userCoordinate[1]] = 'M';
            playMapFogOfWar[userCoordinate[0]][userCoordinate[1]] = 'M';
            System.out.println("You missed\n");
        } else if (playMap[userCoordinate[0]][userCoordinate[1]] == 'O') {
            playMap[userCoordinate[0]][userCoordinate[1]] = 'X';
            playMapFogOfWar[userCoordinate[0]][userCoordinate[1]] = 'X';
            if (checkAllSankShips(playMap)) {
                System.out.println("\nYou sank the last ship. You won. Congratulations!");
            } else if (checkShipSunk(playMap)) {
                System.out.println("\nYou sank a ship! Specify a new target:");

            }
            else {
                System.out.println("\nYou hit a ship!");
            }
        }


    }
    private static boolean checkAllSankShips(char[][] playMap) {
        boolean flag = true;
        for (int i = 0; i < SIZE_GAME_MAP && flag; i++) {
            for (int j = 0; j < SIZE_GAME_MAP ; j++) {
                if (playMap[i][j] == 'O') {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }
    enum Ships {
        AIRCRAFT_CARRIER(new int[]{}, true),
        BATTLESHIP(new int[]{}, true),
        SUBMARINE(new int[]{}, true),
        CRUISER(new int[]{}, true),
        DESTROYER(new int[]{}, true);

        private int[] shipCoordinate;
        private boolean ship;

        Ships(int[] shipCoordinate, boolean ship) {
            this.shipCoordinate = shipCoordinate;
            this.ship = ship;
        }
    }
    private static boolean checkShipSunk(char[][] playMap) {

        for (Ships ships : Ships.values()) {
            int i = 0;
            int condition = 0;
            boolean flag = true;
            if (ships.ship) {
                if (ships.shipCoordinate[0] == ships.shipCoordinate[2] && ships.ship) {
                    //System.out.println(Arrays.toString(ships.shipCoordinate));
                    // System.out.println(ships.shipCoordinate[0] + " " + ships.shipCoordinate[2]);
                    i = ships.shipCoordinate[1] < ships.shipCoordinate[3] ? ships.shipCoordinate[1] : ships.shipCoordinate[3];
                    condition = i + Math.abs(ships.shipCoordinate[1] - ships.shipCoordinate[3]);

                    for (; i <= condition; i++) {
                        flag = flag && playMap[ships.shipCoordinate[0]][i] == 'X';
                    }
                    if (flag) {
                        ships.ship = false;
                        return flag;
                    }
                } else if (ships.shipCoordinate[1] == ships.shipCoordinate[3] && ships.ship) {
                    i = ships.shipCoordinate[0] < ships.shipCoordinate[2] ? ships.shipCoordinate[0] : ships.shipCoordinate[2];
                    condition = i + Math.abs(ships.shipCoordinate[0] - ships.shipCoordinate[2]);

                    for (; i <= condition; i++) {
                        flag = flag && playMap[i][ships.shipCoordinate[0]] == 'X';
                    }
                    if (flag) {
                        ships.ship = false;
                        return flag;
                    }

                }
            }

        }
        return false;
    }
 }




