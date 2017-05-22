package com.passwordboss.android.utils;

import com.passwordboss.android.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Boris on 07/06/16.
 */
public class AvatarResourceMap extends HashMap<String, Integer> {
    @Override
    public Integer get(Object avatar) {
        if(containsKey(avatar)) {
            return super.get(avatar);
        } else {
            return R.drawable.ic_01_big;
        }
    }
}
