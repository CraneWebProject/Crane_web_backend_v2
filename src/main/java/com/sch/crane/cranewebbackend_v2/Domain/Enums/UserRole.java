package com.sch.crane.cranewebbackend_v2.Domain.Enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_ADMIN(Authority.ADMIN_NUM,Authority.ADMIN), // 사이트 관리자
    ROLE_MANAGER(Authority.MANAGER_NUM,Authority.MANAGER), // 임원
    ROLE_MEMBER(Authority.MEMBER_NUM,Authority.MEMBER), // 일반 부원
    ROLE_GRADUATED(Authority.GRADUATED_NUM,Authority.GRADUATED), // 졸업자
    ROLE_STAN_BY(Authority.STANBY_NUM,Authority.STAN_BY); // 가입 대기

    private final int num;
    private final String authority;


    UserRole(int num, String authority) {
        this.num = num;
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String GRADUATED = "ROLE_GRADUATED";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String MEMBER = "ROLE_MEMBER";
        public static final String STAN_BY = "ROLE_STANBY";

        public static final int ADMIN_NUM = 4;
        public static final int GRADUATED_NUM = 3;
        public static final int MANAGER_NUM = 2;
        public static final int MEMBER_NUM = 1;
        public static final int STANBY_NUM = 0;

    }
}
