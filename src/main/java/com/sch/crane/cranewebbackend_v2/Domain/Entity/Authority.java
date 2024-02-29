package com.sch.crane.cranewebbackend_v2.Domain.Entity;

import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;

    @Builder
    public Authority (String authorityName){
        this.authorityName = authorityName;
    }
}
