package space.bobcheng.myapplication;

import java.util.HashMap;

/**
 * Created by bob on 17-3-31.
 */

public class NumberMap {
    public static final HashMap<Integer, String> map = new HashMap<Integer, String>(){
        {
            for (int i = 1; i <= 31 ; i++) {
                if(i >= 1 && i <= 9)
                    put(i, "0"+i);
                else
                    put(i, ""+i);
            }
        }
    };
}
