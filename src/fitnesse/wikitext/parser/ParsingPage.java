package fitnesse.wikitext.parser;

import fitnesse.wiki.WikiPage;
import util.Maybe;

import java.util.HashMap;

public class ParsingPage {
    private WikiPage page;
    private WikiPage namedPage;
    private HashMap<WikiPage, HashMap<String, Maybe<String>>> cache;

    public ParsingPage(WikiPage page) {
        this(page, page, new HashMap<WikiPage, HashMap<String, Maybe<String>>>());
    }

    public ParsingPage copyForPage(WikiPage page) {
        return new ParsingPage(page, page, this.cache);
    }

    public ParsingPage copyForNamedPage(WikiPage namedPage) {
        return new ParsingPage(this.page, namedPage, this.cache);
    }
    
    private ParsingPage(WikiPage page, WikiPage namedPage, HashMap<WikiPage, HashMap<String, Maybe<String>>> cache) {
        this.page = page;
        this.namedPage = namedPage;
        this.cache = cache;
    }

    public WikiPage getPage() { return page; }

    public String getPageName() {
        return namedPage.getName();
    }

    public String getPagePath() {
        try {
            return namedPage.getPageCrawler().getFullPath(namedPage).parentPath().toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public boolean inCache(WikiPage page) {
        return cache.containsKey(page);
    }

    public Maybe<String> findVariable(WikiPage page, String name) {
        if (!cache.containsKey(page)) return Maybe.noString;
        if (!cache.get(page).containsKey(name)) return Maybe.noString;
        return cache.get(page).get(name);
    }

    public Maybe<String> findVariable(String name) {
        return findVariable(page, name);
    }

    public String findVariable(WikiPage page, String name, String defaultValue) {
        Maybe<String> result = findVariable(page, name);
        return result.isNothing() ? defaultValue : result.getValue();
    }

    public void putVariable(WikiPage page, String name, Maybe<String> value) {
        if (!cache.containsKey(page)) cache.put(page, new HashMap<String, Maybe<String>>());
        cache.get(page).put(name, value);
    }

    public void putVariable(String name, String value) {
        putVariable(page, name, new Maybe<String>(value));
    }
}
