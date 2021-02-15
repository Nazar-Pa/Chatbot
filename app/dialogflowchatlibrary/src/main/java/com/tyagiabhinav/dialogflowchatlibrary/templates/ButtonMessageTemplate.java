package com.tyagiabhinav.dialogflowchatlibrary.templates;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tyagiabhinav.dialogflowchatlibrary.templateutil.OnClickCallback;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.protobuf.Value;

import java.util.List;
import java.util.Map;

public class ButtonMessageTemplate extends MessageLayoutTemplate {

    private static final String TAG = ButtonMessageTemplate.class.getSimpleName();

    public ButtonMessageTemplate(Context context, OnClickCallback callback, int type) {
        super(context, callback, type);
        setOnlyTextResponse(false);
    }

    @Override
    FrameLayout populateRichMessageContainer() {
        FrameLayout richMessageContainer = getRichMessageContainer();
        DetectIntentResponse response = getResponse();
        List<com.google.cloud.dialogflow.v2beta1.Context> contextList = response.getQueryResult().getOutputContextsList();

        LinearLayout btnLayout = getVerticalContainer();
//        btnLayout.setGravity(Gravity.CENTER);
        btnLayout.setFocusableInTouchMode(true);
//        for (com.google.cloud.dialogflow.v2.Context context : contextList) {

            Map<String, Value> contextParam = response.getQueryResult().getOutputContextsList().get(0).getParameters().getFieldsMap();
            List<Value> buttonList = (contextParam.get("buttonItems") != null) ? contextParam.get("buttonItems").getListValue().getValuesList() : null;
            //String align = context.getParameters().getFieldsMap().get("align").getStringValue();
            //String sizeValue = context.getParameters().getFieldsMap().get("size").getStringValue();
            String eventName = response.getQueryResult().getOutputContextsList().get(0).getParameters().getFieldsMap().get("eventToCall").getStringValue();

//            if (align.equalsIgnoreCase("horizontal") || align.equalsIgnoreCase("h")) {
//                btnLayout = getHorizontalContainer();
//            }
            btnLayout = getHorizontalContainer();

            if (buttonList != null) {
                for (Value item : buttonList) {
                    btnLayout.addView(getBtn("button", item.getStructValue().getFieldsMap(), "s", eventName));
                }
            }

        Log.d(TAG, "populateRichMessageContainer: btn layout count: " + btnLayout.getChildCount());
        richMessageContainer.addView(btnLayout);

        return richMessageContainer;
    }


}
