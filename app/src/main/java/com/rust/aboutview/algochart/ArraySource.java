package com.rust.aboutview.algochart;

import java.util.Random;

public class ArraySource {

    private static final int DEF_ARR_SIZE = 15;

    public static int[] genIntArray() {
        return genIntArray(DEF_ARR_SIZE);
    }

    public static int[] genIntArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        randomMix(arr);
        return arr;
    }

    private static void randomMix(int[] arr) {
        Random random = new Random();
        int len = arr.length;
        for (int i = 1; i < len; i++) {
            int targetIndex = random.nextInt(len);
            int tmp = arr[i];
            arr[i] = arr[targetIndex];
            arr[targetIndex] = tmp;
        }
    }

}
