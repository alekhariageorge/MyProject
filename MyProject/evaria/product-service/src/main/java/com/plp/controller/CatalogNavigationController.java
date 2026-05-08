package com.plp.controller;

import com.plp.droplet.CategoryLookup;
import com.plp.droplet.ProductList;
import com.plp.droplet.SearchResults;
import com.plp.model.PlpPageDTO;
import com.plp.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CatalogNavigationController {

    private final CatalogService catalogService;
    private final CategoryLookup categoryLookup;
    private final ProductList productList;
    private final SearchResults searchResults;

    public CatalogNavigationController(CatalogService catalogService,
                                       CategoryLookup categoryLookup,
                                       ProductList productList,
                                       SearchResults searchResults) {
        this.catalogService = catalogService;
        this.categoryLookup = categoryLookup;
        this.productList = productList;
        this.searchResults = searchResults;
    }

    @GetMapping("/catalog/{catalogId}")
    public PlpPageDTO getCatalogPage(@PathVariable String catalogId) {
        PlpPageDTO page = new PlpPageDTO();
        page.setCatalog(catalogService.getCatalog(catalogId));
        page.setChildCategories(catalogService.getRootCategories(catalogId));
        page.setProducts(catalogService.getCatalogProducts(catalogId));
        return page;
    }

    @GetMapping("/category/{categoryId}")
    public PlpPageDTO getCategoryPage(@PathVariable String categoryId) {
        PlpPageDTO page = new PlpPageDTO();
        page.setCategory(categoryLookup.getCategory(categoryId));
        page.setChildCategories(categoryLookup.getChildCategories(categoryId));
        page.setProducts(productList.getProducts(categoryId));
        return page;
    }

    @GetMapping("/catalog/{catalogId}/category/{categoryId}")
    public PlpPageDTO getCatalogCategoryPage(@PathVariable String catalogId,
                                             @PathVariable String categoryId) {
        PlpPageDTO page = getCategoryPage(categoryId);
        page.setCatalog(catalogService.getCatalog(catalogId));
        return page;
    }

    @GetMapping("/product/search")
    public PlpPageDTO getSearchResults(@RequestParam String term) {
        PlpPageDTO page = new PlpPageDTO();
        page.setSearchTerm(term);
        page.setProducts(searchResults.getProducts(term));
        return page;
    }

    @GetMapping("/product/search/{term}")
    public PlpPageDTO getSearchResultsByPath(@PathVariable String term) {
        return getSearchResults(term);
    }
}
