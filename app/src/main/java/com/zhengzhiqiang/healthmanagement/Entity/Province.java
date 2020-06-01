package com.zhengzhiqiang.healthmanagement.Entity;

import java.util.List;
import com.contrarywind.interfaces.IPickerViewData;

public class Province implements IPickerViewData{
        public String name;
        public List<City> city;
        public static class City{
            public String name;
            public List<String> area;

        }
        //  这个要返回省的名字
        @Override
        public String getPickerViewText() {
            return this.name;
        }
}
