package com.wirvsvirus.backend.security;

import java.util.HashMap;
import java.util.Map;

public class View {
    static enum Role {
        ROLE_ANONYMOUS,
        ROLE_ADMIN
    }

    public static final Map<Role, Class> MAPPING = new HashMap<>();

    static {
        MAPPING.put(Role.ROLE_ADMIN, Admin.class);
        MAPPING.put(Role.ROLE_ANONYMOUS, Anonymous.class);
    }

    public static class Admin extends Anonymous {}
    public static class Anonymous {}
}
