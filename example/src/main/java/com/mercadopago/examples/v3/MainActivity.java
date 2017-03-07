package com.mercadopago.examples.v3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mercadopago.examples.R;
import com.mercadopago.examples.v3.wallet.CallbacksResponseTestActivity;
import com.mercadopago.examples.v3.wallet.ResultCodesTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v3);

    }

    public void onWalletCallbacksClicked(View view) {
        startActivity(new Intent(this, CallbacksResponseTestActivity.class));
    }

    public void onWalletResultCodesClicked(View view) {
        startActivity(new Intent(this, ResultCodesTestActivity.class));
    }
}
