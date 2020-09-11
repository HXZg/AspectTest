package com.example.arraylistlib

class Animal(var name: String,var weight: Int) : Cloneable{

    public override fun clone(): Any {
        return super.clone()
    }

    override fun toString(): String {
        return "JavaAnimal{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}'
    }
}