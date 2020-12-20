package pl.pwsztar.light;


import android.graphics.Color;

public class status {

    public static boolean ledStatus = false;
    public static int r, g, b;
    public static void reset()
    {
        ledStatus = false;
        r = 0;
        g = 0;
        b = 0;
    }

}