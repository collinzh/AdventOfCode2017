package me.collinzhang.support;

import java.io.InputStream;
import java.util.Scanner;

public class Util {

    public static InputStream openResource(String name) {
        return Util.class.getResourceAsStream(name);
    }

    public static Scanner openScanner(String name) {
        return new Scanner(openResource(name));
    }
}
