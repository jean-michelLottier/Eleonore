package com.dashboard.eleonore.profile.service;

import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.repository.entity.AuthToken;
import com.dashboard.eleonore.profile.repository.entity.Authentication;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

public interface ProfileService {
    /**
     * Method to check if the profile authentication is valid.
     *
     * @param authentication
     * @return {@code true} if authentication valid, otherwise {@code false}
     */
    boolean isValidAuthentication(Authentication authentication);

    /**
     * Method to get authentication information.
     *
     * @param login
     * @param password
     * @return
     */
    Optional<Authentication> getAuthentication(String login, String password);

    /**
     * Method to save the authentication token in database.
     *
     * @param authentication
     * @param token
     * @return
     */
    Optional<AuthToken> saveToken(Authentication authentication, String token);

    /**
     * Method to delete an authentication token in database.
     *
     * @param token
     */
    void deleteToken(String token);

    /**
     * Method to check if a given authentication token is valid.
     *
     * @param authToken
     * @return {@code true} if the authentication token is valid, otherwise {@code false}
     */
    boolean isTokenValid(String authToken);

    /**
     * Method to clean invalid tokens still existing in database.
     */
    void cleanInvalidToken();

    /**
     * Method to save a profile in database.
     *
     * @param profileDTO
     * @param profileType
     * @return
     */
    ProfileDTO saveProfile(ProfileDTO profileDTO, ProfileType profileType);

    Optional<ProfileDTO> getProfile(Long id, ProfileType profileType);

    Optional<ProfileDTO> getProfile(String authToken);

    /**
     * Method to generate an authentication token for each session open.
     *
     * @param authentication
     * @return
     */
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
