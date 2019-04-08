package com.xlm.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SearchResultPageProcess implements PageProcess {
    private Store<String> store;

    //private static Log
    @Override
    public void process(PageContext pageContext) {
        Document document = Jsoup.parse(pageContext.getContent());
        Elements elements = document.select(".product:not(.album-new)");
        if (elements == null) {
            return;
        }
        List<String> searchList = new ArrayList<>();
        for (Element element : elements) {
            Element one = element.selectFirst(".productImg");
            if (one != null) {
                searchList.add(one.attr("href"));
            }
        }
        store.store(searchList);
    }

    public Store<String> getStore() {
        return store;
    }

    public void setStore(Store<String> store) {
        this.store = store;
    }
}
