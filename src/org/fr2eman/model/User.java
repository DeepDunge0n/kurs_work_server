package org.fr2eman.model;

public class User {

    private int m_id;
    private String m_login;
    private String m_pass;
    private int m_access;
    private String m_secondName;
    private String m_firstName;
    private String m_middleName;
    private String m_email;
    private int m_numberOrders;
    private int m_sumOrders;

    public User() {
        m_id = 0;
        m_login = "";
        m_pass = "";
        m_access = 0;
        m_secondName = "";
        m_firstName = "";
        m_middleName = "";
        m_email = "";
        m_numberOrders = 0;
        m_sumOrders = 0;
    }

    public User(int id, String login, int numberOrders, int priceOrders) {
        m_id = id;
        m_login = new String(login);
        m_pass = "";
        m_access = 0;
        m_secondName = "";
        m_firstName = "";
        m_middleName = "";
        m_email = "";
        m_numberOrders = numberOrders;
        m_sumOrders = priceOrders;
    }

    public User(int id, String login, String pass, int access,
                String sName, String fName, String mName, String email) {
        m_id = id;
        m_login = login;
        m_pass = pass;
        m_access = access;
        m_secondName = sName;
        m_firstName = fName;
        m_middleName = mName;
        m_email = email;
        m_numberOrders = 0;
        m_sumOrders = 0;
    }

    public User(int id, String login, String pass, int access,
                String sName, String fName, String mName) {
        m_id = id;
        m_login = login;
        m_pass = pass;
        m_access = access;
        m_secondName = sName;
        m_firstName = fName;
        m_middleName = mName;
        m_email = "";
        m_numberOrders = 0;
        m_sumOrders = 0;
    }

    public User(String strObject) {
        String []datasObject = strObject.split(":");
        m_id = Integer.valueOf(datasObject[0]);
        m_login = datasObject[1];
        m_pass = datasObject[2];
        m_access = Integer.valueOf(datasObject[3]);
        m_secondName = datasObject[4];
        m_firstName = datasObject[5];
        m_middleName = datasObject[6];
        m_email = datasObject[7];
        m_numberOrders = 0;
        m_sumOrders = 0;
    }

    public int getId() {
        return m_id;
    }

    public void setId(int id) {
        m_id = id;
    }

    public String getLogin() {
        return m_login;
    }

    public void setLogin(String login) {
        m_login = login;
    }

    public String getPassword() {
        return m_pass;
    }

    public void setPassword(String password) {
        m_pass = password;
    }

    public int getAccess() {
        return m_access;
    }

    public void setAccess(int access) {
        m_access = access;
    }

    public String getFirstName() {
        return m_firstName;
    }

    public void setFirstName(String firstName) {
        m_firstName = firstName;
    }

    public String getSecondName() {
        return m_secondName;
    }

    public void setSecondName(String secondName) {
        m_secondName = secondName;
    }

    public String getMiddleName() {
        return m_middleName;
    }

    public void setMiddleName(String middleName) {
        m_middleName = middleName;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        m_email = email;
    }

    public int getNumberOrders() {
        return m_numberOrders;
    }

    public void setNumberOrders(int numberOrders) {
        m_numberOrders = numberOrders;
    }

    public int getSummOrders() {
        return m_sumOrders;
    }

    public void setSummOrders(int sumOrders) {
        m_sumOrders = sumOrders;
    }

    @Override
    public String toString() {
        String strObject = "" + String.valueOf(m_id) + ":" + m_login + ":"
                + m_pass + ":" + String.valueOf(m_access) + ":" + m_secondName
                + ":" + m_firstName + ":" + m_middleName + ":" + m_email;
        return strObject;
    }

}
