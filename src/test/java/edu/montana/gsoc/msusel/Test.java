package edu.montana.gsoc.msusel;

public class Test {

    public static void main(String args[]) {
        boolean flag = true;

        try {
            if (flag) {
                while (true) { }
            } else {
                System.exit(1);
            }
        } finally {
            System.out.println("In Finally");
        }
    }
}
