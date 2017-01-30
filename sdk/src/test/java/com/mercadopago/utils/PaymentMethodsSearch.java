package com.mercadopago.utils;

import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearch;
import com.mercadopago.util.JsonUtil;

/**
 * Created by marlanti on 1/30/17.
 */

public class PaymentMethodsSearch {

    private static final String paymentMethodsJson = "{\n" +
            "   \"groups\": [\n" +
            "      {\n" +
            "         \"children\": [\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"\",\n" +
            "               \"description\": \"Tarjeta de crédito\",\n" +
            "               \"id\": \"credit_card\",\n" +
            "               \"type\": \"payment_type\"\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"\",\n" +
            "               \"description\": \"Tarjeta de débito\",\n" +
            "               \"id\": \"debit_card\",\n" +
            "               \"type\": \"payment_type\"\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"\",\n" +
            "               \"description\": \"Tarjeta prepaga de MercadoPago\",\n" +
            "               \"id\": \"prepaid_card\",\n" +
            "               \"type\": \"payment_type\"\n" +
            "            }\n" +
            "         ],\n" +
            "         \"children_header\": \"¿Con que tarjeta?\",\n" +
            "         \"comment\": null,\n" +
            "         \"description\": \"Tarjetas\",\n" +
            "         \"id\": \"cards\",\n" +
            "         \"type\": \"group\",\n" +
            "         \"show_icon\": true\n" +
            "      },\n" +
            "      {\n" +
            "         \"children\": [\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"Oxxo\",\n" +
            "               \"id\": \"oxxo\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"7eleven\",\n" +
            "               \"id\": \"7eleven\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"Telecomm\",\n" +
            "               \"id\": \"telecomm\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"BBVA Bancomer\",\n" +
            "               \"id\": \"bancomer_ticket\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"Banamex\",\n" +
            "               \"id\": \"banamex_ticket\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"Santander\",\n" +
            "               \"id\": \"serfin_ticket\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"Gestopago\",\n" +
            "               \"id\": \"gestopago\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            }\n" +
            "         ],\n" +
            "         \"children_header\": \"¿Dónde quieres pagar?\",\n" +
            "         \"comment\": null,\n" +
            "         \"description\": \"Efectivo\",\n" +
            "         \"id\": \"ticket\",\n" +
            "         \"type\": \"payment_type\",\n" +
            "         \"show_icon\": true\n" +
            "      },\n" +
            "      {\n" +
            "         \"children\": [\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"BBVA Bancomer\",\n" +
            "               \"id\": \"bancomer_bank_transfer\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"Banamex\",\n" +
            "               \"id\": \"banamex_bank_transfer\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"Santander\",\n" +
            "               \"id\": \"serfin_bank_transfer\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            },\n" +
            "            {\n" +
            "               \"children\": null,\n" +
            "               \"children_header\": null,\n" +
            "               \"comment\": \"Se acreditará en 7 hábiles\",\n" +
            "               \"description\": \"PagoEfectivo\",\n" +
            "               \"id\": \"pagoefectivo_atm_pagoefectivo_atm\",\n" +
            "               \"type\": \"payment_method\",\n" +
            "               \"show_icon\": true\n" +
            "            }\n" +
            "         ],\n" +
            "         \"children_header\": \"¿Cuál es tu banco?\",\n" +
            "         \"comment\": null,\n" +
            "         \"description\": \"Transferencia Bancaria\",\n" +
            "         \"id\": \"bank_transfer\",\n" +
            "         \"type\": \"payment_type\",\n" +
            "         \"show_icon\": true\n" +
            "      }\n" +
            "   ],\n" +
            "   \"payment_methods\": [\n" +
            "      {\n" +
            "         \"id\": \"debmaster\",\n" +
            "         \"name\": \"Mastercard Débito\",\n" +
            "         \"payment_type_id\": \"debit_card\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/debmaster.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/debmaster.gif\",\n" +
            "         \"deferred_capture\": \"unsupported\",\n" +
            "         \"settings\": [\n" +
            "            {\n" +
            "               \"bin\": {\n" +
            "                  \"pattern\": \"^5\",\n" +
            "                  \"exclusion_pattern\": null,\n" +
            "                  \"installments_pattern\": \"^5\"\n" +
            "               },\n" +
            "               \"card_number\": {\n" +
            "                  \"length\": 16,\n" +
            "                  \"validation\": \"standard\"\n" +
            "               },\n" +
            "               \"security_code\": {\n" +
            "                  \"mode\": \"mandatory\",\n" +
            "                  \"length\": 3,\n" +
            "                  \"card_location\": \"back\"\n" +
            "               }\n" +
            "            }\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "            \"cardholder_name\",\n" +
            "            \"issuer_id\"\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 5,\n" +
            "         \"max_allowed_amount\": 200000,\n" +
            "         \"accreditation_time\": 1440\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"debvisa\",\n" +
            "         \"name\": \"Visa Débito\",\n" +
            "         \"payment_type_id\": \"debit_card\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/debvisa.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/debvisa.gif\",\n" +
            "         \"deferred_capture\": \"unsupported\",\n" +
            "         \"settings\": [\n" +
            "            {\n" +
            "               \"bin\": {\n" +
            "                  \"pattern\": \"^4\",\n" +
            "                  \"exclusion_pattern\": null,\n" +
            "                  \"installments_pattern\": \"^4\"\n" +
            "               },\n" +
            "               \"card_number\": {\n" +
            "                  \"length\": 16,\n" +
            "                  \"validation\": \"standard\"\n" +
            "               },\n" +
            "               \"security_code\": {\n" +
            "                  \"mode\": \"mandatory\",\n" +
            "                  \"length\": 3,\n" +
            "                  \"card_location\": \"back\"\n" +
            "               }\n" +
            "            }\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "            \"cardholder_name\",\n" +
            "            \"issuer_id\"\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 5,\n" +
            "         \"max_allowed_amount\": 200000,\n" +
            "         \"accreditation_time\": 1440\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"master\",\n" +
            "         \"name\": \"Mastercard\",\n" +
            "         \"payment_type_id\": \"credit_card\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/master.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/master.gif\",\n" +
            "         \"deferred_capture\": \"unsupported\",\n" +
            "         \"settings\": [\n" +
            "            {\n" +
            "               \"bin\": {\n" +
            "                  \"pattern\": \"^5\",\n" +
            "                  \"exclusion_pattern\": null,\n" +
            "                  \"installments_pattern\": \"^5\"\n" +
            "               },\n" +
            "               \"card_number\": {\n" +
            "                  \"length\": 16,\n" +
            "                  \"validation\": \"standard\"\n" +
            "               },\n" +
            "               \"security_code\": {\n" +
            "                  \"mode\": \"mandatory\",\n" +
            "                  \"length\": 3,\n" +
            "                  \"card_location\": \"back\"\n" +
            "               }\n" +
            "            }\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "            \"cardholder_name\",\n" +
            "            \"issuer_id\"\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 5,\n" +
            "         \"max_allowed_amount\": 200000,\n" +
            "         \"accreditation_time\": 2880\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"mercadopagocard\",\n" +
            "         \"name\": \"Tarjeta MercadoPago\",\n" +
            "         \"payment_type_id\": \"prepaid_card\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/mercadopagocard.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/mercadopagocard.gif\",\n" +
            "         \"deferred_capture\": \"unsupported\",\n" +
            "         \"settings\": [\n" +
            "            {\n" +
            "               \"bin\": {\n" +
            "                  \"pattern\": \"^539978\",\n" +
            "                  \"exclusion_pattern\": null,\n" +
            "                  \"installments_pattern\": \"^539978\"\n" +
            "               },\n" +
            "               \"card_number\": {\n" +
            "                  \"length\": 16,\n" +
            "                  \"validation\": \"standard\"\n" +
            "               },\n" +
            "               \"security_code\": {\n" +
            "                  \"mode\": \"mandatory\",\n" +
            "                  \"length\": 3,\n" +
            "                  \"card_location\": \"back\"\n" +
            "               }\n" +
            "            }\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "            \"cardholder_name\"\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 5,\n" +
            "         \"max_allowed_amount\": 200000,\n" +
            "         \"accreditation_time\": 1440\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"visa\",\n" +
            "         \"name\": \"Visa\",\n" +
            "         \"payment_type_id\": \"credit_card\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/visa.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/visa.gif\",\n" +
            "         \"deferred_capture\": \"unsupported\",\n" +
            "         \"settings\": [\n" +
            "            {\n" +
            "               \"bin\": {\n" +
            "                  \"pattern\": \"^4\",\n" +
            "                  \"exclusion_pattern\": null,\n" +
            "                  \"installments_pattern\": \"^4\"\n" +
            "               },\n" +
            "               \"card_number\": {\n" +
            "                  \"length\": 16,\n" +
            "                  \"validation\": \"standard\"\n" +
            "               },\n" +
            "               \"security_code\": {\n" +
            "                  \"mode\": \"mandatory\",\n" +
            "                  \"length\": 3,\n" +
            "                  \"card_location\": \"back\"\n" +
            "               }\n" +
            "            }\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "            \"cardholder_name\",\n" +
            "            \"issuer_id\"\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 5,\n" +
            "         \"max_allowed_amount\": 200000,\n" +
            "         \"accreditation_time\": 2880\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"oxxo\",\n" +
            "         \"name\": \"Oxxo\",\n" +
            "         \"payment_type_id\": \"ticket\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/oxxo.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/oxxo.gif\",\n" +
            "         \"deferred_capture\": \"does_not_apply\",\n" +
            "         \"settings\": [\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 10,\n" +
            "         \"max_allowed_amount\": 10000,\n" +
            "         \"accreditation_time\": 10080\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"serfin\",\n" +
            "         \"name\": \"Santander\",\n" +
            "         \"payment_type_id\": \"atm\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/serfin_bank_transfer.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/serfin_bank_transfer.gif\",\n" +
            "         \"deferred_capture\": \"does_not_apply\",\n" +
            "         \"settings\": [\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 10,\n" +
            "         \"max_allowed_amount\": 10000,\n" +
            "         \"accreditation_time\": 10080\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"banamex\",\n" +
            "         \"name\": \"Banamex\",\n" +
            "         \"payment_type_id\": \"atm\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/banamex_bank_transfer.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/banamex_bank_transfer.gif\",\n" +
            "         \"deferred_capture\": \"does_not_apply\",\n" +
            "         \"settings\": [\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 10,\n" +
            "         \"max_allowed_amount\": 10000,\n" +
            "         \"accreditation_time\": 10080\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"bancomer\",\n" +
            "         \"name\": \"BBVA Bancomer\",\n" +
            "         \"payment_type_id\": \"atm\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/bancomer_bank_transfer.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/bancomer_bank_transfer.gif\",\n" +
            "         \"deferred_capture\": \"does_not_apply\",\n" +
            "         \"settings\": [\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 10,\n" +
            "         \"max_allowed_amount\": 10000,\n" +
            "         \"accreditation_time\": 10080\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"7eleven\",\n" +
            "         \"name\": \"7eleven\",\n" +
            "         \"payment_type_id\": \"ticket\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/7eleven.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/7eleven.gif\",\n" +
            "         \"deferred_capture\": \"does_not_apply\",\n" +
            "         \"settings\": [\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 10,\n" +
            "         \"max_allowed_amount\": 10000,\n" +
            "         \"accreditation_time\": 10080\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"gestopago\",\n" +
            "         \"name\": \"Gestopago\",\n" +
            "         \"payment_type_id\": \"ticket\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/gestopago.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/gestopago.gif\",\n" +
            "         \"deferred_capture\": \"does_not_apply\",\n" +
            "         \"settings\": [\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 10,\n" +
            "         \"max_allowed_amount\": 10000,\n" +
            "         \"accreditation_time\": 10080\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"telecomm\",\n" +
            "         \"name\": \"Telecomm\",\n" +
            "         \"payment_type_id\": \"ticket\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/telecomm.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/telecomm.gif\",\n" +
            "         \"deferred_capture\": \"does_not_apply\",\n" +
            "         \"settings\": [\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 10,\n" +
            "         \"max_allowed_amount\": 10000,\n" +
            "         \"accreditation_time\": 10080\n" +
            "      },\n" +
            "      {\n" +
            "         \"id\": \"pagoefectivo_atm\",\n" +
            "         \"name\": \"PagoEfectivo\",\n" +
            "         \"payment_type_id\": \"atm\",\n" +
            "         \"status\": \"active\",\n" +
            "         \"secure_thumbnail\": \"https://www.mercadopago.com/org-img/MP3/API/logos/telecomm.gif\",\n" +
            "         \"thumbnail\": \"http://img.mlstatic.com/org-img/MP3/API/logos/telecomm.gif\",\n" +
            "         \"deferred_capture\": \"does_not_apply\",\n" +
            "         \"settings\": [\n" +
            "         ],\n" +
            "         \"additional_info_needed\": [\n" +
            "         ],\n" +
            "         \"min_allowed_amount\": 10,\n" +
            "         \"max_allowed_amount\": 10000,\n" +
            "         \"accreditation_time\": 10080\n" +
            "      }\n" +
            "   ]\n" +
            "}";

        public static PaymentMethodSearch getPaymentMethodSearchWithoutCustomOptionsAsJson(){
            return JsonUtil.getInstance().fromJson(paymentMethodsJson, PaymentMethodSearch.class);
        }


}
