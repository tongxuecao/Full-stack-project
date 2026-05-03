package com.example.secondHandTrading.service;

import com.example.secondHandTrading.entity.Product;
import com.example.secondHandTrading.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    public String addFavorite(Integer userId, Integer productId) {
        if (favoriteMapper.findByUserAndProduct(userId, productId) != null) {
            return "already";
        }
        return favoriteMapper.insert(userId, productId) > 0 ? "success" : "fail";
    }

    public String removeFavorite(Integer userId, Integer productId) {
        return favoriteMapper.delete(userId, productId) > 0 ? "success" : "fail";
    }

    public boolean isFavorited(Integer userId, Integer productId) {
        return favoriteMapper.findByUserAndProduct(userId, productId) != null;
    }

    public List<Product> getFavorites(Integer userId) {
        return favoriteMapper.findFavoritedProducts(userId);
    }

    public List<Integer> getFavoritedIds(Integer userId) {
        return favoriteMapper.findFavoritedProductIds(userId);
    }
}
