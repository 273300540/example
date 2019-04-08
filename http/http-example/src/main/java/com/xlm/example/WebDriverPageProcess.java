package com.xlm.example;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Predicate;

public class WebDriverPageProcess implements PageProcess {
    private Store<String> store;
    private Pool<WebDriver> webDriverPool;
    private Predicate<WebDriver> webDriverPredicate;
    private int size = 5;
    private PageProcess detailPageSourceProcess;

    @Override
    public void process(PageContext pageContext) {
        List<String> pulledList = store.pullLast(size);
        WebDriver webDriver = null;
        int i = 0;
        while (i < pulledList.size()) {
            String pulled = pulledList.get(i);
            if (webDriver == null) {
                webDriver = webDriverPool.borrow();
            }
            webDriver.navigate().to(pulled);
            if (!webDriverPredicate.test(webDriver)) {
                webDriverPool.dead(webDriver);
                webDriver = null;
                continue;
            }
            i++;
            processInternal(pulled);
        }

    }

    protected  void processInternal(String item){
        detailPageSourceProcess.process(new PageContext(item));
    }
}
