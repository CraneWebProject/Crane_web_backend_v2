package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.Redis;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Collection;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 604800)
@Embeddable
public class RefreshToken {


    @Id
    private String userEmail;

    private String ip;

    private Collection<? extends GrantedAuthority> authorities;

    @Indexed
    private String refreshToken;

}
