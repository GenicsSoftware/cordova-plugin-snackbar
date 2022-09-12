package com.materialSnackbar;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.design.widget.Snackbar;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.View;

import android.app.Activity;
import android.content.Context;

//import android.util.DisplayMetrics;
//import android.widget.LinearLayout;

public class MaterialSnackbar extends CordovaPlugin {

    private FrameLayout layout;
    private Snackbar snackbar;

  @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        layout = (FrameLayout) webView.getView().getParent();
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        try {
            if ("materialSnackbar".equals(action)) {

                if(snackbar != null){
                    snackbar.dismiss();
                }

                JSONObject arg_object = args.getJSONObject(0);

                final String text = arg_object.getString("text");

                final String duration = arg_object.getString("duration");

                final String button = arg_object.getString("button");

                //DisplayMetrics dm = new DisplayMetrics();
                //this.cordova.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        snackbar = Snackbar
                        .make(layout, text, Snackbar.LENGTH_INDEFINITE);

                        if(duration.equals("SHORT")){
                          snackbar.setDuration(Snackbar.LENGTH_SHORT);
                        } else if(duration.equals("LONG")){
                          snackbar.setDuration(Snackbar.LENGTH_LONG);
                        } else if(duration.equals("INDEFINITE")){
                          snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                        }

                        if(button != null && !button.isEmpty()){
                          snackbar.setAction(button, new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  snackbar.dismiss();
                                  callbackContext.success();
                              }
                          });
                        }


                        TextView messageView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                        messageView.setMaxLines(3);

                        /*View snackbarLayout = snackbar.getView();
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        lp.topMargin = (int)((double)dm.heightPixels-500);


                        // Margins relative to the parent view.
                        snackbarLayout.setLayoutParams(lp);*/



                        snackbar.show();
                    }
                });
                return true;
            } else if ("closeMaterialSnackbar".equals(action)) {
                snackbar.dismiss();
                callbackContext.success();
            }
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }
}
