package com.tensquare.test;

import org.junit.Test;

import java.lang.ref.WeakReference;

/**
 * @author daixiaoyong
 * @date 2021/4/2 17:07
 * @description reference测试
 */
public class ReferenceTest {

    @Test
    public void test() {
        Object object1 = new Object();
        WeakReference<Object> weakReference = new WeakReference<>(object1);
        System.out.println(object1);
        System.out.println(weakReference.get());
        object1 = null;
        System.gc();

        System.out.println("=====================================");
        System.out.println(object1);
        System.out.println(weakReference.get());
    }
}