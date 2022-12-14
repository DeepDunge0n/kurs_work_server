package org.fr2eman.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fr2eman.model.Order;
import org.fr2eman.model.Product;
import org.fr2eman.model.User;

public class DataBaseService {

    // users table
    final public static String USERS_ID = "id";
    final public static String USERS_LOGIN = "login";
    final public static String USERS_PASS = "password";
    final public static String USERS_ACCESS = "access";
    final public static String USERS_FIRST_NAME = "f_name";
    final public static String USERS_SECOND_NAME = "s_name";
    final public static String USERS_MIDDLE_NAME = "m_name";
    final public static String USERS_EMAIL = "email";

    // products table
    final public static String PRODUCTS_ID = "id";
    final public static String PRODUCTS_NAME = "name";
    final public static String PRODUCTS_PRICE = "price";
    final public static String PRODUCTS_NUMBER = "number";
    final public static String PRODUCTS_DESCRIPTION = "description";

    // orders table
    final public static String ORDERS_ID = "id";
    final public static String ORDERS_USER = "user";
    final public static String ORDERS_DATE = "date";
    final public static String ORDERS_STATE = "state";

    //orderdetails table
    final public static String ORDER_DETAILS_ID = "id_order";
    final public static String ORDER_DETAILS_PRODUCT_ID = "id_product";
    final public static String ORDER_DETAILS_NUMBER = "number";
    final public static String ORDER_MONTH = "month";
    final public static String ORDER_DAY = "day";

    // views
    final public static String NUMBER_ORDERS = "numberOrders";
    final public static String SUMM_ORDERS = "summOrders";
    final public static String PRICE_ORDER = "priceOrder";

    private static DataBaseService instance = new DataBaseService();

    public static DataBaseService getInstance() {
        return instance;
    }

    public List<User> parseUsersData(ResultSet parseData) {
        List<User> users = new ArrayList<User>();
        try {
            while(parseData.next()) {
                int id = parseData.getInt(USERS_ID);
                String login = parseData.getString(USERS_LOGIN);
                String pass = parseData.getString(USERS_PASS);
                int access = parseData.getInt(USERS_ACCESS);
                String fName = parseData.getString(USERS_FIRST_NAME);
                String sName = parseData.getString(USERS_SECOND_NAME);
                String mName = parseData.getString(USERS_MIDDLE_NAME);
                String email = parseData.getString(USERS_EMAIL);
                users.add(new User(id, login, pass, access, sName, fName, mName, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public List<User> parseUsersByNumberOrdersData(ResultSet parseData) {
        List<User> users = new ArrayList<User>();
        try {
            while(parseData.next()) {
                int id = parseData.getInt(USERS_ID);
                String login = parseData.getString(USERS_LOGIN);
                int numberOrders = parseData.getInt(NUMBER_ORDERS);
                int summOrders = parseData.getInt(SUMM_ORDERS);
                users.add(new User(id, login, numberOrders, summOrders));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public List<Product> parseProductsData(ResultSet parseData) {
        List<Product> users = new ArrayList<Product>();
        try {
            while(parseData.next()) {
                int id = parseData.getInt(PRODUCTS_ID);
                String name = parseData.getString(PRODUCTS_NAME);
                int number = parseData.getInt(PRODUCTS_NUMBER);
                int price = parseData.getInt(PRODUCTS_PRICE);
                String description = parseData.getString(PRODUCTS_DESCRIPTION);
                users.add(new Product(id, name, price, number, description));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public List<Order> parseOrdersData(ResultSet parseData) {
        List<Order> orders = new ArrayList<Order>();
        try {
            while(parseData.next()) {
                int id = parseData.getInt(ORDERS_ID);
                int user = parseData.getInt(ORDERS_USER);
                String date = parseData.getString(ORDERS_DATE);
                int state = parseData.getInt(ORDERS_STATE);
                orders.add(new Order(id, user, date, state));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orders;
    }

    public List<Order> parseOrdersBySumPriceData(ResultSet parseData) {
        List<Order> orders = new ArrayList<Order>();
        try {
            while(parseData.next()) {
                int id = parseData.getInt(ORDERS_ID);
                int user = parseData.getInt(ORDERS_USER);
                String date = parseData.getString(ORDERS_DATE);
                int price = parseData.getInt(PRICE_ORDER);
                int state = parseData.getInt(ORDERS_STATE);
                orders.add(new Order(id, user, date, price, state));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orders;
    }

    public void parseStatisticOrdersForYear(ResultSet parseData,
                                            Map<Integer, Integer> numberOrders) {
        try {
            while(parseData.next()) {
                int month = parseData.getInt(ORDER_MONTH);
                int number = parseData.getInt(NUMBER_ORDERS);
                numberOrders.put(month, number);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseStatisticOrdersForMonth(ResultSet parseData,
                                             Map<Integer, Integer> numberOrders) {
        try {
            while(parseData.next()) {
                int day = parseData.getInt(ORDER_DAY);
                int number = parseData.getInt(NUMBER_ORDERS);
                numberOrders.put(day, number);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Integer> getIntegersFromTable(ResultSet parseData, String column) {
        List<Integer> listNumbers = new ArrayList<Integer>();
        try {
            while(parseData.next()) {
                listNumbers.add(parseData.getInt(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listNumbers;
    }

    public List<String> getStringsFromTable(ResultSet parseData, String column) {
        List<String> listStrings = new ArrayList<String>();
        try {
            while(parseData.next()) {
                listStrings.add(parseData.getString(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listStrings;
    }
    public List<Product> getOrderProducts(ResultSet parseData) {
        List<Product> product = new ArrayList<Product>();
        try {
            while(parseData.next()) {
                int id = parseData.getInt(PRODUCTS_ID);
                String name = parseData.getString(PRODUCTS_NAME);
                int number = parseData.getInt(ORDER_DETAILS_NUMBER);
                int price = parseData.getInt(PRODUCTS_PRICE);
                product.add(new Product(id, name, price, number));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return product;
    }
    public int getNumberFromTable(ResultSet parseData, String column) {
        int number = 0;
        try {
            parseData.next();
            number = parseData.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return number;
    }
    public Map<Integer, Integer> parseOrderDetails(ResultSet parseData) {
        Map<Integer, Integer> orderDetails = new HashMap<Integer, Integer>();
        try {
            while(parseData.next()) {
                int id_product = parseData.getInt(ORDER_DETAILS_PRODUCT_ID);
                int number = parseData.getInt(ORDER_DETAILS_NUMBER);
                orderDetails.put(id_product, number);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                parseData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderDetails;
    }
}
