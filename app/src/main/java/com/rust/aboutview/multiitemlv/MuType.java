package com.rust.aboutview.multiitemlv;

/**
 * multi type
 * Created by Rust on 2018/5/31.
 */
public enum MuType {
    ONE_LINE(0),
    PIC_AND_ONE_LINE(1),
    ONE_PIC(2),
    BANNER(3);

    int code;

    MuType(int c) {
        code = c;
    }
}
