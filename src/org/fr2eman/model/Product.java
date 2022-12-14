package org.fr2eman.model;

public class Product {

    private int m_id;
    private String m_name;
    private int m_number;
    private int m_price;
    private String m_description;

    public Product() {
        m_id = 0;
        m_name = "";
    }
    public Product(int id, String name) {
        m_id = id;
        m_name = new String(name);
    }

    public Product(int id, String name, int price, int number) {
        m_id = id;
        m_name = new String(name);
        m_price = price;
        m_number = number;
        m_description = new String("");
    }

    public Product(int id, String name, int price, int number, String description) {
        m_id = id;
        m_name = new String(name);
        m_price = price;
        m_number = number;
        if(description != null)
            m_description = new String(description);
        else
            m_description = new String("");
    }

    public int getId() {
        return m_id;
    }
    public void setId(int id) {
        m_id = id;
    }
    public String getName() {
        return m_name;
    }
    public void setName(String name) {
        m_name = name;
    }
    public int getNumber() {
        return m_number;
    }
    public void setNumber(int number) {
        m_number = number;
    }
    public String getDescription() {
        return m_description;
    }
    public void setDescription(String description) {
        m_description = description;
    }
    public int getPrice() {
        return m_price;
    }
    public void setPrice(int price) {
        m_price = price;
    }
}
