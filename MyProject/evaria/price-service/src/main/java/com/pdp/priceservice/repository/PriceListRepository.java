package com.pdp.priceservice.repository;

import com.base.repository.model.RepositoryInfo;
import com.base.repository.model.RepositoryItem;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class PriceListRepository extends RepositoryInfo {
    public PriceListRepository() {
        super("repository/repository-definition.xml");
    }

    public RepositoryItem findPriceBySkuId(String skuId) {
        Map<String, RepositoryItem> priceItems = getRepositoryItems().get("price");
        for(String priceItemID: priceItems.keySet()){
            RepositoryItem priceItem = priceItems.get(priceItemID);
            if(skuId.equalsIgnoreCase((String)priceItem.getPropertyValue
                    ("skuId"))){
                return priceItem;
            }
        }
        return null;
    }
}
