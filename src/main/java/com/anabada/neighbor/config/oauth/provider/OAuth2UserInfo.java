package com.anabada.neighbor.config.oauth.provider;

public interface OAuth2UserInfo {
    String getProviderId();
    String getEmail();
    String getName();
}
