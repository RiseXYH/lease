package com.atguigu.lease.web.admin.custom.converter;

import com.atguigu.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Configuration;
@Configuration
public class StringToItemTypeConverter implements Converter<String, ItemType>{
    @Override
    public ItemType convert(String code) {
       /*
       其他写法
       if("1".equals(code)){
           return ItemType.APARTMENT;
       }else if("2".equals(code)){
           return ItemType.ROOM;
       }*/

//  其他写法：ItemType.class.getEnclosingClass();
        ItemType[] values = ItemType.values();
        for (ItemType value : values) {
            if(value.getCode().equals(Integer.valueOf(code))){
                return value;
            }
        }
        throw new IllegalArgumentException("code:"+code+"类型错误");
    }
}

