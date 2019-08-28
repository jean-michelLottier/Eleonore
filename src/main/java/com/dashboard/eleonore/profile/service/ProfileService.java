package com.dashboard.eleonore.profile.service;

import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.dto.UserDTO;
import com.dashboard.eleonore.profile.repository.entity.AuthToken;
import com.dashboard.eleonore.profile.repository.entity.Authentication;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import com.dashboard.eleonore.profile.repository.entity.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

public interface ProfileService {
    boolean isValidAuthentication(Authentication authentication);

    Optional<Authentication> getAuthentication(String login, String password);

    Optional<AuthToken> saveToken(Authentication authentication, String token);

    void deleteToken(String token);

    ProfileDTO saveProfile(ProfileDTO profileDTO, ProfileType profileType);

    Optional<ProfileDTO> getProfile(Long id, ProfileType profileType);

    static String generateToken(Authentication authentication) {
        StringBuilder content = new StringBuilder("eleonore_token");
        content.append("/").append(authentication.getLogin())
                .append("/").append(authentication.getPassword())
                .append("/").append(UUID.randomUUID().toString());

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(getSalt());
            StringBuilder token = new StringBuilder();
            for (byte b : messageDigest.digest(content.toString().getBytes())) {
                token.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }

            return token.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }
}
