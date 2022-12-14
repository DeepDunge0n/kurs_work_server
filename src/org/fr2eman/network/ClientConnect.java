package org.fr2eman.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.fr2eman.send.*;

public class ClientConnect implements Runnable {

    Socket m_clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    public ClientConnect(Socket clientSocket) {
        this.m_clientSocket = clientSocket;
    }

    @Override
    public void run() {
        DataRequest requestData = null;
        try {
            input = new ObjectInputStream(m_clientSocket.getInputStream());
            output = new ObjectOutputStream(m_clientSocket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        try {
            try {
                requestData = (DataRequest)input.readObject();
            } catch (ClassNotFoundException e) {
            }
        } catch (IOException e) {
            return;
        }
        try {
            output.writeObject(RequestHandler.getInstance().getResponse(requestData));
        } catch (IOException e) {
        }
    }
}
