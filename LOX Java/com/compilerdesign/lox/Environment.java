package com.compilerdesign.lox;

import java.util.HashMap;
import java.util.Map;

class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    // Chapter 8 - Challenge 2
    // Making it a runtime error for accessing a variable that has not been initialized yet.

    static final Object UNINITIALIZED = new Object();


    Environment(){
        enclosing = null;
    }

    Environment(Environment enclosing){
        this.enclosing = enclosing;
    }

    Object get(Token name){
        if(values.containsKey(name.lexeme)){
            Object value = values.get(name.lexeme);
            if (value == UNINITIALIZED) {
                throw new RuntimeError(name,
                        "Variable '" + name.lexeme + "' has not been initialized.");
            }
            return value;
        }

        if(enclosing!=null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable: " + name.lexeme +".");
    }

    void define(String name, Object value) {
        values.put(name, value);
    }

    void assign(Token name, Object value) {
        if(values.containsKey(name.lexeme)){
            values.put(name.lexeme, value);
            return;
        }

        if(enclosing!=null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable: " + name.lexeme +".");
    }



}
