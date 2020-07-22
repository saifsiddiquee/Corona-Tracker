package com.saif.coronatracker.helpers;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerController {

    private NavigationView navigationView;
    private DrawerLayout drawer;


    public DrawerController(NavigationView navigationView, DrawerLayout drawer) {
        this.navigationView = navigationView;
        this.drawer = drawer;
    }
}
