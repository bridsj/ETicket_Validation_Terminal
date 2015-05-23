package com.ticket.validation.terminal.restful;

import android.content.Context;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Request;
import retrofit.client.UrlConnectionClient;

public class ReqRestAdapter {

    public static RestAdapter getInstance(final Context context) {
        return getInstance(context, ApiConstants.API_BASE_URL);
    }

    public static RestAdapter getInstance(final Context context, String baseUrl) {
        RestAdapter restAdapter = new RestAdapter.Builder().setRequestInterceptor(new RequestInterceptor() {

            @Override
            public void intercept(RequestFacade requestFacade) {
                requestFacade.addHeader("from_client", "ETicketValidationTerminal");
                requestFacade.addQueryParam("device", "2");
                requestFacade.addQueryParam("outid", System.currentTimeMillis() + "");

            }

        }).setClient(new MyUrlConnectionClient())
                .setConverter(new JsonObjectConverter()).setEndpoint(baseUrl).build();
        return restAdapter;
    }

    private static class MyUrlConnectionClient extends UrlConnectionClient {
        @Override
        protected HttpURLConnection openConnection(Request request) throws IOException {
            HttpURLConnection connection =
                    (HttpURLConnection) new URL(request.getUrl()).openConnection();
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);
            return connection;
        }
    }


}
