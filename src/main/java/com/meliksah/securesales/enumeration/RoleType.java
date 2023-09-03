package com.meliksah.securesales.enumeration;

/**
 * @Author mselvi
 * @Created 09.08.2023
 */

public enum RoleType {

    ROLE_USER("Role User"),
    ROLE_MANAGER("Role Manager"),
    ROLE_ADMIN("Role Admin"),
    ROLE_SYSADMIN("Role SysAdmin");

    private final String type;

    RoleType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
