package com.zerolatency.backend.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.zerolatency.backend.model.AuthProvider;
import com.zerolatency.backend.model.User;
import com.zerolatency.backend.repo.UserRepo;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepo userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Process OAuth2 user and save/update in database
        processOAuth2User(userRequest, oAuth2User);

        return oAuth2User;
    }

    private void processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = null;
        String providerId = null;

        // Extract provider-specific attributes
        if ("google".equals(registrationId)) {
            picture = (String) attributes.get("picture");
            providerId = (String) attributes.get("sub");
        } else if ("github".equals(registrationId)) {
            picture = (String) attributes.get("avatar_url");
            providerId = String.valueOf(attributes.get("id"));
            // GitHub might not provide email in attributes if not public
            if (email == null) {
                email = (String) attributes.get("login") + "@github.local";
            }
        }

        // Check if user exists
        User user = userRepository.findByEmail(email);

        if (user == null) {
            // Create new user
            user = new User();
            user.setEmail(email);
            user.setUsername(name != null ? name : email.split("@")[0]);
            user.setProvider("google".equals(registrationId) ? AuthProvider.GOOGLE : AuthProvider.GITHUB);
            user.setProviderId(providerId);
            user.setProfilePicture(picture);
            user.setEnabled(true);
            user.setRole("USER");
            // No password for OAuth User
            user.setPassword(null);
        } else {
            // Update existing user
            user.setProfilePicture(picture);
            user.setProviderId(providerId);
        }

        userRepository.save(user);
    }
}
