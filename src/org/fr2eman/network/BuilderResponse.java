package org.fr2eman.network;

import com.mysql.jdbc.MysqlDataTruncation;
import org.fr2eman.database.DataBase;
import org.fr2eman.model.Order;
import org.fr2eman.model.Product;
import org.fr2eman.model.User;
import org.fr2eman.send.*;
import org.fr2eman.service.DataBaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

public class BuilderResponse {

    //keys request
    final public static String KEY_TYPE_REQUEST = "type";
    final public static String KEY_ID_REQUEST = "id";
    final public static String KEY_ID_USER_REQUEST = "id_user";
    final public static String KEY_ID_ORDER_REQUEST = "id_order";
    final public static String KEY_LOGIN_REQUEST = "login";
    final public static String KEY_PASS_REQUEST = "pass";
    final public static String KEY_ACCESS_REQUEST = "access";
    final public static String KEY_SNAME_REQUEST = "second_name";
    final public static String KEY_FNAME_REQUEST = "first_name";
    final public static String KEY_MNAME_REQUEST = "middle_name";
    final public static String KEY_EMAIL_REQUEST = "email";
    final public static String KEY_SEARCH_REQUEST = "search";
    final public static String KEY_SORT_REQUEST = "sort";
    final public static String KEY_TYPE_SORT_REQUEST = "type_sort";
    final public static String KEY_NUMBER_PRODUCTS_ORDER = "number_products_order";
    final public static String KEY_PRODUCT = "product";
    final public static String KEY_NUMBER_PRODUCT = "number_product";
    final public static String KEY_BEGIN_DATE = "begin_date";
    final public static String KEY_END_DATE = "end_date";
    final public static String KEY_NAME_PRODUCT_REQUEST = "name_product";
    final public static String KEY_PRICE_PRODUCT_REQUEST = "price_product";
    final public static String KEY_DESCRIPTION_PRODUCT_REQUEST = "description_product";
    final public static String KEY_ID_PRODUCT_REQUEST = "id_product";
    final public static String KEY_STATUS_ORDER_REQUEST = "order_status";
    final public static String KEY_IS_ALL_ORDERS = "is_all_orders";
    final public static String KEY_SEARCH_LOGIN_REQUEST = "search_login";
    final public static String KEY_NUMBER_PRODUCT_REQUEST = "number_product";
    final public static String KEY_MONTH_REQUEST = "month";
    final public static String KEY_YEAR_REQUEST = "year";

    //keys response
    final public static String KEY_LOGIN_RESPONSE = "login";
    final public static String KEY_PASS_RESPONSE = "pass";
    final public static String KEY_ACCESS_RESPONSE = "access";
    final public static String KEY_SNAME_RESPONSE = "second_name";
    final public static String KEY_FNAME_RESPONSE = "first_name";
    final public static String KEY_MNAME_RESPONSE = "middle_name";
    final public static String KEY_EMAIL_RESPONSE = "email";
    final public static String KEY_ID_RESPONSE = "id";
    final public static String KEY_ID_USER_RESPONSE = "id_user";
    final public static String KEY_USER_RESPONSE = "user";
    final public static String KEY_STATUS_AUTHORIZATION_RESPONSE = "authorization_status";
    final public static String KEY_STATUS_REGISTRATION_RESPONSE = "registration_status";
    final public static String KEY_MESSAGE_RESPONSE = "message";
    final public static String KEY_NUMBER_RESPONSE = "number";
    final public static String KEY_PRODUCT_RESPONSE = "product";
    final public static String KEY_DESCRIPTION_RESPONSE = "description";
    final public static String KEY_ORDER_RESPONSE = "order";
    final public static String KEY_DATE_RESPONSE = "date";
    final public static String KEY_STATE_RESPONSE = "state";
    final public static String KEY_DATE_ORDER_RESPONSE = "date_order";
    final public static String KEY_STATE_ORDER_RESPONSE = "state_order";
    final public static String KEY_SUMM_ORDER_RESPONSE = "summ_order";
    final public static String KEY_NUMBER_PRODUCTS_RESPONSE = "number_products";
    final public static String KEY_ID_ORDER_RESPONSE = "id_order";
    final public static String KEY_ID_PRODUCT_RESPONSE = "id_product";
    final public static String KEY_NAME_PRODUCT_RESPONSE = "name_product";
    final public static String KEY_NUMBER_PRODUCT_RESPONSE = "number_product";
    final public static String KEY_PRICE_PRODUCT_RESPONSE = "price_product";
    final public static String KEY_IS_DESCRIPTION_RESPONSE = "is_description";
    final public static String KEY_NUMBER_ORDERS_RESPONSE = "number_orders";
    final public static String KEY_PRICE_ORDERS_RESPONSE = "summ_orders";
    final public static String KEY_PRICE_RESPONSE = "price";
    final public static String KEY_MONTH_RESPONSE = "month";
    final public static String KEY_YEAR_RESPONSE = "year";
    final public static String KEY_DAY_RESPONSE = "day";

    //values response
    final public static boolean AUTH_SUCCESS_RESPONSE = false;
    final public static boolean AUTH_UNSUCCESS_RESPONSE = true;

    //database
    final public static String USERS_TABLE = "users";
    final public static String ORDERS_TABLE = "orders";
    final public static String PRODUCTS_TABLE = "products";
    final public static String ORDERS_DETAILS_TABLE = "order_details";

    //sql queries
    private static String STATISTIC_PRODUCTS_SQL = "SELECT order_details.id_product AS id, " +
            "products.name, SUM(order_details.number) AS number, SUM(order_details.number) " +
            "* products.price AS price, products.description FROM order_details " +
            "INNER JOIN orders ON order_details.id_order = orders.id " +
            "INNER JOIN products ON order_details.id_product = products.id " +
            "WHERE orders.date BETWEEN %s AND %s " +
            "GROUP BY order_details.id_product ORDER BY number DESC, price DESC " +
            "LIMIT 0, %d";

    private static String STATISTIC_USERS_SQL = "SELECT T.user AS id, COUNT(T.user) AS numberOrders, " +
            "SUM(T.priceOrder) AS summOrders, users.login FROM (SELECT order_details.id_order, " +
            "SUM(order_details.number * products.price) AS priceOrder, orders.user, orders.date " +
            "FROM order_details INNER JOIN products ON order_details.id_product = products.id " +
            "INNER JOIN orders ON order_details.id_order = orders.id " +
            "GROUP BY order_details.id_order) AS T INNER JOIN users ON T.user = users.id " +
            "WHERE T.date BETWEEN %s AND %s GROUP BY T.user " +
            "ORDER BY COUNT(T.user) DESC, SUM(T.priceOrder) DESC LIMIT 0, %d";

    private static String STATISTIC_ORDERS_SQL = "SELECT order_details.id_order AS id, " +
            "users.login, SUM(order_details.number * products.price) AS priceOrder, orders.user " +
            "AS user, orders.date, orders.state FROM order_details INNER JOIN products ON " +
            "order_details.id_product = products.id INNER JOIN orders ON " +
            "order_details.id_order = orders.id INNER JOIN users ON orders.user = users.id " +
            "WHERE orders.date BETWEEN %s AND %s GROUP BY order_details.id_order " +
            "ORDER BY priceOrder DESC LIMIT 0, %d";

    private static String STATISTIC_ORDERS_FOR_YEAR_SQL = "SELECT MONTH(date) AS month, " +
            "COUNT(id) AS numberOrders FROM fr2eman.orders WHERE date BETWEEN '%s' AND " +
            "'%s' GROUP BY MONTH(date)";
    private static String STATISTIC_ORDERS_FOR_MONTH_SQL = "SELECT DAY(date) AS day, " +
            "COUNT(id) AS numberOrders FROM fr2eman.orders WHERE date BETWEEN '%s' AND " +
            "'%s' GROUP BY DAY(date)";

    private static BuilderResponse instance = new BuilderResponse();

    public static BuilderResponse getInstance() {
        return instance;
    }

    DataResponse getResponseAuthorization(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String login = requestMap.get(KEY_LOGIN_REQUEST);
        String pass = requestMap.get(KEY_PASS_REQUEST);
        pass = SHA_256.hashCode(pass);
        int access = Integer.valueOf(requestMap.get(KEY_ACCESS_REQUEST));
        String whereString = String.format(
                "WHERE login = '%s' AND password = '%s' AND access & %d = %d",
                login, pass, access, access);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(USERS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<User> listUsers = DataBaseService.getInstance().parseUsersData(data);
        if(listUsers.isEmpty()) {
            responseError = true;
            responseMessage = "Пользователь с таким логином и паролем не найден";
        } else {
            responseData.put(KEY_ACCESS_RESPONSE, String.valueOf(
                    listUsers.get(0).getAccess()));
            responseError = false;
            responseMessage = "Авторизация прошла успешно";
        }
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseRegistration(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String firstName = requestMap.get(KEY_FNAME_REQUEST);
        String secondName = requestMap.get(KEY_SNAME_REQUEST);
        String middleName = requestMap.get(KEY_MNAME_REQUEST);
        String email = requestMap.get(KEY_EMAIL_REQUEST);
        String login = requestMap.get(KEY_LOGIN_REQUEST);
        String pass = requestMap.get(KEY_PASS_REQUEST);
        String access = requestMap.get(KEY_ACCESS_REQUEST);
        String whereString = String.format("WHERE login = '%s'", login);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(USERS_TABLE, "login", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        boolean notEmptyResultSet = false;
        try {
            notEmptyResultSet = data.next();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        if(!notEmptyResultSet) {
            try {
                pass= SHA_256.hashCode(pass);
                DataBase.getInstance().insertData(USERS_TABLE, "Default", login, pass,
                        access, firstName, secondName, middleName, email);
                responseData.put(KEY_STATUS_REGISTRATION_RESPONSE, "true");
                responseData.put(KEY_MESSAGE_RESPONSE, "Регистрация прошла успешно");
            } catch (SQLException e1) {
                e1.printStackTrace();
                responseData.put(KEY_STATUS_REGISTRATION_RESPONSE, "false");
                responseData.put(KEY_MESSAGE_RESPONSE, "Ошибка при добавлении пользователь");
            }
        } else {
            responseData.put(KEY_STATUS_REGISTRATION_RESPONSE, "false");
            responseData.put(KEY_MESSAGE_RESPONSE, "Пользователь с таким логином уже существует");
        }
        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseProducts(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String searchSubstring = requestMap.get(KEY_SEARCH_REQUEST);
        String sortColumn = requestMap.get(KEY_SORT_REQUEST);
        String typeSort = requestMap.get(KEY_TYPE_SORT_REQUEST);
        String whereString = "WHERE name LIKE '%" + searchSubstring + "%' "
                + "OR description LIKE '%" + searchSubstring + "%'";
        String orderString = "";
        if(!sortColumn.equals(""))
            orderString = String.format("ORDER BY %s %s", sortColumn, typeSort);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(PRODUCTS_TABLE, "*", whereString, orderString);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Product> listProducts = DataBaseService.getInstance().parseProductsData(data);
        if(listProducts.isEmpty()) {
            responseError = true;
            responseMessage = "Товары не найдены";
        } else {
            responseError = false;
            responseMessage = "Запрос выполнен успешно";
            responseData.put(KEY_NUMBER_RESPONSE, String.valueOf(listProducts.size()));
            int i = 1;
            for(Product item : listProducts) {
                responseData.put(KEY_ID_PRODUCT_RESPONSE + i,
                        String.valueOf(item.getId()));
                responseData.put(KEY_NAME_PRODUCT_RESPONSE + i,
                        item.getName());
                responseData.put(KEY_PRICE_PRODUCT_RESPONSE + i,
                        String.valueOf(item.getPrice()));
                responseData.put(KEY_DESCRIPTION_RESPONSE + i,
                        item.getDescription());
                responseData.put(KEY_NUMBER_PRODUCT_RESPONSE + i,
                        String.valueOf(item.getNumber()));
                ++i;
            }
        }
        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseDesctiptionProduct(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        int idProduct = Integer.valueOf(requestMap.get(KEY_ID_REQUEST));
        String whereString = String.format("WHERE id = %d", idProduct);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(PRODUCTS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Product> listProducts = DataBaseService.getInstance().parseProductsData(data);
        if(listProducts.isEmpty()) {
            responseError = true;
            responseMessage = "Товар не найден";
        } else {
            responseError = false;
            responseMessage = "Запрос выполнен успешно";
            responseData.put(KEY_ID_PRODUCT_RESPONSE, String.valueOf(
                    listProducts.get(0).getId()));
            responseData.put(KEY_NAME_PRODUCT_RESPONSE, listProducts.get(0).
                    getName());
            responseData.put(KEY_PRICE_PRODUCT_RESPONSE, String.valueOf(
                    listProducts.get(0).getPrice()));
            responseData.put(KEY_NUMBER_PRODUCT_RESPONSE, String.valueOf(
                    listProducts.get(0).getNumber()));
            if(!listProducts.get(0).getDescription().equals("")) {
                responseData.put(KEY_DESCRIPTION_RESPONSE, listProducts.get(0).getDescription());
                responseData.put(KEY_IS_DESCRIPTION_RESPONSE, String.valueOf(
                        true));
            } else {
                responseData.put(KEY_IS_DESCRIPTION_RESPONSE, String.valueOf(
                        false));
                responseData.put(KEY_DESCRIPTION_RESPONSE, "Описание для данного товара не найдено");
            }
        }
        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseMakeOrder(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String login = requestMap.get(KEY_LOGIN_REQUEST);
        String pass = requestMap.get(KEY_PASS_REQUEST);
        pass = SHA_256.hashCode(pass);
        String whereString = String.format(
                "WHERE login = '%s' AND password = '%s' AND (access = 1 OR access = 2)",
                login, pass);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(USERS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<User> listUsers = DataBaseService.getInstance().parseUsersData(data);
        if(listUsers.isEmpty()) {
            responseError = true;
            responseMessage = "Ошибка авторизации, ваш аккаун не найден";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        int idUser = listUsers.get(0).getId();
        int numberProduct = 0;
        try {
            numberProduct = Integer.valueOf(requestData.getData().get(KEY_NUMBER_PRODUCTS_ORDER));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Отправленные данные не корректны";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        try {
            DataBase.getInstance().insertData(ORDERS_TABLE, "Default",
                    String.valueOf(idUser), "CURDATE()", "0");
        } catch (SQLException e1) {
            e1.printStackTrace();
            responseError = true;
            responseMessage = "Ошибка при добавлении заказа";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        whereString = String.format("WHERE user = %d", idUser);
        String orderString = String.format(
                "ORDER BY id DESC LIMIT 0, 1",
                login, pass);
        data = null;
        try {
            data = DataBase.getInstance().selectData(ORDERS_TABLE, "*", whereString, orderString);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Order> listOrders = DataBaseService.getInstance().parseOrdersData(data);
        if(listOrders.isEmpty()) {
            responseError = true;
            responseMessage = "Ошибка добавления заказа";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String idOrder = String.valueOf(listOrders.get(0).getId());
        int i = 0;
        try {
            for(i = 1; i <= numberProduct; i++) {
                DataBase.getInstance().insertData(ORDERS_DETAILS_TABLE,
                        idOrder, requestMap.get(KEY_PRODUCT + i),
                        requestMap.get(KEY_NUMBER_PRODUCT + i));
                DataBase.getInstance().updateData(PRODUCTS_TABLE,
                        String.format("SET number = number - %s",
                                requestMap.get(KEY_NUMBER_PRODUCT + i)),
                        String.format("WHERE id = %s",
                                requestMap.get(KEY_PRODUCT + i)));
            }

        } catch (MysqlDataTruncation e1) {
            String productName = "";
            try {
                productName = DataBaseService.getInstance().getStringsFromTable(
                        DataBase.getInstance().selectData(PRODUCTS_TABLE, "name",
                                String.format("WHERE id = %s", requestMap.get(KEY_PRODUCT + i)), ""), "name").get(0);
                DataBase.getInstance().deleteData(ORDERS_DETAILS_TABLE,
                        String.format("WHERE id_order = %s", idOrder));
                DataBase.getInstance().deleteData(ORDERS_TABLE,
                        String.format("WHERE id = %s", idOrder));
                for(int j = 1; j < i; j++) {
                    DataBase.getInstance().updateData(PRODUCTS_TABLE,
                            String.format("SET number = number + %s",
                                    requestMap.get(KEY_NUMBER_PRODUCT + j)),
                            String.format("WHERE id = %s",
                                    requestMap.get(KEY_PRODUCT + j)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            responseError = true;
            responseMessage = "Товара не достаточно на складе: " + productName;
            return new DataResponse(responseError, responseMessage, responseData);
        } catch (SQLException e2) {
            responseError = true;
            responseMessage = "Ошибка при добавлении товаров в заказ";
            String where = String.format("WHERE id_order = %s", idOrder);
            try {
                DataBase.getInstance().deleteData(ORDERS_DETAILS_TABLE, where);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseData.put(KEY_ORDER_RESPONSE, idOrder);
        responseError = false;
        responseMessage = "Заказ принят";

        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseDatasUser(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String login = requestMap.get(KEY_LOGIN_REQUEST);
        String pass = requestMap.get(KEY_PASS_REQUEST);
        pass = SHA_256.hashCode(pass);
        String whereString = String.format(
                "WHERE login = '%s' AND password = '%s'",
                login, pass);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(USERS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<User> listUsers = DataBaseService.getInstance().parseUsersData(data);
        if(listUsers.isEmpty()) {
            responseError = true;
            responseMessage = "Данные о пользователе не найдены";
        } else {
            responseData.put(KEY_ID_RESPONSE, String.valueOf(
                    listUsers.get(0).getId()));
            responseData.put(KEY_LOGIN_RESPONSE, listUsers.get(0).getLogin());
            responseData.put(KEY_SNAME_RESPONSE, listUsers.get(0).getSecondName());
            responseData.put(KEY_FNAME_RESPONSE, listUsers.get(0).getFirstName());
            responseData.put(KEY_MNAME_RESPONSE, listUsers.get(0).getMiddleName());
            responseData.put(KEY_EMAIL_RESPONSE, listUsers.get(0).getEmail());
            responseData.put(KEY_ACCESS_RESPONSE, String.valueOf(
                    listUsers.get(0).getAccess()));
            responseError = false;
            responseMessage = "Данные найдены";
        }
        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseOrdersUser(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String login = requestMap.get(KEY_LOGIN_REQUEST);
        String pass = requestMap.get(KEY_PASS_REQUEST);
        pass = SHA_256.hashCode(pass);
        String beginDate = requestMap.get(KEY_BEGIN_DATE);
        String endDate = requestMap.get(KEY_END_DATE);
        String strSearch = requestMap.get(KEY_SEARCH_REQUEST);
        String whereString = String.format(
                "WHERE login = '%s' AND password = '%s' AND access = 1",
                login, pass);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(USERS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<User> listUsers = DataBaseService.getInstance().parseUsersData(data);
        if(listUsers.isEmpty()) {
            responseError = true;
            responseMessage = "Данные о пользователе не найдены";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        int idUser = listUsers.get(0).getId();
        if(beginDate.isEmpty()) beginDate = "''";
        else beginDate = "'" + beginDate + "'";
        if(endDate.isEmpty()) endDate = "CURDATE()";
        else endDate = "'" + endDate + "'";
        whereString = "WHERE id LIKE '%" + strSearch +
                "%' AND user = " + idUser + " AND date BETWEEN " + beginDate
                + " AND " + endDate;
        try {
            data = DataBase.getInstance().selectData(ORDERS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Order> listOrders = DataBaseService.getInstance().parseOrdersData(data);
        responseData.put(KEY_NUMBER_RESPONSE, String.valueOf(listOrders.size()));
        for(int i = 1; i <= listOrders.size(); ++i) {
            responseData.put(KEY_ID_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getId()));
            responseData.put(KEY_USER_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getUser()));
            responseData.put(KEY_DATE_RESPONSE + i, listOrders.get(i - 1).getStringDate());
            responseData.put(KEY_STATE_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getState()));
        }
        responseError = false;
        responseMessage = "Данные найдены";
        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseOrderDetails(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        int idOrder = Integer.valueOf(requestMap.get(KEY_ID_ORDER_REQUEST));
        String whereString = String.format(
                "WHERE id = '%d'", idOrder);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(ORDERS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Order> listOrders = DataBaseService.getInstance().parseOrdersData(data);
        if(listOrders.isEmpty()) {
            responseError = true;
            responseMessage = "Заказ не найден";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String dateOrder = listOrders.get(0).getStringDate();
        String stateOrder = String.valueOf(listOrders.get(0).getState());
        String idUserOrder = String.valueOf(listOrders.get(0).getUser());
        whereString = String.format("WHERE id = %d", listOrders.get(0).getUser());
        try {
            data = DataBase.getInstance().selectData(USERS_TABLE, "login", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String loginUser = "";
        try {
            if(data.next()) {
                loginUser = data.getString("login");
            }
        } catch (SQLException e1) {
        }
        //
        String fields = String.format("%s.id, %s.name, %s.number, %s.number * %s.price AS price",
                PRODUCTS_TABLE, PRODUCTS_TABLE, ORDERS_DETAILS_TABLE, ORDERS_DETAILS_TABLE,
                PRODUCTS_TABLE);
        whereString = String.format("WHERE id_order = %d", idOrder);
        String join = String.format("INNER JOIN %s ON %s.id_product = %s.id",
                PRODUCTS_TABLE, ORDERS_DETAILS_TABLE, PRODUCTS_TABLE);
        try {
            data = DataBase.getInstance().selectData(ORDERS_DETAILS_TABLE, fields, whereString, "", join);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Product> listProduct = DataBaseService.getInstance().getOrderProducts(data);
        fields = String.format("SUM(%s.number * %s.price) AS summ", ORDERS_DETAILS_TABLE, PRODUCTS_TABLE);
        try {
            data = DataBase.getInstance().selectData(ORDERS_DETAILS_TABLE, fields, whereString, "", join);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        int summ = DataBaseService.getInstance().getNumberFromTable(data, "summ");
        responseData.put(KEY_ID_ORDER_RESPONSE, String.valueOf(idOrder));
        responseData.put(KEY_USER_RESPONSE, idUserOrder);
        responseData.put(KEY_LOGIN_RESPONSE, loginUser);
        responseData.put(KEY_DATE_ORDER_RESPONSE, dateOrder);
        responseData.put(KEY_STATE_ORDER_RESPONSE, stateOrder);
        responseData.put(KEY_SUMM_ORDER_RESPONSE, String.valueOf(summ));
        responseData.put(KEY_NUMBER_PRODUCTS_RESPONSE, String.valueOf(listProduct.size()));
        int index = 1;
        for(Product product : listProduct) {
            responseData.put(KEY_ID_PRODUCT_RESPONSE + index, String.valueOf(product.getId()));
            responseData.put(KEY_NAME_PRODUCT_RESPONSE + index, product.getName());
            responseData.put(KEY_NUMBER_PRODUCT_RESPONSE + index, String.valueOf(product.getNumber()));
            responseData.put(KEY_PRICE_PRODUCT_RESPONSE + index, String.valueOf(product.getPrice()));
            ++index;
        }
        responseError = false;
        responseMessage = "Данные найдены";
        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseCancelOrder(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        int idOrder = Integer.valueOf(requestMap.get(KEY_ID_ORDER_REQUEST));
        String whereString = String.format(
                "WHERE id_order = '%d'", idOrder);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(ORDERS_DETAILS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        Map<Integer, Integer> orderDetails = DataBaseService.getInstance().parseOrderDetails(data);
        for(Entry<Integer, Integer> entry : orderDetails.entrySet()) {
            String setString = String.format("SET number = number + %d", entry.getValue());
            whereString = String.format("WHERE id = %d", entry.getKey());
            try {
                DataBase.getInstance().updateData(PRODUCTS_TABLE, setString, whereString);
            } catch(SQLException e) {
                responseError = true;
                responseMessage = "Ошибка запроса к БД";
                return new DataResponse(responseError, responseMessage, responseData);
            }
        }
        whereString = String.format("WHERE id = %d", idOrder);
        try {
            DataBase.getInstance().deleteData(ORDERS_TABLE, whereString);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseError = false;
        responseMessage = "Заказ №" + idOrder + " отменён";
        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseAddProduct(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;

        Map<String, String> requestMap = requestData.getData();
        String name = requestMap.get(KEY_NAME_PRODUCT_REQUEST);
        String description = requestMap.get(KEY_DESCRIPTION_PRODUCT_REQUEST);
        int number = 0;
        int price = 0;
        try {
            number = Integer.valueOf(requestMap.get(KEY_NUMBER_PRODUCT));
            price = Integer.valueOf(requestMap.get(KEY_PRICE_PRODUCT_REQUEST));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Ошибка обработки полученых данных";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String whereString = String.format("WHERE name = '%s'", name);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(PRODUCTS_TABLE, "COUNT(*)", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        int countRow = 0;
        try {
            data.next();
            countRow = data.getInt("COUNT(*)");
        } catch (SQLException e) {
            System.out.println("Ошибка проверки наименования товара");
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        if(countRow != 0) {
            responseError = true;
            responseMessage = "Товар с таким наименованием уже существует";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        int id = 0;
        try {
            DataBase.getInstance().insertData(PRODUCTS_TABLE, "Default", name,
                    String.valueOf(price), String.valueOf(number), description);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка добавления товара";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        try {
            ResultSet setId = DataBase.getInstance().getStatement().getGeneratedKeys();
            if(setId.next()) {
                id = setId.getInt(1);
            }
        } catch (SQLException e) {
            responseError = true;
            responseMessage = "Ошибка получения кода товара";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseData.put(KEY_ID_PRODUCT_RESPONSE, String.valueOf(id));
        responseData.put(KEY_NAME_PRODUCT_RESPONSE, name);
        responseData.put(KEY_PRICE_PRODUCT_RESPONSE, String.valueOf(price));
        responseData.put(KEY_NUMBER_PRODUCT_RESPONSE, String.valueOf(number));
        responseData.put(KEY_DESCRIPTION_RESPONSE, description);

        responseError = false;
        responseMessage = "Товар №" + id + " добавлен";
        return new DataResponse(responseError, responseMessage, responseData);
    }
    DataResponse getResponseUpdateProduct(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;

        Map<String, String> requestMap = requestData.getData();
        String name = requestMap.get(KEY_NAME_PRODUCT_REQUEST);
        String description = requestMap.get(KEY_DESCRIPTION_PRODUCT_REQUEST);
        int number = 0;
        int price = 0;
        int id = 0;
        try {
            id = Integer.valueOf(requestMap.get(KEY_ID_PRODUCT_REQUEST));
            number = Integer.valueOf(requestMap.get(KEY_NUMBER_PRODUCT));
            price = Integer.valueOf(requestMap.get(KEY_PRICE_PRODUCT_REQUEST));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Ошибка обработки полученых данных";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        if(number < 0 || price <= 0) {
            responseError = true;
            responseMessage = "Отправлены не правильные данные";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String whereString = String.format("WHERE name = '%s' AND id != %d", name, id);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(PRODUCTS_TABLE, "COUNT(*)", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        int countRow = 0;
        try {
            data.next();
            countRow = data.getInt("COUNT(*)");
        } catch (SQLException e) {
            System.out.println("Ошибка проверки наименования товара");
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        if(countRow != 0) {
            responseError = true;
            responseMessage = "Товар с таким наименованием уже существует";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String setValues = String.format("SET name = '%s', price = %d, number = %d, " +
                "description = '%s'", name, price, number, description);
        whereString = String.format("WHERE id = %d", id);
        try {
            DataBase.getInstance().updateData(PRODUCTS_TABLE, setValues, whereString);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка обновления товара";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseError = false;
        responseMessage = "Товар №" + id + " обновлён";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseAllOrders(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String beginDate = requestMap.get(KEY_BEGIN_DATE);
        String endDate = requestMap.get(KEY_END_DATE);
        String strSearch = requestMap.get(KEY_SEARCH_REQUEST);
        ResultSet data = null;
        if(beginDate.isEmpty()) beginDate = "''";
        else beginDate = "'" + beginDate + "'";
        if(endDate.isEmpty()) endDate = "CURDATE()";
        else endDate = "'" + endDate + "'";
        String whereString = "";
        if(Boolean.valueOf(requestMap.get(KEY_IS_ALL_ORDERS))) {
            whereString = "WHERE id LIKE '%" + strSearch +
                    "%' AND date BETWEEN " + beginDate
                    + " AND " + endDate;
        } else {
            whereString = "WHERE id LIKE '%" + strSearch +
                    "%' AND user = " + requestMap.get(KEY_ID_USER_REQUEST)
                    + " AND date BETWEEN " + beginDate + " AND " + endDate;
        }
        try {
            data = DataBase.getInstance().selectData(ORDERS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Order> listOrders = DataBaseService.getInstance().parseOrdersData(data);
        responseData.put(KEY_NUMBER_RESPONSE, String.valueOf(listOrders.size()));
        for(int i = 1; i <= listOrders.size(); ++i) {
            responseData.put(KEY_ID_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getId()));
            responseData.put(KEY_USER_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getUser()));
            responseData.put(KEY_DATE_RESPONSE + i, listOrders.get(i - 1).getStringDate());
            responseData.put(KEY_STATE_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getState()));
        }
        responseError = false;
        responseMessage = "Данные найдены";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseUpdateStatusOrder(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        int id_order = 0;
        int status = 0;
        try {
            id_order = Integer.valueOf(requestMap.get(KEY_ID_ORDER_REQUEST));
            status = Integer.valueOf(requestMap.get(KEY_STATUS_ORDER_REQUEST));
        } catch(NumberFormatException e) {

        }
        String whereString = "WHERE id = " + id_order;
        String setString = "SET state = " + status;
        try {
            DataBase.getInstance().updateData(ORDERS_TABLE, setString, whereString);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка обновления заказа";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseError = false;
        responseMessage = "Заказ №" + id_order + " обновлён";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseGetAllUsers(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String search_login = "";
        try {
            search_login = requestMap.get(KEY_SEARCH_LOGIN_REQUEST);
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Ошибка обработки полученых данных";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String whereString = "WHERE login LIKE '%" + search_login + "%' AND access = 1";
        String sortString = "ORDER BY login";
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(USERS_TABLE, "*", whereString, sortString);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка получения списка пользователей";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<User> listUsers = DataBaseService.getInstance().parseUsersData(data);
        responseData.put(KEY_NUMBER_RESPONSE, String.valueOf(listUsers.size()));
        for(int i = 1; i <= listUsers.size(); ++i) {
            responseData.put(KEY_ID_USER_RESPONSE + i, String.valueOf(
                    listUsers.get(i - 1).getId()));
            responseData.put(KEY_LOGIN_RESPONSE + i,
                    listUsers.get(i - 1).getLogin());
            responseData.put(KEY_FNAME_RESPONSE + i,
                    listUsers.get(i - 1).getFirstName());
            responseData.put(KEY_SNAME_RESPONSE + i,
                    listUsers.get(i - 1).getSecondName());
            responseData.put(KEY_MNAME_RESPONSE + i,
                    listUsers.get(i - 1).getMiddleName());
            responseData.put(KEY_EMAIL_RESPONSE + i,
                    listUsers.get(i - 1).getEmail());
        }
        responseError = false;
        responseMessage = "Список менеджеров";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseUserDetailsById(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        int idUser = 0;
        try {
            idUser = Integer.valueOf(requestMap.get(KEY_ID_USER_REQUEST));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Отправлены некорректыные данные";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String whereString = String.format(
                "WHERE id = %d", idUser);
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(USERS_TABLE, "*", whereString, "");
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<User> listUsers = DataBaseService.getInstance().parseUsersData(data);
        if(listUsers.isEmpty()) {
            responseError = true;
            responseMessage = "Данные о пользователе не найдены";
        } else {
            responseData.put(KEY_ID_RESPONSE, String.valueOf(
                    listUsers.get(0).getId()));
            responseData.put(KEY_LOGIN_RESPONSE, listUsers.get(0).getLogin());
            responseData.put(KEY_SNAME_RESPONSE, listUsers.get(0).getSecondName());
            responseData.put(KEY_FNAME_RESPONSE, listUsers.get(0).getFirstName());
            responseData.put(KEY_MNAME_RESPONSE, listUsers.get(0).getMiddleName());
            responseData.put(KEY_EMAIL_RESPONSE, listUsers.get(0).getEmail());
            responseData.put(KEY_ACCESS_RESPONSE, String.valueOf(
                    listUsers.get(0).getAccess()));
            responseError = false;
            responseMessage = "Данные найдены";
        }
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseRemoveUser(DataRequest requestData) {
        // нужно возвращать товары на склад
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        int idUser = Integer.valueOf(requestMap.get(KEY_ID_USER_REQUEST));
        String whereString = String.format(
                "WHERE id = '%d'", idUser);
        try {
            DataBase.getInstance().deleteData(USERS_TABLE, whereString);
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseError = false;
        responseMessage = "Пользователь удалён";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseStatisticProducts(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String beginDate = requestMap.get(KEY_BEGIN_DATE);
        String endDate = requestMap.get(KEY_END_DATE);
        int limit = 0;
        try {
            limit = Integer.valueOf(requestMap.get(KEY_NUMBER_PRODUCT_REQUEST));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Ошибка обработки полученых данных";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        ResultSet data = null;
        if(beginDate.isEmpty()) beginDate = "''";
        else beginDate = "'" + beginDate + "'";
        if(endDate.isEmpty()) endDate = "CURDATE()";
        else endDate = "'" + endDate + "'";

        try {
            data = DataBase.getInstance().selectData(String.format(STATISTIC_PRODUCTS_SQL
                    , beginDate, endDate, limit));
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Product> listProducts = DataBaseService.getInstance().parseProductsData(data);
        if(listProducts.isEmpty()) {
            responseError = true;
            responseMessage = "За данный период не совершено ни одного заказа";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseData.put(KEY_NUMBER_RESPONSE, String.valueOf(listProducts.size()));
        for(int i = 1; i <= listProducts.size(); ++i) {
            responseData.put(KEY_ID_PRODUCT_RESPONSE + i, String.valueOf(
                    listProducts.get(i - 1).getId()));
            responseData.put(KEY_NAME_PRODUCT_RESPONSE + i,
                    listProducts.get(i - 1).getName());
            responseData.put(KEY_NUMBER_PRODUCT_RESPONSE + i,
                    String.valueOf(listProducts.get(i - 1).getNumber()));
            responseData.put(KEY_PRICE_PRODUCT_RESPONSE + i, String.valueOf(
                    listProducts.get(i - 1).getPrice()));
            responseData.put(KEY_DESCRIPTION_RESPONSE + i,
                    listProducts.get(i - 1).getDescription());
        }
        responseError = false;
        responseMessage = "Данные получены";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseStatisticUsers(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String beginDate = requestMap.get(KEY_BEGIN_DATE);
        String endDate = requestMap.get(KEY_END_DATE);
        int limit = 0;
        try {
            limit = Integer.valueOf(requestMap.get(KEY_NUMBER_PRODUCT_REQUEST));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Ошибка обработки полученых данных";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        ResultSet data = null;
        if(beginDate.isEmpty()) beginDate = "''";
        else beginDate = "'" + beginDate + "'";
        if(endDate.isEmpty()) endDate = "CURDATE()";
        else endDate = "'" + endDate + "'";

        try {
            data = DataBase.getInstance().selectData(String.format(STATISTIC_USERS_SQL
                    , beginDate, endDate, limit));
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<User> listUsers = DataBaseService.getInstance().parseUsersByNumberOrdersData(data);
        if(listUsers.isEmpty()) {
            responseError = true;
            responseMessage = "За данный период не совершено ни одного заказа";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseData.put(KEY_NUMBER_RESPONSE, String.valueOf(listUsers.size()));
        for(int i = 1; i <= listUsers.size(); ++i) {
            responseData.put(KEY_ID_USER_RESPONSE + i, String.valueOf(
                    listUsers.get(i - 1).getId()));
            responseData.put(KEY_LOGIN_RESPONSE + i,
                    listUsers.get(i - 1).getLogin());
            responseData.put(KEY_NUMBER_ORDERS_RESPONSE + i,
                    String.valueOf(listUsers.get(i - 1).getNumberOrders()));
            responseData.put(KEY_PRICE_ORDERS_RESPONSE + i, String.valueOf(
                    listUsers.get(i - 1).getSummOrders()));
        }
        responseError = false;
        responseMessage = "Данные получены";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseStatisticOrders(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        String beginDate = requestMap.get(KEY_BEGIN_DATE);
        String endDate = requestMap.get(KEY_END_DATE);
        int limit = 0;
        try {
            limit = Integer.valueOf(requestMap.get(KEY_NUMBER_PRODUCT_REQUEST));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Ошибка обработки полученых данных";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        ResultSet data = null;
        if(beginDate.isEmpty()) beginDate = "''";
        else beginDate = "'" + beginDate + "'";
        if(endDate.isEmpty()) endDate = "CURDATE()";
        else endDate = "'" + endDate + "'";

        try {
            data = DataBase.getInstance().selectData(String.format(STATISTIC_ORDERS_SQL
                    , beginDate, endDate, limit));
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        List<Order> listOrders = DataBaseService.getInstance().parseOrdersBySumPriceData(data);
        if(listOrders.isEmpty()) {
            responseError = true;
            responseMessage = "За данный период не совершено ни одного заказа";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        responseData.put(KEY_NUMBER_RESPONSE, String.valueOf(listOrders.size()));
        for(int i = 1; i <= listOrders.size(); ++i) {
            responseData.put(KEY_ID_ORDER_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getId()));
            responseData.put(KEY_USER_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getUser()));
            responseData.put(KEY_DATE_RESPONSE + i,
                    listOrders.get(i - 1).getStringDate());
            responseData.put(KEY_STATE_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getState()));
            responseData.put(KEY_PRICE_RESPONSE + i, String.valueOf(
                    listOrders.get(i - 1).getPrice()));
        }
        responseError = false;
        responseMessage = "Данные получены";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseStatisticOrdersForYear(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        int year = 0;
        try {
            year = Integer.valueOf(requestMap.get(KEY_YEAR_REQUEST));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Ошибка обработки полученых данных";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String beginDate = "" + year + "-01-01";
        String endDate = "" + year + "-12-31";

        Map<Integer, Integer> numberOrdersForMonths = new HashMap<Integer, Integer>();
        for(int i = 1; i <= 12; ++i) {
            numberOrdersForMonths.put(i, 0);
        }
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(String.format(STATISTIC_ORDERS_FOR_YEAR_SQL
                    , beginDate, endDate));
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        DataBaseService.getInstance().parseStatisticOrdersForYear(data, numberOrdersForMonths);
        for(Entry<Integer, Integer> month : numberOrdersForMonths.entrySet()) {
            responseData.put(KEY_MONTH_RESPONSE + month.getKey(), String.valueOf(
                    month.getValue()));
        }
        responseError = false;
        responseMessage = "Данные получены";
        return new DataResponse(responseError, responseMessage, responseData);
    }

    DataResponse getResponseStatisticOrdersForMonth(DataRequest requestData) {
        Map<String, String> responseData = new HashMap<String, String>();
        String responseMessage = "";
        boolean responseError = false;
        Map<String, String> requestMap = requestData.getData();
        int year = 0;
        int month = 0;
        try {
            year = Integer.valueOf(requestMap.get(KEY_YEAR_REQUEST));
            month = Integer.valueOf(requestMap.get(KEY_MONTH_REQUEST));
        } catch(NumberFormatException e) {
            responseError = true;
            responseMessage = "Ошибка обработки полученых данных";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        String beginDate = "" + year + "-" + month + "-01";
        String endDate = "" + year + "-" + month + "-31";
        int dayInMonth = new GregorianCalendar(year, month, 1).getActualMaximum(Calendar.DAY_OF_MONTH);
        Map<Integer, Integer> numberOrdersForMonths = new HashMap<Integer, Integer>();
        for(int i = 1; i <= dayInMonth; ++i) {
            numberOrdersForMonths.put(i, 0);
        }
        ResultSet data = null;
        try {
            data = DataBase.getInstance().selectData(String.format(STATISTIC_ORDERS_FOR_MONTH_SQL
                    , beginDate, endDate));
        } catch(SQLException e) {
            responseError = true;
            responseMessage = "Ошибка запроса к БД";
            return new DataResponse(responseError, responseMessage, responseData);
        }
        DataBaseService.getInstance().parseStatisticOrdersForMonth(data, numberOrdersForMonths);
        responseData.put(KEY_NUMBER_RESPONSE, String.valueOf(numberOrdersForMonths.size()));
        for(Entry<Integer, Integer> day : numberOrdersForMonths.entrySet()) {
            responseData.put(KEY_DAY_RESPONSE + day.getKey(), String.valueOf(
                    day.getValue()));
        }
        responseError = false;
        responseMessage = "Данные получены";
        return new DataResponse(responseError, responseMessage, responseData);
    }

}
