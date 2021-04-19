package com.tiff.tiffinbox.authentication;

import android.widget.Spinner;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import fr.ganfra.materialspinner.MaterialSpinner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegisterTest {

    @Mock
    public Register register;
     @Mock
     MaterialSpinner spinner;

//     ProgressBar progressBar;

    @Rule
    public ActivityTestRule<Register> mActivityRule;

    @BeforeEach
   public void setUp() {

        MockitoAnnotations.initMocks(this);


        //register  = new Register();
       //  spinner = mock(MaterialSpinner.class);
//         progressBar = mock(ProgressBar.class);
        // authenticationPresenterLayer = new AuthenticationPresenterLayer();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
   public void setSpinnerError() {
        mActivityRule =
        new ActivityTestRule(Register.class);
       // Register registerr = new Register();
      Spinner spinner = mock(Spinner.class);
//        String s = mock(String.class);

        mActivityRule.getActivity().setSpinnerError(any(Spinner.class),"Errorr");


        register = mock(Register.class);

        //when(mActivityRule.getActivity().setSpinnerError(spinner, "Errorr")).thenReturn(true);
       // verify(register).setSpinnerError(spinner, "Errorr");

        //register.setSpinnerError(spinner, "Errorr");
       // assertTrue(register.setSpinnerError(spinner, "Errorr"));

    //    verify(authenticationPresenterLayer, times(1)).progressbarShow(progressBar);

    }
}