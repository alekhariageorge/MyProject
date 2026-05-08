package com.plp.droplet;

import com.base.repository.model.RepositoryItem;
import com.plp.service.SearchService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchResults {

    private final SearchService searchService;

    public SearchResults(SearchService searchService) {
        this.searchService = searchService;
    }

    public List<RepositoryItem> getProducts(String searchTerm) {
        return searchService.searchProducts(searchTerm);
    }
}
