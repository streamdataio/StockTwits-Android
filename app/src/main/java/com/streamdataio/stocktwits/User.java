package com.streamdataio.stocktwits;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String name;
    private String bio;
    private String join;
    private String avatarURL;
    private String location;
    private String followers;
    private String following;
    private String off;
    private String xpLevel;
    private String holding_p;
    private String approach;
    private String webURL;
    private String ideas;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getXpLevel() {
        return xpLevel;
    }

    public void setXpLevel(String xpLevel) {
        this.xpLevel = xpLevel;
    }

    public String getHolding_p() {
        return holding_p;
    }

    public void setHolding_p(String holding_p) {
        this.holding_p = holding_p;
    }

    public String getApproach() {
        return approach;
    }

    public void setApproach(String approach) {
        this.approach = approach;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getIdeas() {
        return ideas;
    }

    public void setIdeas(String ideas) {
        this.ideas = ideas;
    }

    public User (String uname, String name, String bio, String joined, String avatar, String loc,
                 String followers, String following, String off, String xp,
                 String holding_p, String approach, String weburl, String Ideas) {
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
        this.holding_p= holding_p;
        this.approach = approach;
        this.webURL = weburl;
        this.ideas= Ideas;


    }

}