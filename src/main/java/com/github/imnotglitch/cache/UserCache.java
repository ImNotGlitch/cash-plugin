package com.github.imnotglitch.cache;

import com.github.imnotglitch.database.Repository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class UserCache {
    private final Repository userRepository;

    private final Map<String, User> userMap = Maps.newHashMap();

    public void load(Player player) {
        User user = this.userRepository.search(player.getName());

        if (user == null) {
            user = new User(player.getName(), 0);

            userRepository.create(user);
        }

        userMap.put(player.getName(), user);
    }

    public void invalidate(Player player) {
        userMap.remove(player.getName());
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public User get(String name) {

        if (userMap.get(name) == null)
            return userRepository.search(name);

        return userMap.get(name);
    }

    public void setCash(String playerName, int cash) {
        User user = userMap.get(playerName);
        if (user != null) {
            user = new User(playerName, cash);
            userMap.put(playerName, user);
            userRepository.update(user);
        }
    }

    public void addCash(String playerName, int cash) {
        User user = userMap.get(playerName);
        if (user != null) {
            user = new User(playerName, user.getCash() + cash);
            userMap.put(playerName, user);
            userRepository.update(user);
        }
    }
}
