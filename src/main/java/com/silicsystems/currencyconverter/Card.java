package com.silicsystems.currencyconverter;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jabyte on 10/31/17.
 */
public class Card {
    private static final String TAG = MainActivity.class.getName();
    private String baseCurrency;
    private String cryptoCurrency;
    private float rate;
    private static ArrayList<Card> cards;

    public Card(String baseCurrency, String cryptoCurrency) {
        try {
            this.baseCurrency = baseCurrency;
            this.cryptoCurrency = cryptoCurrency;
        } catch (Exception exc) {
            Log.i(TAG, exc.getMessage());
        }
    }

    public float convert(float amount) {
        try
        {
            if( amount > 0 )
                return amount * getRate();
        } catch (Exception exc) {
            Log.i(TAG, exc.getMessage());
        }
        return 0;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setCryptoCurrency(String cryptocurrency) {
        this.cryptoCurrency = cryptocurrency;
    }

    public void setRate(float rate) {
        try {
            if( rate > 0 )
                this.rate = rate;
        }
        catch (Exception exc)
        {

        }
    }

    public String getBaseCurrency() {
        return this.baseCurrency;
    }

    public String getCryptoCurrency() {
        return this.cryptoCurrency;
    }

    public float getRate() {
        return this.rate;
    }

    public static void addCard(Card card) {
        if (card != null) {
            cards = new ArrayList<>();
            cards.add(card);
        } else{
            cards.add(card);
        }
    }

    public static ArrayList<Card> getCards()
    {
        if (cards != null) {
            cards = new ArrayList<>();
        }
        return cards;
    }
}