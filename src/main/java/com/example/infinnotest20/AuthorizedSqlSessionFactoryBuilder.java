package com.example.infinnotest20;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class AuthorizedSqlSessionFactoryBuilder {
    private static SqlSessionFactory factory;

    public static boolean isAuthorized = false;

    public static void setAuthorized(boolean state) {
        isAuthorized = state;
    }

    public static SqlSessionFactory getSqlSessionFactory() throws FileNotFoundException {
        if (!isAuthorized)
            return null;

        if (factory == null)
            factory = new SqlSessionFactoryBuilder().build(new FileReader(new File("C:\\Users\\plame\\IdeaProjects\\infinno-test-20\\src\\main\\java\\com\\example\\infinnotest20\\config.xml")));

        return factory;
    }


}
