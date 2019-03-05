package com.mall;

import java.util.Arrays;

public class classTest {
    public static  void main (String args[]){
        int count1=0;
        for (int i=0;i<=1000;i++){
           if( i%3==0||i%5==0||i%7==0){
               count1++;
           }
        }
        System.out.println("被357整除得数有"+count1+"个");


        getValue();
        String src="want you to know one thing";
        findCount(src,"n");
        findCount(src,"o");
    }
    public  static  void getValue(){
        int maxValue=0;
        int[] arrays=new int[10];
        System.out.print("随机数组内容是:");
        for (int i : arrays){
            i=(int)(Math.random()*100);
            System.out.print(","+i);
            if(i>maxValue){
                maxValue=i;
            }
        }
        System.out.println();
        System.out.println("最大数为"+maxValue);
    }
    public static void findCount(String src,String des) {
     int index = 0;
     int count = 0;
     while((index = src.indexOf(des, index)) != -1) {
            count++;
            index = index + des.length();
         }
     System.out.println(des+"出现了 "+count+" 次");
    }

}
