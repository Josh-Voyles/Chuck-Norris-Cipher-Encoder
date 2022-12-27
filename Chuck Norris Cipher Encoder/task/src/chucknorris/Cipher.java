package chucknorris;

import java.util.Objects;

public class Cipher {
    // useful for controlling state of program
    private State status = State.MENU;

    // to store entire message is binary
    private final StringBuilder new_binary = new StringBuilder(); // e.g 10010001101001010000001111000110011
    // used to store 7 bit sets of binary
    private final StringBuilder block  = new StringBuilder();  // e.g. 1000011
    // used to store final decoded output; prints to console
    private final StringBuilder decoded = new StringBuilder();  // e.g. Thanks for reading my code!

    // empty constructor since we don't need to assign values on object creation
    public Cipher() {
    }
    // simply a prompt to menu when needed
    void menu() {
        System.out.println("\nPlease input operation (encode/decode/exit):");
    }
    // in while loop to keep program running
    boolean on() {
        return !Objects.equals(status, State.EXIT);
}
    // main switchboard for the menu options, loops through here several times for each method.
    void run(String input) {
        if (on()) {
            if (status == State.MENU) {
                // makes sure user inputs a valid operation
                boolean check = Objects.equals(input, "encode") || Objects.equals(input, "decode")
                        || Objects.equals(input, "exit");
                if (!check) {
                    // states error and returns to menu
                    System.out.printf("There is no '%s' operation", input); // outputs incorrect input
                    menu();
                    return;
                }
                status = State.getState(input);  // ENCODE, DECODE, or EXIT
                // if user selections exit, there is no need to print the messages below
                if (!input.equals("exit")) {
                    String message = Objects.equals(status, State.ENCODE) ? "Input string:" : "Input encoded String:";
                    System.out.println(message); // one of the two options in the line above
                    return;
                }
            }
            switch (status) {
                case ENCODE -> {
                    System.out.println("Encoded string:");
                    // converts user input to ones and zeros
                    convertToBinaryString(input);
                    // concerts to chuck norris method of encoding (all zeros)
                    encoderChuckNorris(new_binary.toString());
                    // return String builder to zero for next round
                    new_binary.setLength(0);
                    // back to menu
                    setStatus(State.MENU);
                    menu();
                }
                case DECODE -> {
                    try {
                        // this parsing check ensure that the input is all digits; throws error
                        Integer.parseInt(input.replaceAll("\\s", ""));
                        // we need to let the methods run first to see if they throw an error
                        // decodes back to binary
                        decoderChuckNorris(input);
                        // converts to ASCII String
                        convertFromBinaryString(new_binary);
                        System.out.println("Decoded string:");
                        System.out.println(decoded);
                        // return String builder to zero for next round
                        new_binary.setLength(0);
                        setStatus(State.MENU);

                    } catch (Exception e) {  // of user inputs 'hello', 00011100, or non convertable to 7 bit string
                        System.out.println("Encoded string is not valid.");
                        // making sure all of our String Builders are empty if we throw an error
                        block.setLength(0);
                        new_binary.setLength(0);
                        decoded.setLength(0);
                        setStatus(State.MENU);
                    }
                    menu();
                }
                case EXIT -> {
                    setStatus(State.EXIT);
                    System.out.println("Bye!");
                }

            }
        }
    }
    // changes status, called in each menu option method
    private void setStatus(State status) {
        this.status = status;
    }
    private void decoderChuckNorris(String input) {
        for (int j = 0; j < input.length();) {
            int count = 0;  // variable used to count zeros
            boolean iterate;  // will determine if we continue counting or not
            int x = 0;

            // single or double zero?
            boolean zero = input.charAt(j) == '0' && input.charAt(j + 1) == '0';
            int oneorzero = zero ? 0 : 1;

            switch (oneorzero) {
                case 0 -> {
                    j += 3; // we need to move to the start of zeros past space
                    do {
                        // will return false once it hits a space
                        iterate = x + j < input.length() && input.charAt(x + j) == '0';
                        // we removed condition from encoder since we need to move 1 more past space
                        ++count;
                        x++;
                    } while (iterate);
                    // -1 count for append since we added the space to count
                    new_binary.append("0".repeat(count - 1));
                    // adds count to j, so we can move on from where we stopped counting
                    j += count;
                }
                case 1 -> {
                    j += 2;
                    do {
                        // will return false once it hits a space
                        iterate = x + j < input.length() && input.charAt(x + j) == '0';
                        // we removed condition from encoder since we need to move 1 more past space
                        ++count;
                        x++;
                    } while (iterate);
                    // -1 count for append since we added the space to count
                    new_binary.append("1".repeat(count - 1));
                    j += count;
                }
            }
        }
    }
    private void encoderChuckNorris(String bitstring) {
        for (int j = 0; j < bitstring.length(); ) {

            int count = 0; // variable used to count series of ones or zeros
            boolean iterate;
            int x = 0; // variable to increment in do while loop

            // inspects every character in the string
            switch (bitstring.charAt(j)) {
                case '0' -> {
                    // prints identifier "00 " if '0'
                    System.out.print("0" + "0" + " ");
                    do {
                        iterate = x + j < bitstring.length() && bitstring.charAt(x + j) == '0';
                        count = iterate ? ++count : count; // will count as long as iterate is true
                        x++;
                    } while (iterate);
                    // adds count to j, so we can move on from where we stopped counting
                    j += count;
                }

                case '1' -> {
                    // prints identifier "0 " if '1'
                    System.out.print("0" + " ");
                    do {
                        iterate = x + j < bitstring.length() && bitstring.charAt(x + j) == '1';
                        count = iterate ? ++count : count; // will count as long as iterate is true
                        x++;
                    } while (iterate);
                    // adds count to j, so we can move on from where we stopped counting
                    j += count;
                }
            } // end of switch

            // prints number of ones or zeros
            for (int q = 0; q < count; q++) {
                System.out.print("0");
            }
            // if we haven't reached the end, we make a space for the next zero
            if (j < bitstring.length()) {
                System.out.print(" ");
            }
        }
    }
    private void convertToBinaryString(String input) {
        // prints character and binary value for each character in string
        for (int i = 0; i < input.length(); i++) {
            // converts character to hexadecimal
            String hex = String.format("%h", input.charAt(i));
            // turns hex into unicode number
            int unicode_int = Integer.parseInt(hex, 16);
            // turns unicode into binary
            String binary = Integer.toBinaryString(unicode_int);
            // ensures binary is 7 bits long
            binary = String.format("%7s", binary);
            // adds zeros onto the front if shorter than 7 bits
            binary = binary.replaceAll(" ", "0");

            new_binary.append(binary);

        }
    }
    private void convertFromBinaryString(StringBuilder new_binary) {
        for (int i = 0; i < new_binary.length();) {
            for (int j = 0; j < 7; j++) {
                // since we know the text is 7 bits, we can divide the 1's and 0's into 7 character blocks
                block.append(new_binary.charAt(j + i));
            }
            // we need to make sure the user inputs a valid input
            if (new_binary.length() < 7) {
                throw new RuntimeException();
            }
            // String builder to String
            String binary = block.toString();
            // converts binary to ASCII decimal number
            int ch_code = Integer.parseInt(binary, 2);
            // converts ASCII decimal number to character
            char ch = (char)ch_code;
            // add characters to String Builder
            decoded.append(ch);
            // move 7 spaces forward to the next block of 7
            i += 7;
            // empties String builder for next block
            block.setLength(0);
        }

    }
}


