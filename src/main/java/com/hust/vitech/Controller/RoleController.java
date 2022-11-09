package com.hust.vitech.Controller;

import com.hust.vitech.Model.Role;
import com.hust.vitech.Response.ApiResponse;
import com.hust.vitech.Service.Impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping(value = "/roles", produces = "application/json")
    public ApiResponse<Page<Role>> getAllRoles(@RequestParam int size, @RequestParam int page, @RequestParam String sortBy) {
        return ApiResponse.successWithResult(roleService.getAllRoles(size,page,sortBy));
    }
}
