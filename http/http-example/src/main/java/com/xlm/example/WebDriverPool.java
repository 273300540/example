package com.xlm.example;

import org.openqa.selenium.WebDriver;

public class WebDriverPool implements Pool<WebDriver> {
    //private GenericOb
    @Override
    public WebDriver borrow() {
        return null;
    }

    @Override
    public void back(WebDriver pooled) {

    }

    @Override
    public void dead(WebDriver pooled) {
        //pooled.

    }
}
