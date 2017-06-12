package com.mercadopago.mocks;

import com.google.gson.reflect.TypeToken;
import com.mercadopago.model.Card;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.utils.ResourcesUtil;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by mromar on 4/17/17.
 * =======
 * import com.mercadopago.model.Card;
 * import com.mercadopago.util.JsonUtil;
 * import com.mercadopago.utils.ResourcesUtil;
 * <p>
 * /**
 * Created by vaserber on 4/21/17.
 * >>>>>>> v3
 */

public class Cards {

    private Cards() {
    }

    public static Card getCard() {
        String json = ResourcesUtil.getStringResource("card.json");
        return JsonUtil.getInstance().fromJson(json, Card.class);
    }

    public static List<Card> getCardsMLA() {
        List<Card> cards;
        String json = ResourcesUtil.getStringResource("cards_MLA.json");

        try {
            Type listType = new TypeToken<List<Card>>() {
            }.getType();
            cards = JsonUtil.getInstance().getGson().fromJson(json, listType);
        } catch (Exception ex) {
            cards = null;
        }
        return cards;
    }
}