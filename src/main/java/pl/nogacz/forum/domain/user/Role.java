package pl.nogacz.forum.domain.user;

public enum Role {
    USER,
    MODERATOR,
    ADMIN;

    public boolean isUser() {
        return this == USER;
    }

    public boolean isModerator() {
        return this == MODERATOR;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}