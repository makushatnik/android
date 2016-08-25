package com.cdesign.shoppinglistplusplus;

import com.firebase.client.Firebase;

import org.androidannotations.annotations.EApplication;

/**
 * Includes one-time initialization of Firebase related code
 */
@EApplication
public class ShoppingListApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}