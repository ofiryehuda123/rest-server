package com.dev.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
//האובייקט נוצר על ידי ספרינג יש לו את מעגל החיים של ספרינג
public class Persist {

    private Connection connection;


    @PostConstruct
 //היא רצה לבד מיד עם היצירה של האובייקט
    public void createConnectionToDatabase () {
        try {
            //יצירת קונקשיין לדאטאבייס,באמצעות 3פרטים שאני מספק לו:כתובת ובו כתוב לאיזה סכמה אני רוצה להתחבר,משתמש,ססמה
            //3 אלה מאפשרים לנו לגשת לדאטאבייס ולהתחבר ולבדוק את התקינות שלו-דרך הקונקשיין הזה אפשר לבצע שאילתות
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/first_project", "root", "1234");
            System.out.println("Successfully connected to DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
