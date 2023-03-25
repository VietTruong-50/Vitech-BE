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
    CATEGORY_NOT_EXIST("CATEGORY_NOT_EXIST", "Phân loại không tồn tại"),
    SUB_CATEGORY_NOT_EXIST("SUB_CATEGORY_NOT_EXIST", "Nhà sản xuất không tồn tại"),
    PRODUCT_NOT_EXIST("PRODUCT_NOT_EXIST", "Sản phẩm không tồn tại"),
    SLIDER_NOT_EXIST("SLIDER_NOT_EXIST", "Slider không tồn tại");
    private final String message;
    private final String code;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}