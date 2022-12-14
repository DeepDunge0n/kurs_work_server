package org.fr2eman;

import org.fr2eman.database.DataBase;
import org.fr2eman.network.MultiThreadServer;
import org.fr2eman.service.LoaderSettings;

public class Main {

    private static final String CONFIG = "resource/config.conf";

    public static void main(String[] args) {

        try {

            LoaderSettings.getInstance().initConfigurations(CONFIG);

            DataBase.getInstance().initDatabaseConfigurations();

            DataBase.getInstance().connectToBD();

            MultiThreadServer.getInstance().startServer();

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Завершение программы...");
            return;
        }

    }
}
