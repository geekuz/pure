package com.geekuz.comparator;

import java.util.Comparator;

import static java.lang.Math.signum;

public class ComparatorMain {

    public static void main(String[] args) {
        Comparator<User> comparator= Comparator.comparingDouble(User::getAge).thenComparing(User::getName);



    }

    private static class ByScoreComparator implements Comparator<User> {

        @Override
        public int compare(User u1, User u2) {
            return (int) signum(u2.getAge()-u1.getAge());
        }
    }

    private static class User {
        private double age;
        private String name;

        public double getAge() {
            return age;
        }

        public String getName() {
            return name;
        }
    }
}
