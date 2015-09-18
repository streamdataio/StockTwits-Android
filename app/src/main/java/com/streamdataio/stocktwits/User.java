package com.streamdataio.stocktwits;

import java.io.Serializable;

public class User implements Serializable {

    private String username, name, bio, join, avatarURL, location, followers, following, off, xpLevel, holdingPeriod, approach, webURL, ideas;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }


    public String getJoin() {
        return join;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getLocation() {
        return location;
    }

    public String getFollowers() {
        return followers;
    }

    public String getFollowing() {
        return following;
    }

    public String getOff() {
        return off;
    }

    public String getXpLevel() {
        return xpLevel;
    }

    public String getHoldingPeriod() {
        return holdingPeriod;
    }

    public String getApproach() {
        return approach;
    }

    public String getWebURL() {
        return webURL;
    }

    public String getIdeas() {
        return ideas;
    }

    public User (String uname, String name, String bio, String joined, String avatar, String loc,
                 String followers, String following, String off, String xp,
                 String holdingPeriod, String approach, String weburl, String Ideas) {
        this.username = uname;
        this.name = name;
        this.bio = bio;
        this.join = joined;
        this.avatarURL = avatar;
        this.location = loc;
        this.followers = followers;
        this.following = following;
        this.off = off;
        this.xpLevel = xp;
        this.holdingPeriod = holdingPeriod;
        this.approach = approach;
        this.webURL = weburl;
        this.ideas= Ideas;
    }

}