package com.tiff.tiffinbox.Seller.Profile;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProfileTest {

    @Rule
    public ActivityTestRule<Profile> activityTestRule = new ActivityTestRule<Profile>(Profile.class);

    private Profile profile = null;

    @Before
    public void setUp() throws Exception {
        profile = activityTestRule.getActivity();
    }

    @Test
    public void testlaunch(){
        View view = profile.btnUpdate;
        assertNotNull(view);

    }

//    @Test
//    public void performbtnclick(){
//        assertEquals(true, profile.btnEdit.performClick());
//    }

    @After
    public void tearDown() throws Exception {
        profile = null;
    }

    @Test
    public void validations() {
        assertEquals(true, profile.validations());
    }

}