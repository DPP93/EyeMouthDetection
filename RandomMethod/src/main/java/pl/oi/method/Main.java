package pl.oi.method;

import java.io.IOException;

/**
 * Jako args do metody musi być podana ścieżka do folderu z bazą
 * Created by dpp on 5/5/16.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Random method start");

        Method method = new Method(args[0], args[1]);
        method.learn();
        method.testMethod();
    }
}
