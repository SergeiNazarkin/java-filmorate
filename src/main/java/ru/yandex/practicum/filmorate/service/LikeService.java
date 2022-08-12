package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.LikeRepository;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void addLike(Integer filmId, Integer userId) {
        likeRepository.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        likeRepository.deleteLike(filmId, userId);
    }
}
