package com.mall;

import com.mall.util.BigDecimalUtil;
import org.junit.Test;

import java.math.BigDecimal;

public class Junit {
    @Test
    public  void test1(){
        //BigDecimal在金融处理方面精度缺失的情况，传入String类型即可。
        System.out.println(0.1+0.2);
        System.out.println(0.1*0.2);
        System.out.println(6/2.0);
    }
    @Test
    public  void test2(){
        System.out.println(BigDecimalUtil.add(0.02,1.02));
        System.out.println(BigDecimalUtil.mul(0.1,0.2));
        System.out.println(BigDecimalUtil.div(6,2));

//        1.0220000000000000195399252334027551114559173583984375
//        21.0000050000000015870682545937597751617431640625
//        5
    }
    @Test
public  void test3(){
    System.out.println(new BigDecimal("0.002"));
    System.out.println(new BigDecimal("1.000005"));
    System.out.println(new BigDecimal("5.000000000000002154"));
}
}
