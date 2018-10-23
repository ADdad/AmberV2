//package com.example.demo;
//
//import org.springframework.boot.*;
//import org.springframework.boot.autoconfigure.*;
//import org.springframework.stereotype.Component;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
//@Component
//public class DBLoad implements CommandLineRunner {
//
//    private final UserRepo repository;
//
//    @Autowired
//    public DBLoad(UserRepo repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public void run(String... strings) throws Exception {
//        this.repository.save(new User("a","b","c"));
//    }
//}