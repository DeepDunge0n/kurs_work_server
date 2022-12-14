package org.fr2eman.service;

public class DataService {

    private static DataService instance = new DataService();

    public static DataService getInstance() {
        return instance;
    }

    private int m_portServerSocket;
    private String m_databaseURL;
    private int m_databasePort;
    private String m_databaseName;
    private String m_databaseLogin;
    private String m_databasePass;

    public int getPortServerSocket() {
        return m_portServerSocket;
    }
    public void setPortServerSocket(int port) {
        m_portServerSocket = port;
    }

    public String getDatabaseURL() {
        return m_databaseURL;
    }
    public void setDatabaseURL(String url) {
        m_databaseURL = new String(url);
    }

    public int getDatabasePort() {
        return m_databasePort;
    }
    public void setDatabasePort(int port) {
        m_databasePort = port;
    }

    public String getDatabaseName() {
        return m_databaseName;
    }
    public void setDatabaseName(String name) {
        m_databaseName = new String(name);
    }

    public String getDatabaseLogin() {
        return m_databaseLogin;
    }
    public void setDatabaseLogin(String login) {
        m_databaseLogin = new String(login);
    }

    public String getDatabasePassword() {
        return m_databasePass;
    }
    public void setDatabasePassword(String password) {
        m_databasePass = new String(password);
    }

}
