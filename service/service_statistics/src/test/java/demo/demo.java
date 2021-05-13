package demo;

import org.junit.Test;

import java.util.*;
import demo.Person;

public class demo {



    @Test
    public void test(){

      /*  Map<String,Object> hashMap=new HashMap<>();
        hashMap.put("d","ddd");
        hashMap.put("b","bbb");
        hashMap.put("a","aaa");
        hashMap.put("c","ccc");
        hashMap.put("e",null);
        Iterator<String> hashMapIterators = hashMap.keySet().iterator();
        while (hashMapIterators.hasNext()){
            String key = hashMapIterators.next();
            System.out.println("hashMap.get(key) is :"+hashMap.get(key));
        }

        Map<String,String> treeMap =new TreeMap<>();
        treeMap.put("d","ddd");
        treeMap.put("b","bbb");
        treeMap.put("a","aaa");
        treeMap.put("c","ccc");
        treeMap.put("e",null);

        Iterator<String> treeMapIterators = treeMap.keySet().iterator();
        while (treeMapIterators.hasNext()){
            String key = treeMapIterators.next();
            System.out.println("treeMap.get(key) is :"+treeMap.get(key));

        }




        Map<String,String> hashTable=new Hashtable<>();
        hashTable.put("d","ddd");
        hashTable.put("b","bbb");
        hashTable.put("a","aaa");
        hashTable.put("c","ccc");
        Set<Map.Entry<String, String>> entries = hashTable.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println("hashTable.get(key) is :"+entry.getValue());
        }
       // hashTable.put("e",null);*/

       /* List<String> list=new ArrayList<>();
        list.add("x");
        List<String> list1 = Collections.unmodifiableList(list);
        list1.add("z");
        System.out.println(list1.size());*/
        String str="sanfklasfnasklfas";
        Map<String,Integer> map=new HashMap<>();
        char[] bytes = str.toCharArray();
        for (char aByte : bytes) {
            int count=1;
            if(map.containsKey(aByte+"")){
                Integer integer = map.get(aByte+"");
                map.put(aByte+"",++integer);
            }
            else {
                map.put(aByte+"",count);
            }
        }
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey()+"的次数为"+entry.getValue());
        }


    }

    @Test
    public void  test1(){
        Set<Person> set=new TreeSet<>();
        Comparator<Person> comparator=new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge()-o2.getAge();
            }
        };
        set.add(new Person("zhangsan",12));
        set.add(new Person("lisi",13));
        set.add(new Person("wangwu",15));

        Iterator<Person> iterator = set.iterator();
        while (iterator.hasNext()){
            Person next = iterator.next();
            System.out.println(next.getName()+":"+next.getAge());
        }
    }
}
