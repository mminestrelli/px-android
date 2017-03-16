package com.mercadopago.uicontrollers.discounts;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mercadopago.R;
import com.mercadopago.model.Currency;
import com.mercadopago.model.Discount;
import com.mercadopago.util.CurrenciesUtil;

import java.math.BigDecimal;

/**
 * Created by mromar on 1/19/17.
 */

public class DiscountRowView implements DiscountView {


    //Local vars
    private String mCurrencyId;
    private BigDecimal mTransactionAmount;
    private BigDecimal mShippingCost;
    private Context mContext;
    private Discount mDiscount;
    private Boolean mShortRowEnabled;
    private Boolean mDiscountEnabled;
    private Boolean mShowArrow;
    private Boolean mShowSeparator;

    //Views
    private View mView;
    private TextView mTotalAmountTextView;
    private TextView mDiscountAmountTextView;
    private TextView mDiscountOffTextView;
    private ImageView mDiscountArrow;
    private LinearLayout mHighDiscountRow;
    private LinearLayout mHasDiscountLinearLayout;
    private LinearLayout mHasDirectDiscountLinearLayout;
    private LinearLayout mDiscountDetail;
    private View mDiscountSeparator;
    private TextView mLabel;

    public DiscountRowView(Context context, Discount discount, BigDecimal transactionAmount, BigDecimal shippingCost, String currencyId, Boolean shortRowEnabled,
                           Boolean discountEnabled, Boolean showArrow, Boolean showSeparator) {
        mContext = context;
        mDiscount = discount;
        mTransactionAmount = transactionAmount;
        mCurrencyId = currencyId;
        mShortRowEnabled = shortRowEnabled;
        mDiscountEnabled = discountEnabled;
        mShowArrow = showArrow;
        mShowSeparator = showSeparator;
        mShippingCost = shippingCost;
    }

    @Override
    public void draw() {
        if (isDiscountEnabled()) {
            if (mDiscount == null) {
                showHasDiscountRow();
            } else {
                showDiscountDetailRow();
            }
        } else {
            showDefaultRow();
        }
    }

    private void showDefaultRow() {
        if (!isShortRowEnabled()) {
            showHighDefaultRow();
        }
    }

    private void showHighDefaultRow() {
        if (isAmountValid(mTransactionAmount) && CurrenciesUtil.isValidCurrency(mCurrencyId)) {
            if(shouldShowShippingCost()) {
                mLabel.setText(R.string.mpsdk_product_and_shipping_cost_label);
            }
            BigDecimal finalAmount = mShippingCost == null ? mTransactionAmount : mTransactionAmount.add(mShippingCost);
            mTotalAmountTextView.setText(getFormattedAmount(finalAmount, mCurrencyId));
        } else {
            mHighDiscountRow.setVisibility(View.GONE);
        }
    }

    private boolean shouldShowShippingCost() {
        return mShippingCost != null && mShippingCost.compareTo(BigDecimal.ZERO) == 1;
    }

    private void showHasDiscountRow() {
        if (isShortRowEnabled()) {
            drawShortHasDiscountRow();
        } else {
            drawHighHasDiscountRow();
        }
    }

    private void showDiscountDetailRow() {
        if (isShortRowEnabled()) {
            drawShortDiscountDetailRow();
        } else {
            drawHighDiscountDetailRow();
        }
    }

    private void drawShortDiscountDetailRow() {
        if (isAmountValid(mDiscount.getCouponAmount())) {
            mDiscountDetail.setVisibility(View.VISIBLE);
            mHasDiscountLinearLayout.setVisibility(View.GONE);

            setDiscountOff();
        }
    }

    private void drawHighDiscountDetailRow() {
        if (areValidParameters()) {
            mHasDirectDiscountLinearLayout.setVisibility(View.VISIBLE);
            mDiscountAmountTextView.setVisibility(View.VISIBLE);
            mHasDiscountLinearLayout.setVisibility(View.GONE);

            setArrowVisibility();
            setSeparatorVisibility();
            setDiscountOff();
            setTotalAmountWithDiscount();
            setTotalAmount();
        } else {
            mHasDirectDiscountLinearLayout.setVisibility(View.GONE);
            mHighDiscountRow.setVisibility(View.GONE);
        }
    }

    private Boolean areValidParameters() {
        return isDiscountCurrencyIdValid() && isAmountValid(mTransactionAmount) && isAmountValid(mDiscount.getCouponAmount()) && isCampaignIdValid();
    }

    private void drawShortHasDiscountRow() {
        mDiscountDetail.setVisibility(View.GONE);
        mHasDiscountLinearLayout.setVisibility(View.VISIBLE);
    }

    private void drawHighHasDiscountRow() {
        if (isAmountValid(mTransactionAmount) && CurrenciesUtil.isValidCurrency(mCurrencyId)) {
            mHasDiscountLinearLayout.setVisibility(View.VISIBLE);
            mTotalAmountTextView.setText(getFormattedAmount(mTransactionAmount, mCurrencyId));
        } else {
            mHighDiscountRow.setVisibility(View.GONE);
        }
    }

    private void setArrowVisibility() {
        if (mShowArrow != null && !mShowArrow) {
            mDiscountArrow.setVisibility(View.GONE);
        }
    }

    private void setSeparatorVisibility() {
        if (mShowSeparator != null && !mShowSeparator) {
            mDiscountSeparator.setVisibility(View.GONE);
        }
    }

    private void setDiscountOff() {
        if (isAmountOffValid() && mDiscount.getAmountOff().compareTo(BigDecimal.ZERO) > 0) {
            Currency currency = CurrenciesUtil.getCurrency(mDiscount.getCurrencyId());
            String amount = currency.getSymbol() + " " + mDiscount.getAmountOff();

            mDiscountOffTextView.setText(amount);
        } else if (isPercentOffValid() && mDiscount.getPercentOff().compareTo(BigDecimal.ZERO) > 0) {
            String discountOff = mContext.getResources().getString(R.string.mpsdk_discount_percent_off,
                    String.valueOf(mDiscount.getPercentOff()));

            mDiscountOffTextView.setText(discountOff);
        } else {
            Currency currency = CurrenciesUtil.getCurrency(mDiscount.getCurrencyId());
            String amount = currency.getSymbol() + " " + mDiscount.getCouponAmount();

            mDiscountOffTextView.setText(amount);
        }
    }

    private void setTotalAmountWithDiscount() {
        BigDecimal finalAmount = mShippingCost == null ? mDiscount.getAmountWithDiscount(mTransactionAmount)
                : mDiscount.getAmountWithDiscount(mTransactionAmount).add(mShippingCost);
        mDiscountAmountTextView.setText(getFormattedAmount(finalAmount, mDiscount.getCurrencyId()));
    }

    private void setTotalAmount() {

        BigDecimal amount = mTransactionAmount;
        if(shouldShowShippingCost()) {
            mLabel.setText(R.string.mpsdk_product_and_shipping_cost_label);
            amount = mTransactionAmount.add(mShippingCost);
        }
        mTotalAmountTextView.setText(getFormattedAmount(amount, mDiscount.getCurrencyId()));
        mTotalAmountTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        mView.setOnClickListener(listener);
    }

    @Override
    public void initializeControls() {
        mHighDiscountRow = (LinearLayout) mView.findViewById(R.id.mpsdkDiscountRow);
        mTotalAmountTextView = (TextView) mView.findViewById(R.id.mpsdkTotalAmount);
        mDiscountAmountTextView = (TextView) mView.findViewById(R.id.mpsdkDiscountAmount);
        mDiscountOffTextView = (TextView) mView.findViewById(R.id.mpsdkDiscountOff);
        mHasDiscountLinearLayout = (LinearLayout) mView.findViewById(R.id.mpsdkHasDiscount);
        mHasDirectDiscountLinearLayout = (LinearLayout) mView.findViewById(R.id.mpsdkHasDirectDiscount);
        mDiscountDetail = (LinearLayout) mView.findViewById(R.id.mpsdkDiscountDetail);
        mDiscountArrow = (ImageView) mView.findViewById(R.id.mpsdkDiscountArrow);
        mDiscountSeparator = mView.findViewById(R.id.mpsdkDiscountSeparator);
        mLabel = (TextView) mView.findViewById(R.id.mpsdkLabel);
    }

    @Override
    public View inflateInParent(ViewGroup parent, boolean attachToRoot) {
        parent.removeAllViews();
        if (isShortRowEnabled()) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.mpsdk_row_guessing_discount, parent, attachToRoot);
        } else {
            mView = LayoutInflater.from(mContext).inflate(R.layout.mpsdk_row_discount, parent, attachToRoot);
        }
        return mView;
    }

    @Override
    public View getView() {
        return mView;
    }

    private Boolean isShortRowEnabled() {
        return mShortRowEnabled != null && mShortRowEnabled;
    }

    private Boolean isDiscountEnabled() {
        return mDiscountEnabled == null || mDiscountEnabled;
    }

    private Spanned getFormattedAmount(BigDecimal amount, String currencyId) {
        String originalNumber = CurrenciesUtil.formatNumber(amount, currencyId);
        Spanned amountText = CurrenciesUtil.formatCurrencyInText(amount, currencyId, originalNumber, false, true);
        return amountText;
    }

    private Boolean isAmountValid(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    private Boolean isAmountOffValid() {
        return mDiscount != null && mDiscount.getAmountOff() != null && isAmountValid(mDiscount.getAmountOff());
    }

    private Boolean isPercentOffValid() {
        return mDiscount != null && mDiscount.getPercentOff() != null && mDiscount.getPercentOff().compareTo(BigDecimal.ZERO) >= 0;
    }

    private Boolean isDiscountCurrencyIdValid() {
        return mDiscount != null && mDiscount.getCurrencyId() != null && CurrenciesUtil.isValidCurrency(mDiscount.getCurrencyId());
    }

    private Boolean isCampaignIdValid() {
        return mDiscount.getId() != null;
    }
}
