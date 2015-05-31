package com.ticket.validation.terminal.helper;

/**
 * Created by dengshengjin on 15/5/31.
 */
public interface IDCardStrategy {

    void startRecognition(RecognitionCallback callback);

    interface RecognitionCallback {

        void onErrorIDCard();

        void onStartIDCard();

        void onFinishIDCard(String idcard);

        void onFailIDCard();
    }
}
