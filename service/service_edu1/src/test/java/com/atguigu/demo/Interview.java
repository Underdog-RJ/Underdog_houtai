package com.atguigu.demo;

import com.atguigu.commonutils.R;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.omg.CORBA.portable.InputStream;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;

public class Interview {


    public static void main(String[] args) {
       /* System.out.print("输入");
        Scanner scanner=new Scanner(System.in);
        String reader = scanner.nextLine();
        String[] split = reader.split("/");
        List<String> list=new ArrayList<>();
        for (String s : split) {
            char[] chars = s.toCharArray();
            StringBuilder builder=new StringBuilder();
            for (int i = 0; i < chars.length/2; i++) {
                char temp=chars[i];
                chars[i]=chars[chars.length-i-1];
                chars[chars.length-i-1]=temp;
            }
            for (int i = 0; i < chars.length; i++) {
                builder.append(chars[i]);
            }
           list.add(builder.toString());
        }
        int result=0;
        for (int i = 0; i < list.size(); i++) {
            result+=Integer.parseInt(list.get(i));
        }
        System.out.println(result);*/

        System.out.println("safas".length());
    }

    @Test
    public void test() throws IOException {
        String filePath="D:\\2.txt";
        File file=new File(filePath);
        FileInputStream fis=new FileInputStream(file);
        int b;
        List<Character> list=new ArrayList<>();
        while ((b=fis.read())!=-1){
            boolean flag = Character.isDigit((char) b);
            if(flag){
                list.add((char)b);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        fis.close();
    }

    @Test
    public void test1() throws IOException {
        String filePath="D:\\2.txt";
        File file=new File(filePath);
        FileInputStream fis=new FileInputStream(file);
        int len;
        byte[] b=new byte[2];
        while ((len=fis.read(b))!=-1){
            System.out.println(new String(b));
        }

        fis.close();
    }


    @Test
    public void test2() throws IOException {
        String filePath="G:\\1.jpg";
        FileInputStream fis=new FileInputStream(filePath);
        FileOutputStream fos=new FileOutputStream("G:\\test_copy.jpg");
        int len;
        byte[] b=new byte[1024];
        while ((len=fis.read(b))!=-1){
            fos.write(b,0,len);
        }

        fis.close();
        fos.close();
    }

    @Test
    public void test3() throws IOException {
        FileReader fr=new FileReader("D:\\2.txt");
        int len;
        char[] chars=new char[2];
        while((len=fr.read(chars))!=-1){
            System.out.println(new String(chars,0,len));
        }
    }

    public static Integer getInteger(Supplier<Integer> sup){
        return sup.get();
    }

    @Test
    public void test4(){
        int[] arr=new int[]{0,1,2,3,4,5,6,7,8,9};
        List<int[]> ints = Arrays.asList(arr);


    }
    public String getRandom(int[] arr){



        return null;
    }






}
