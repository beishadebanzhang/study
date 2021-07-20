package com.sunsy.nio.day01;

public class Test {
    public static void main(String[] args) {

        String str = null;
        String url = new StringBuffer().append("&contactId=").append(String.valueOf(str))
            .toString();
        System.out.println(url);

    }
}
