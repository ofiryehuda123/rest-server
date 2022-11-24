package com.dev.controllers;

import com.dev.objects.User;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
public class TestController {

    private List<User> myUsers = new ArrayList<>();

    @Autowired
    public Utils utils;

    @PostConstruct
    public void init () {
    }


    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test () {
        return new Date().toString();
    }

    @RequestMapping(value = "/create-account", method = {RequestMethod.GET, RequestMethod.POST})
    public User createAccount (String username, String password) {
        User newAccount = null;
        if (utils.validateUsername(username)) {
            if (utils.validatePassword(password)) {
                if (!checkIfUsernameExists(username)) {
                    String token = createHash(username, password);
                    newAccount = new User(username, token);
                    this.myUsers.add(newAccount);
                } else {
                    System.out.println("username already exits");
                }
            } else {
                System.out.println("password is invalid");
            }
        } else {
            System.out.println("username is invalid");
        }
        return newAccount;
    }


    public String createHash (String username, String password) {
        String raw = String.format("%s_%s", username, password);
        String myHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }

    private boolean checkIfUsernameExists (String username) {
        boolean exists = false;
        for (User user : this.myUsers) {
            if (user.getUsername().equals(username)) {
                exists = true;
                break;
            }
        }

        return exists;
    }


    @RequestMapping(value = "/save-note", method = {RequestMethod.GET, RequestMethod.POST})
    public boolean saveNote (String note, String token) {
        boolean success = false;
        if (utils.validateNote(note)) {
            User user = getUserByToken(token);
            if (user != null) {
                user.addNote(note);
                success = true;
            } else {
                System.out.println("cannot find match for token " + token);
            }
        } else {
            System.out.println("note text was not validated");
        }
        return success;
    }

    @RequestMapping(value = "/get-all-notes", method = {RequestMethod.GET, RequestMethod.POST})
    public List<String> getAllNotes (String token) {
        List<String> notes = null;
        User user = getUserByToken(token);
        if (user != null) {
            notes = user.getNotes();
        } else {
            System.out.println("cannot find match for token " + token);
        }

        return notes;
    }

    private User getUserByToken (String token) {
        User matchedUser = null;
        if (token != null) {
            for (User user : this.myUsers) {
                if (user.getToken().equals(token)) {
                    matchedUser = user;
                    break;
                }
            }
        }
        return matchedUser;
    }




}
