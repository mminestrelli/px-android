package com.mercadopago.px_tracking.services;

import android.content.Context;
import android.util.Log;

import com.mercadopago.px_tracking.model.EventTrackIntent;
import com.mercadopago.px_tracking.model.PaymentIntent;
import com.mercadopago.px_tracking.model.TrackingIntent;
import com.mercadopago.px_tracking.utils.HttpClientUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vaserber on 6/5/17.
 */

public class MPTrackingService {

    private static final String API_BETA_VERSION = "beta";
    private static final String API_PROD_VERSION = "v1";
    private static final String BASE_URL = "https://api.mercadopago.com/";
    private static MPTrackingService mTrackingService;

    private String mTrackPath = API_PROD_VERSION;

    protected MPTrackingService() {

    }

    synchronized public static MPTrackingService getInstance(){
        if(mTrackingService == null) {
            mTrackingService = new MPTrackingService();
        }
        return mTrackingService;
    }

    public void enableTestMode(){
        mTrackPath = API_BETA_VERSION;
    }

    private Retrofit getRetrofit(Context context) {
        return new Retrofit.Builder()
                .client(HttpClientUtil.getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public void trackToken(TrackingIntent trackingIntent, Context context) {

        Retrofit retrofit = getRetrofit(context);
        TrackingService service = retrofit.create(TrackingService.class);

        Call<Void> call = service.trackToken(trackingIntent, mTrackPath);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 400) {
                    Log.e("Failure","Error 400, parameter invalid");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Failure","Service failure");
            }
        });
    }

    public void trackPaymentId(PaymentIntent paymentIntent, Context context) {

        Retrofit retrofit = getRetrofit(context);
        TrackingService service = retrofit.create(TrackingService.class);

        Call<Void> call = service.trackPaymentId(paymentIntent, mTrackPath);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 400) {
                    Log.e("Failure","Error 400, parameter invalid");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Failure","Service failure");
            }
        });
    }

    public void trackEvent(EventTrackIntent eventTrackIntent, Context context) {
        Retrofit retrofit = getRetrofit(context);
        TrackingService service = retrofit.create(TrackingService.class);

        Call<Void> call = service.trackEvents(eventTrackIntent, mTrackPath);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 400) {
                    Log.e("Failure","Error 400, parameter invalid");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Failure","Service failure");
            }
        });
    }
}
