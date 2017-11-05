package com.silicsystems.currencyconverter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by jabyte on 11/2/17.
 */

public class ConversionTab extends Fragment  implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    private RequestQueue queue;
    private Adapter adapter;
    private Card card;
    private JsonFetcher fetcher;
    public static final String TAG = ConversionTab.class.getName();

    private Spinner fromCurrencySpinner;
    private Spinner toCurrencySpinner;
    private Button convertButton;
    private Button saveCardButton;
    private EditText amountBox;
    private TextView resultDisplay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Adapter.getInstance(this.getContext()).getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conversion_tab, container, false);

        try {
            fromCurrencySpinner = (Spinner)rootView.findViewById(R.id.fromCurrencySpinner);
            toCurrencySpinner = (Spinner)rootView.findViewById(R.id.toCurrencySpinner);
            amountBox = (EditText)rootView.findViewById(R.id.amountBox);
            resultDisplay = (TextView)rootView.findViewById(R.id.displaTextView);

            convertButton = (Button)rootView.findViewById(R.id.convertButton);
            saveCardButton = (Button)rootView.findViewById(R.id.saveCardButton);
            //
            convertButton.setOnClickListener(this);
            saveCardButton.setOnClickListener(this);
        } catch (Exception exc){
            Log.d(TAG, exc.getMessage());
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        String fromcurr;
        String tocurr;
        String url;

        fromcurr = fromCurrencySpinner.getSelectedItem().toString();
        tocurr = toCurrencySpinner.getSelectedItem().toString();
        card = new Card( fromcurr, tocurr );

        switch (view.getId()) {
            case R.id.convertButton:
                url = "https://min-api.cryptocompare.com/data/price?fsym=" + card.getBaseCurrency() + "&tsyms=" + card.getCryptoCurrency();
                try
                {
                    fetcher = new JsonFetcher(Request.Method.GET, url, new JSONObject(), this, this);
                    fetcher.setTag(TAG);
                    queue.add(fetcher);
                }
                catch (Exception exc )
                {
                    Log.i(TAG, exc.getMessage());
                }
                break;

            case R.id.saveCardButton:
                try
                {
                    Toast.makeText( getContext(), "Saving card...", Toast.LENGTH_LONG );
                    Card.addCard( card );
                }
                catch (Exception exc )
                {
                    Log.i(TAG, exc.getMessage());
                }

            break;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    public void onErrorResponse(VolleyError error) {
        Log.i(TAG, error.getMessage());
        Toast.makeText( getContext(), "ERROR!\nPlease try again later.", Toast.LENGTH_SHORT).show();
    }

    public void onResponse(JSONObject response) {
        String rateString;
        float amount;
        float value;

        try {
            resultDisplay.setText( "" );
            if( card == null)
                card = new Card( fromCurrencySpinner.getSelectedItem().toString(), toCurrencySpinner.getSelectedItem().toString());

            rateString = response.getString(card.getCryptoCurrency());
            card.setRate(Float.parseFloat( rateString ) );
            amount = Float.parseFloat(amountBox.getText().toString());

            if( amountBox.getText().toString() != "" )
                value = card.convert( amount );
            else
                value = card.convert( 1f );
            resultDisplay.setText( String.valueOf( value ) );
        } catch (JSONException e) {
            Log.i(TAG, e.getMessage());
            Toast.makeText( getContext(), "ERROR!\nSorry for this. Try again.", Toast.LENGTH_SHORT);
        }
    }

}
