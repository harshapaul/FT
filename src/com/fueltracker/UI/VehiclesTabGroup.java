package com.fueltracker.UI;

import android.content.Intent;
import android.os.Bundle;

public class VehiclesTabGroup extends TabGroupActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startChildActivity("Vehicles", new Intent(this,Vehicles.class));
    }
}
