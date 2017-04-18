package com.cheng.spider;

import org.junit.Test;

/**
 * Desc:
 * Author: hp
 * Date: 2017/4/18
 */
public class TestAll {

    public static final int CASE_INSENSITIVE = 0x02;

    public static final int DOTALL = 0x20;

    @Test
    private static void test01() {
        System.out.println(CASE_INSENSITIVE|DOTALL);
    }

    public static void main(String[] args) {
        test01();
    }
}
