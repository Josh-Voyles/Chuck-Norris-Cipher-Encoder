package chucknorris;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // new cipher object
        Cipher cipher = new Cipher();
        // pulls up cipher menu
        cipher.menu();
        // continual loop until users 'shuts the program off'
        while (cipher.on()) {
            cipher.run(in.nextLine()); // accepts user input into switchboard
        }
    }
}


