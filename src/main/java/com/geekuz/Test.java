package com.geekuz;

import java.sql.DriverManager;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        System.out.println("Classloader of this class:"
                + Main.class.getClassLoader());

        System.out.println("Classloader of DriverManager:"
                + DriverManager.class.getClassLoader());

        System.out.println("Classloader of ArrayList:"
                + ArrayList.class.getClassLoader());
    }

}
