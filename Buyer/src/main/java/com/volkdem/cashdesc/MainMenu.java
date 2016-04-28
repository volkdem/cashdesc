package com.volkdem.cashdesc;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Evgeny on 28.04.2016.
 */
public class MainMenu {
    private AppCompatActivity activity;

    public MainMenu(AppCompatActivity activity) {
        this.activity = activity;
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final RelativeLayout mainMenu = (RelativeLayout) findViewById( R.id.main_menu );

        final ListView mainMenuList = (ListView) findViewById(R.id.main_menu_list);
        mainMenuList.setAdapter( new MainMenuAdapter() );


        drawerLayout.setDrawerListener(new ActionBarDrawerToggle(activity, drawerLayout, R.string.button_open_browser, R.string.button_add_contact ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("opened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getSupportActionBar().setTitle("closed");
            }
        });
    }

    private View findViewById(int id) {
        return activity.findViewById( id );
    }
}
