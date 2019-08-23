//package com.example.SpirngElasProject.controller;
//
//import com.example.SpirngElasProject.service.SearchSuggestService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.converter.json.MappingJacksonValue;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//public class SuggestController {
//
//    @Autowired
//    private SearchSuggestService searchSuggestService;
//
//    @RequestMapping(value = "/suggest/{partial}", method = RequestMethod.GET, headers = "Accept=application/json")
//    public List listData(@PathVariable("partial") String partial) {
//        return searchSuggestService.getSuggestions(partial);
//    }
//
//    @RequestMapping(value = "/suggest/{partial}", method = RequestMethod.GET, headers = "Accept=application/json", params = {"callback"})
//    public MappingJacksonValue listData(@PathVariable("partial") String partial, @RequestParam("callback") String callback) {
//        MappingJacksonValue value = new MappingJacksonValue(searchSuggestService.getSuggestions(partial));
//        value.setJsonpFunction(callback);
//        return value;
////        return searchSuggestService.getSuggestions(partial);
//    }
//
//}