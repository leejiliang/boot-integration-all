package com.uk.bootintegrationall.springmvc.config;

/**
 * @Description TODO
 */
public class UserInfo {
    private Long id;
    private String name;

    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", role='" + role + '\'' +
            '}';
    }
}
