package com.hust.vitech.Service;

import com.hust.vitech.Model.User;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<User> getAllUser(int size, int page, String sortBy);
}
