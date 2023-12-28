package com.sch.crane.cranewebbackend_v2.Util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomProvider {

    private final static int LEFT_LIMIT = 48;
    private final static int RIGHT_LIMIT = 122;

    public String RandomNumCharStringProvider(int length){
        Random random = new Random();
        String randString = random.ints(LEFT_LIMIT, RIGHT_LIMIT + 1 )
                .filter(i -> (i <= 57 || i > 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return randString;
    }
}
