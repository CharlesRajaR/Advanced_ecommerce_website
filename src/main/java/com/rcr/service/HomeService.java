package com.rcr.service;

import com.rcr.model.Home;
import com.rcr.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
