//package com.example.SpirngElasProject.dao.daoImpl;
//
//import com.example.SpirngElasProject.dao.SearchSuggest;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.search.suggest.Suggest;
//import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SearchSuggestImpl implements SearchSuggest {
//
//    private Client client;
//
//    @Override
//    public List<String> getSuggestions(String partial) {
//        List<String> returnList = new ArrayList<>();
//
//        String suggestionName = "suggestion";
//
//        CompletionSuggestionBuilder completionSuggestionBuilder = new CompletionSuggestionBuilder(suggestionName);
//        SuggestResponse suggestResponse = client.prepareSuggest("wow").setSuggestText(partial)
//                .addSuggestion(completionSuggestionBuilder.field("name.suggest")).execute().actionGet();
//        Suggest suggest = suggestResponse.getSuggest();
//        Suggest.Suggestion suggestion = suggest.getSuggestion(suggestionName);
//
//        List<Suggest.Suggestion.Entry> list = suggestion.getEntries();
//        for(Suggest.Suggestion.Entry entry : list) {
//            List<Suggest.Suggestion.Entry.Option> options = entry.getOptions();
//            for(Suggest.Suggestion.Entry.Option option : options) {
//                returnList.add(option.getText().toString());
//            }
//        }
//        return returnList;
//    }
//
//    public void setClient(Client client) {
//        this.client = client;
//    }
//}