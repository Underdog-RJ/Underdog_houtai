package demo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person implements Comparable<Person>{

    private String name;
    private Integer age;


    @Override
    public int compareTo(Person o) {
        return this.getAge()-o.getAge();
    }
}
