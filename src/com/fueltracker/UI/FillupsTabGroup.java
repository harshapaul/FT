package com.fueltracker.UI;

import android.content.Intent;
import android.os.Bundle;

public class FillupsTabGroup extends TabGroupActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startChildActivity("Fillups", new Intent(this,Fillups.class));
    }
}
