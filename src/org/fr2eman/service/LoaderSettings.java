package org.fr2eman.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LoaderSettings {

    private static final String DATABASE_URL = "DATABASE_URL";
    private static final String DATABASE_PORT = "DATABASE_PORT";
    private static final String DATABASE_NAME = "DATABASE_NAME";
    private static final String DATABASE_LOGIN = "DATABASE_LOGIN";
    private static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";
    private static final String SERVER_PORT = "SERVER_PORT";

    private static LoaderSettings instance = new LoaderSettings();

    public static LoaderSettings getInstance() {
        return instance;
    }

    public void initConfigurations(String nameFileConfig) throws IOException {

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(nameFileConfig)));
        } catch (FileNotFoundException e) {
            //System.out.println("Файл config.conf не найден.");
            throw e;
        } catch (IOException e) {
            //System.out.println("Ошибка чтения файла config.conf");
            throw e;
        }
        boolean isValidProperties = true;

        String databaseURL = properties.getProperty(DATABASE_URL);
        if(databaseURL == null || databaseURL.isEmpty()) {
            //System.out.println("В файле config.conf не указан URL базы данных");
            isValidProperties = false;
        } else DataService.getInstance().setDatabaseURL(databaseURL);

        int databasePort = 0;
        try {
            databasePort = Integer.valueOf(properties.getProperty(DATABASE_PORT));
            if(databasePort <= 1024 || databasePort > 65535) {
                //System.out.println("Порт должен быть в диапахоне от 1025 до 65535");
                isValidProperties = false;
            } else DataService.getInstance().setDatabasePort(databasePort);
        } catch(NumberFormatException e) {
            //System.out.println("В файле config.conf не указан порт базы данных, или указан некорректно");
            isValidProperties = false;
        }

        String databaseName = properties.getProperty(DATABASE_NAME);
        if(databaseName == null || databaseName.isEmpty()) {
            //System.out.println("В файле config.conf не указано название базы данных");
            isValidProperties = false;
        } else DataService.getInstance().setDatabaseName(databaseName);

        String databaseLogin = properties.getProperty(DATABASE_LOGIN);
        if(databaseLogin == null || databaseLogin.isEmpty()) {
            //System.out.println("В файле config.conf не указан логин для доступа к базе данных");
            isValidProperties = false;
        } else DataService.getInstance().setDatabaseLogin(databaseLogin);

        String databasePass = properties.getProperty(DATABASE_PASSWORD);
        if(databasePass == null) {
            //System.out.println("В файле config.conf не указан пароль для доступа к базе данных");
            isValidProperties = false;
        } else DataService.getInstance().setDatabasePassword(databasePass);

        int serverPort = 0;
        try {
            serverPort = Integer.valueOf(properties.getProperty(SERVER_PORT));
            if(serverPort <= 1024 || serverPort > 65535) {
                //System.out.println("Порт сервера должен быть в диапазоне от 1025 до 65535");
                isValidProperties = false;
            } else DataService.getInstance().setPortServerSocket(serverPort);
        } catch(NumberFormatException e) {
            //System.out.println("В файле config.conf не указан порт сервера, или указан некорректно");
            isValidProperties = false;
        }

        if(!isValidProperties) throw new IOException();
    }

}
