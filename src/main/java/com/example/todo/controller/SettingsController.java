package com.example.todo.controller;

import com.example.todo.data.ChangePasswordDTO;
import com.example.todo.entity.User;
import com.example.todo.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final PasswordEncoder encoder;
    private final UserService userService;
    public SettingsController(PasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @ModelAttribute("changePassword")
    private ChangePasswordDTO changePasswordDTO() {
        return new ChangePasswordDTO();
    }

    @GetMapping("")
    public String redirect() {
        return "redirect:/settings/password";
    }

    @GetMapping("/password")
    public String password() {
        return "change_password";
    }

    @PostMapping("/password")
    public String changePassword(@ModelAttribute(name = "changePassword") ChangePasswordDTO dto,
                                 BindingResult result,
                                 @AuthenticationPrincipal User user,
                                 Model model) {
        String currentPassword = dto.getCurrentPassword();
        String newPassword = dto.getNewPassword();
        if (!encoder.matches(currentPassword, user.getEncryptedPassword())) {
            result.addError(new ObjectError("globalError", "The current password is invalid."));
        }
        if (!newPassword.equals(dto.getConfirmPassword())) {
            result.addError(new ObjectError("globalError", "The new password and confirm password aren't same."));
        }
        if (!result.hasErrors()) {
            user.setEncryptedPassword(encoder.encode(newPassword));
            userService.save(user);
            model.addAttribute("message", "Your password was changed");
        }
        return "change_password";
    }
}
