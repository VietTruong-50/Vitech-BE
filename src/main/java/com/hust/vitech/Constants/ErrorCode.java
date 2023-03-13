package com.hust.vitech.Constants;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNKNOWN_ERROR("UNKNOWN_ERROR", "Lỗi không xác định"),
    AUTHORIZATION_ERROR("AUTHORIZATION_ERROR", "Lỗi xác thực"),
    CUSTOMER_NOT_EXIST("CUSTOMER_NOT_EXIST", "Khách hàng không tồn tại"),
    CUSTOMER_EXIST("CUSTOMER_EXIST", "Khách hàng tồn tại"),
    USER_NOT_EXIST("USER_NOT_EXIST", "Người dùng không tồn tại"),
    USER_EXIST("USER_EXIST", "Người dùng tồn tại"),
    USERNAME_NOT_EXIST("USERNAME_NOT_EXIST", "Tên người dùng không tồn tại"),
    USERNAME_EXIST("USERNAME_EXIST", "Tên người dùng tồn tại"),
    EMAIL_NOT_EXIST("EMAIL_NOT_EXIST", "Email người dùng không tồn tại"),
    EMAIL_EXIST("EMAIL_EXIST", "Email người dùng tồn tại"),
    LIKE_NOT_EXIST("LIKE_NOT_EXIST", "Lượt thích không tồn tại"),
    LIKE_EXIST("LIKE_EXIST", "Lượt thích tồn tại"),
    FILM_NOT_EXIST("FILM_NOT_EXIST", "Phim không tồn tại"),
    FILM_EXIST("FILM_EXIST", "Phim tồn tại"),
    DISLIKE_NOT_EXIST("DISLIKE_NOT_EXIST", "Lượt không thích không tồn tại"),
    DISLIKE_EXIST("DISLIKE_EXIST", "Lượt không thích tồn tại"),
    COMMENT_NOT_EXIST("COMMENT_NOT_EXIST", "Bình luận không tồn tại"),
    COMMENT_EXIST("COMMENT_EXIST", "Bình luận tồn tại"),
    FAVOURITE_NOT_EXIST("FAVOURITE_NOT_EXIST", "Yêu thích không tồn tại"),
    FAVOURITE_EXIST("FAVOURITE_EXIST", "Phim đã được yêu thích"),
    GENRE_NOT_EXIST("GENRE_NOT_EXIST", "Thể loại không tồn tại"),
    GENRE_EXIST("GENRE_EXIST", "Thể loại tồn tại"),
    INVALID_ORDER_BY_METHOD("INVALID_ORDER_BY_METHOD", "Phương thức sắp xếp sai");
    private final String message;
    private final String code;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}