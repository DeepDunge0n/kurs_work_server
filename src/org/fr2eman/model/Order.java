package org.fr2eman.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {

    public static final int ORDER_IS_PREPARING = 0;
    public static final int ORDER_IS_SENT = 1;
    public static final int ORDER_IS_COMPLETED = 2;

    private int m_id;
    private int m_user;
    private Date m_date;
    private int m_state;
    private int m_price;

    public Order() {
        this.m_id = 0;
        this.m_user = 0;
        this.m_date = null;
        this.m_state = 0;
        this.m_price = 0;
    }

    public Order(int id, int user, String date, int state) {
        this.m_id = id;
        this.m_user = user;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-mm-dd");
        try {
            this.m_date = formatDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                this.m_date = formatDate.parse("0000-00-00");
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        this.m_state = state;
        this.m_price = 0;
    }

    public Order(int id, int user, String date, int price, int state) {
        this.m_id = id;
        this.m_user = user;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-mm-dd");
        try {
            this.m_date = formatDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                this.m_date = formatDate.parse("0000-00-00");
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        this.m_state = state;
        this.m_price = price;
    }

    public Order(int id, int user, String date) {
        this.m_id = id;
        this.m_user = user;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-mm-dd");
        try {
            this.m_date = formatDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                this.m_date = formatDate.parse("0000-00-00");
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        this.m_state = 0;
        this.m_price = 0;
    }

    public int getId() {
        return this.m_id;
    }

    public int getUser() {
        return this.m_user;
    }

    public Date getDate() {
        return this.m_date;
    }

    public int getState() {
        return this.m_state;
    }

    public int getPrice() {
        return this.m_price;
    }

    public String getStringDate() {
        return (new SimpleDateFormat("yyyy-mm-dd")).format(m_date);
    }
    @Override
    public String toString() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        return "" + m_id + ":" + m_user + ":" + sdfDate.format(m_date) + ":" + m_state;
    }
}
