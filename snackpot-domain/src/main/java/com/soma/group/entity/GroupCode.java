package com.soma.group.entity;

import java.util.Random;

public class GroupCode {
    private static Random random = new Random();
    private static int leftLimit = 48; // numeral '0'
    private static int rightLimit = 57; // numeral '9'
    private static int targetStringLength = 6;

    public static String create6Number(){
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
