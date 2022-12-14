package org.fr2eman.network;

import org.fr2eman.send.*;

public class RequestHandler {

    final public static int TYPE_AUTHORIZATION = 1;
    final public static int TYPE_REGISTRATION = 2;
    final public static int TYPE_GET_PRODUCTS = 3;
    final public static int TYPE_DESCRIPTION_PRODUCT = 4;
    final public static int TYPE_MAKE_ORDER = 5;
    final public static int TYPE_DATAS_USER = 6;
    final public static int TYPE_ORDERS_USER = 7;
    final public static int TYPE_ORDER_DETAILS = 8;
    final public static int TYPE_CANCEL_ORDER = 9;
    final public static int TYPE_ADD_PRODUCT = 10;
    final public static int TYPE_UPDATE_PRODUCT = 11;
    final public static int TYPE_ALL_ORDERS = 12;
    final public static int TYPE_UPDATE_STATUS_ORDER = 13;
    final public static int TYPE_ALL_USERS = 14;
    final public static int TYPE_USER_DETAILS_BY_ID = 15;
    final public static int TYPE_REMOVE_USER = 16;
    final public static int TYPE_STATISTIC_PRODUCTS = 17;
    final public static int TYPE_STATISTIC_USERS = 18;
    final public static int TYPE_STATISTIC_ORDERS = 19;
    final public static int TYPE_STATISTIC_ORDERS_FOR_YEAR = 20;
    final public static int TYPE_STATISTIC_ORDERS_FOR_MONTH = 21;

    private static RequestHandler instance = new RequestHandler();

    public static RequestHandler getInstance() {
        return instance;
    }

    public DataResponse getResponse(DataRequest requestData) {

        switch(requestData.getType()) {
            case TYPE_AUTHORIZATION: {
                return BuilderResponse.getInstance().getResponseAuthorization(requestData);
            }
            case TYPE_REGISTRATION: {
                return BuilderResponse.getInstance().getResponseRegistration(requestData);
            }
            case TYPE_GET_PRODUCTS: {
                return BuilderResponse.getInstance().getResponseProducts(requestData);
            }
            case TYPE_DESCRIPTION_PRODUCT: {
                return BuilderResponse.getInstance().getResponseDesctiptionProduct(requestData);
            }
            case TYPE_MAKE_ORDER: {
                return BuilderResponse.getInstance().getResponseMakeOrder(requestData);
            }
            case TYPE_DATAS_USER: {
                return BuilderResponse.getInstance().getResponseDatasUser(requestData);
            }
            case TYPE_ORDERS_USER: {
                return BuilderResponse.getInstance().getResponseOrdersUser(requestData);
            }
            case TYPE_ORDER_DETAILS: {
                return BuilderResponse.getInstance().getResponseOrderDetails(requestData);
            }
            case TYPE_CANCEL_ORDER: {
                return BuilderResponse.getInstance().getResponseCancelOrder(requestData);
            }
            case TYPE_ADD_PRODUCT: {
                return BuilderResponse.getInstance().getResponseAddProduct(requestData);
            }
            case TYPE_UPDATE_PRODUCT: {
                return BuilderResponse.getInstance().getResponseUpdateProduct(requestData);
            }
            case TYPE_ALL_ORDERS: {
                return BuilderResponse.getInstance().getResponseAllOrders(requestData);
            }
            case TYPE_UPDATE_STATUS_ORDER: {
                return BuilderResponse.getInstance().getResponseUpdateStatusOrder(requestData);
            }
            case TYPE_ALL_USERS: {
                return BuilderResponse.getInstance().getResponseGetAllUsers(requestData);
            }
            case TYPE_USER_DETAILS_BY_ID: {
                return BuilderResponse.getInstance().getResponseUserDetailsById(requestData);
            }
            case TYPE_REMOVE_USER: {
                return BuilderResponse.getInstance().getResponseRemoveUser(requestData);
            }
            case TYPE_STATISTIC_PRODUCTS: {
                return BuilderResponse.getInstance().getResponseStatisticProducts(requestData);
            }
            case TYPE_STATISTIC_USERS: {
                return BuilderResponse.getInstance().getResponseStatisticUsers(requestData);
            }
            case TYPE_STATISTIC_ORDERS: {
                return BuilderResponse.getInstance().getResponseStatisticOrders(requestData);
            }
            case TYPE_STATISTIC_ORDERS_FOR_YEAR: {
                return BuilderResponse.getInstance().getResponseStatisticOrdersForYear(requestData);
            }
            case TYPE_STATISTIC_ORDERS_FOR_MONTH: {
                return BuilderResponse.getInstance().getResponseStatisticOrdersForMonth(requestData);
            }
            default: {
                return new DataResponse(true, "Обработчик для этого запроса не найден");
            }
        }
    }
}
