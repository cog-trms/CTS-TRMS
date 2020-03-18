package com.cognizant.trms.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * This is a global controller written merely for showing the login and logout apis in the
 * swagger documentation allowing users to get the authorisation token from the same interface
 * and use it for executing the secured API operations.
 *
 * Aravindan Dandapani
 */
@RestController
@RequestMapping("/api")
@Api(value="trms-application", description="Operations pertaining to user login and logout in the TRMS application")
public class FakeController {
    @ApiOperation("Login")
    @PostMapping("/auth")
    public void fakeLogin(@RequestBody @Valid LoginRequest loginRequest) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @ApiOperation("Logout")
    @PostMapping("/logout")
    public void fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class LoginRequest{
        @NotNull(message = "Email can not be blank")
        private String email;
        @NotNull(message = "Password can not be blank")
        private String password;
    }
}
