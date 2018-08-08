package com.rust.aboutview.algochart.algo;


public class BaseSort {

    public static void exch(int a[], int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /**
     * v < w 返回 true
     */
    public static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0; //v < w 返回 -1
    }
}
