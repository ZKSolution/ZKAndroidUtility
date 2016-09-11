package com.zklogic.androidutility.model;

import android.content.Context;

/**
 * Created by ArfanMirza on 8/24/2015.
 */
public class ZKConstant {

    private static ZKConstant instance;


    private ZKConstant(Context context){

    }

    public static ZKConstant getInstance(Context context) {
        if(instance == null){
            instance = new ZKConstant(context);
        }
        return instance;
    }


}
