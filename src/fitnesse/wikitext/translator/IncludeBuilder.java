package fitnesse.wikitext.translator;

import fitnesse.wikitext.parser.CollapsibleRule;
import fitnesse.wikitext.parser.Symbol;

public class IncludeBuilder implements Translation {
    public String toHtml(Translator translator, Symbol symbol) {
        if (symbol.getChildren().size() < 4) {
            return translator.translate(symbol.childAt(2));
        }
        String option = symbol.childAt(0).getContent();
        if (option.equals("-seamless")) {
                return translator.translate(symbol.childAt(3));
        }
        else {
            String collapseState = stateForOption(option);
            String title = "Included page: " + translator.translate(symbol.childAt(1));
            return CollapsibleBuilder.generateHtml(collapseState, title, translator.translate(symbol.childAt(3)));
        }
    }

    private String stateForOption(String option) {
        return option.equals("-setup") || option.equals("-c") 
                ? CollapsibleRule.ClosedState
                : CollapsibleRule.OpenState;
    }
}
