package org.fr2eman.network;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.fr2eman.service.DataService;

public class MultiThreadServer {

    private static MultiThreadServer instance = new MultiThreadServer();

    private ServerSocket m_serverSocket;

    public static MultiThreadServer getInstance() {
        return instance;
    }

    public void startServer() throws IOException {
        try {
            m_serverSocket = new ServerSocket(DataService.getInstance().getPortServerSocket());
        } catch (BindException e) {
            System.out.println("Сервер не запущен. Порт " +
                    DataService.getInstance().getPortServerSocket() + " занят.");
            throw e;
        } catch (IOException e) {
            System.out.println("Сервер не запущен.");
            throw e;
        }
        System.out.println("Статус сервера: Запущен");
        System.out.println("Адресс сервера: " + m_serverSocket.getLocalSocketAddress());
        while (true) {
            Socket socket = null;
            try {
                socket = m_serverSocket.accept();
            } catch (IOException e) {
                break;
            }
            new Thread(new ClientConnect(socket)).start();
        }
    }

    public void stopServer() throws IOException {
        this.m_serverSocket.close();
    }

}
