package com.kevinus22.kevinus22;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// Класс выполняется асинхронно (extends Thread)
public class HttpURLConnectionExample extends Thread {

    //Объявляем переменную для кода ответа сервера
    static int responsecode;

    @Override
    public void run()
    {
        URL myUrl = null;
        try {
            myUrl = new URL("https://kevinus21.ru/NmqFdKqf");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("ОШИБКА");

        }
        HttpURLConnection myUrlCon = null;
        try {
            myUrlCon = (HttpURLConnection) myUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ОШИБКА");
        }

        System.out.println("Метод запроса: " +
                myUrlCon.getRequestMethod());

        try {
            responsecode = myUrlCon.getResponseCode();
            System.out.println("Код ответа: " +
                    responsecode);
        } catch (IOException e) {
            System.out.println("ОШИБКА11");
            e.printStackTrace();
            System.out.println("ОШИБКА22");
            responsecode = 404;
        }
    }

    public int otvetservera ()
    {
        return responsecode;
    }

}
