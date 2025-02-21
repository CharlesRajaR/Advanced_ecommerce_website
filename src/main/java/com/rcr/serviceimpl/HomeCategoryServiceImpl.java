package com.rcr.serviceimpl;

import com.rcr.model.HomeCategory;
import com.rcr.repository.HomeCategoryRepository;
import com.rcr.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {
    private final HomeCategoryRepository homeCategoryRepository;
    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
        if(homeCategoryRepository.findAll().isEmpty()){
            return homeCategoryRepository.saveAll(homeCategories);
        }
        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
        HomeCategory homeCategory1 = homeCategoryRepository.findById(id)
                .orElseThrow(() -> new Exception("home category not found..."));
        if(homeCategory.getImage() != null) homeCategory1.setImage(homeCategory.getImage());
        if(homeCategory.getCategory_id() != null) homeCategory1.setCategory_id(homeCategory.getCategory_id());

        return homeCategoryRepository.save(homeCategory1);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }
}
