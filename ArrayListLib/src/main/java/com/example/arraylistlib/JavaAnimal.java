package com.example.arraylistlib;

/**
 *   实现 Cloneable  只是浅拷贝  只对 基本数据类型和string  值传递得数据可以做修改    引用类型 是地址传递  改变会同步
 *
 *   让 引用类型 也实现Cloneable  并 修改 该类得clone 方法，重新设置引用类型，做到深拷贝
 */
public class JavaAnimal implements Cloneable{

    private String name;
    private Integer weight;

    private Animal animal;

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Animal getAnimal() {
        return animal;
    }

    public JavaAnimal(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        JavaAnimal javaAnimal = (JavaAnimal) super.clone();
        Animal clone = (Animal) animal.clone();  //重新设置 animal  以实现 深拷贝
        javaAnimal.setAnimal(clone);
        return javaAnimal;
    }

    @Override
    public String toString() {
        return "JavaAnimal{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
