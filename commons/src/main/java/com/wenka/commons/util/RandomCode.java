package com.wenka.commons.util;

import java.util.Random;

/**
 * 获取随机数
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/10.
 */
public class RandomCode {

    private static final int LENGTH = 6;

    public static String getRandomCode(int length){
        String code = "";

        Random random = new Random();
        for (int i = 0; i < length; i++){
            code += random.nextInt(10);
        }
        return code;
    }

    public static String getRandomCode(){
        return getRandomCode(LENGTH);
    }

    public static void main(String[] args){
        System.out.print(getRandomCode());
    }
}
