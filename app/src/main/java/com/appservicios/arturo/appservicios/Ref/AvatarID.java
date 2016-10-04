package com.appservicios.arturo.appservicios.Ref;

import com.appservicios.arturo.appservicios.R;

import java.util.Random;


public class AvatarID {

    private static Random randomAvatarGenerator = new Random();
    private static int    NUMBER_OF_AVATAR=3;

    /*Generate an avatar randomly*/
    public static int  generateRandomAvatarForUser(){
        return randomAvatarGenerator.nextInt(NUMBER_OF_AVATAR);
    }

    /*Get avatar id*/

    public static int getDrawableAvatarId(int givenRandomAvatarId){

        switch (givenRandomAvatarId){

            case 0:
                return R.mipmap.ic_avatar_blue;
            case 1:
                return R.mipmap.ic_avatar_green;
            case 2:
                return R.mipmap.ic_avatar_purple;
            default:
                return R.mipmap.ic_avatar_purple;
        }
    }


}
