package com.cinsc.MainView.utils.convert;

import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 束手就擒
 * @Date: 18-10-11 下午5:37
 * @Description:
 */
@Slf4j
public class MapTurnPojo {

    public static Map<String,Object> object2Map(Object obj) {
        Map<String,Object> map = new HashMap<>();
        if (obj == null){
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(),field.get(obj));
            } catch (IllegalAccessException e) {
                log.info("[获得我执行的安排] MapTurnPojo erro");
                throw new SystemException(ResultEnum.DATA_ERROR);
            }
        }
        return map;
    }
    public static Object map2Object(Map<String,Object> map, Class<?> clazz){
        if (map == null){
            return null;
        }
        Object obj = null;

        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }
                field.setAccessible(true);
                field.set(obj,map.get(field.getName()));
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
