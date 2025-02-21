package com.rcr.serviceimpl;

import com.rcr.domain.HomeCategorySection;
import com.rcr.model.Deal;
import com.rcr.model.Home;
import com.rcr.model.HomeCategory;
import com.rcr.repository.DealRepository;
import com.rcr.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final DealRepository dealRepository;
    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.GRID)
                .collect(Collectors.toList());

        List<HomeCategory> shopCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES)
                .collect(Collectors.toList());

        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES)
                .collect(Collectors.toList());
        List<HomeCategory> dealCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                .collect(Collectors.toList());

        List<Deal> createDeal = new ArrayList<>();

        if(dealRepository.findAll().isEmpty()){
            List<Deal> deals = allCategories.stream()
                    .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .collect(Collectors.toList());
            createDeal = deals;
        }
        else{
            createDeal = dealRepository.findAll();
        }

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setDeals(createDeal);
        home.setElectricCategories(electricCategories);
        home.setShopByCategories(shopCategories);
        home.setDealCategories(dealCategories);

        return home;
    }
}
