package com.test.acremote;

import java.util.concurrent.TimeUnit;

public class Util
{
    /////////////////////Delay//////////////////////////////

    public static void Delay(int duration) {
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DelayMili(int duration) {
        try {
            TimeUnit.MILLISECONDS.sleep(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
