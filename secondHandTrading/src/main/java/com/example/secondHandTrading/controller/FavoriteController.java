package com.example.secondHandTrading.controller;

import com.example.secondHandTrading.entity.Product;
import com.example.secondHandTrading.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public String addFavorite(@RequestParam Integer userId, @RequestParam Integer productId) {
        return favoriteService.addFavorite(userId, productId);
    }

    @DeleteMapping("/remove")
    public String removeFavorite(@RequestParam Integer userId, @RequestParam Integer productId) {
        return favoriteService.removeFavorite(userId, productId);
    }

    @GetMapping("/check")
    public boolean isFavorited(@RequestParam Integer userId, @RequestParam Integer productId) {
        return favoriteService.isFavorited(userId, productId);
    }

    @GetMapping("/list")
    public List<Product> getFavorites(@RequestParam Integer userId) {
        return favoriteService.getFavorites(userId);
    }

    @GetMapping("/ids")
    public List<Integer> getFavoritedIds(@RequestParam Integer userId) {
        return favoriteService.getFavoritedIds(userId);
    }
}
