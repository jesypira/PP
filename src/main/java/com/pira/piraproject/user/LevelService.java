package com.pira.piraproject.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelService {

    public final LevelRepository levelRepository;

    public void checkLevel(User user) {
        //TODO: load levels at app start
        List<Level> levelList = levelRepository.findAll();

        Level reachedLevel = levelList.stream()
                .filter(lvl -> user.getXp() >= lvl.getRequiredXP())
                .max(Comparator.comparingInt(Level::getRequiredXP)).get();

        user.setLevel(reachedLevel);
    }

    public Level getLevel(Long levelId) {
        return levelRepository.findById(levelId).orElse(null);
    }

    public Level getLNextLevel(Long levelId) {
        return levelRepository.findFirstByIdGreaterThanOrderByIdAsc(levelId).orElse(null);
    }
}
