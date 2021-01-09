package com.dashboard.eleonore.profile.service;

import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.dto.UserDTO;
import com.dashboard.eleonore.profile.factory.ProfileFactory;
import com.dashboard.eleonore.profile.mapper.AuthenticationMapper;
import com.dashboard.eleonore.profile.mapper.UserMapper;
import com.dashboard.eleonore.profile.repository.AuthTokenRepository;
import com.dashboard.eleonore.profile.repository.AuthenticationRepository;
import com.dashboard.eleonore.profile.repository.UserRepository;
import com.dashboard.eleonore.profile.repository.entity.AuthToken;
import com.dashboard.eleonore.profile.repository.entity.Authentication;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import com.dashboard.eleonore.profile.repository.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    public static final String AUTH_TOKEN_KEY = "eleonore_auth_token";
    public static final long AUTH_TOKEN_TIMEOUT = 30L;

    private final AuthenticationRepository authenticationRepository;
    private final AuthTokenRepository authTokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationMapper authenticationMapper;
    private final UserMapper userMapper;

    public ProfileServiceImpl(AuthenticationRepository authenticationRepository, AuthTokenRepository authTokenRepository, UserRepository userRepository) {
        this.authenticationRepository = authenticationRepository;
        this.authTokenRepository = authTokenRepository;
        this.userRepository = userRepository;
        this.authenticationMapper = Mappers.getMapper(AuthenticationMapper.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Override
    public boolean isValidAuthentication(Authentication authentication) {
        if (authentication == null || StringUtils.isEmpty(authentication.getLogin()) || StringUtils.isEmpty(authentication.getPassword())) {
            return false;
        }

        return this.authenticationRepository.exists(authentication.getLogin(), authentication.getPassword());
    }

    @Override
    public Optional<Authentication> getAuthentication(String login, String password) {
        return this.authenticationRepository.findByLoginPassword(login, password);
    }

    @Override
    public Optional<AuthToken> saveToken(Authentication authentication, String token) {
        if (StringUtils.isEmpty(token) || authentication == null || authentication.getId() == null) {
            return Optional.empty();
        }

        AuthToken authToken = new AuthToken();
        authToken.setAuthenticationId(authentication.getId());
        authToken.setToken(token);
        authToken.setCreatedDateTime(LocalDateTime.now());
        authToken.setModifiedDateTime(authToken.getCreatedDateTime());

        return Optional.of(this.authTokenRepository.save(authToken));
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return;
        }

        this.authTokenRepository.deleteByToken(token);
    }

    @Override
    public boolean isTokenValid(String authToken) {
        if (StringUtils.isEmpty(authToken)) {
            return false;
        }

        Optional<AuthToken> optionalAuthToken = this.authTokenRepository.findByToken(authToken);
        boolean tokenValid = false;
        if (optionalAuthToken.isPresent()) {
            AuthToken token = optionalAuthToken.get();
            LocalDateTime currentDateTime = LocalDateTime.now();
            if (token.getModifiedDateTime().plusMinutes(AUTH_TOKEN_TIMEOUT).isAfter(currentDateTime)) {
                tokenValid = true;
                // If the token is soon expired then the modified date is updated
                if (token.getModifiedDateTime().plusMinutes(AUTH_TOKEN_TIMEOUT).isBefore(currentDateTime.plusMinutes(5L))) {
                    log.info("eleonore - Token {} almost expired, its modified date is updated", token.getId());
                    token.setModifiedDateTime(currentDateTime);
                    this.authTokenRepository.save(token);
                }
            }
        }

        return tokenValid;
    }

    @Override
    public ProfileDTO saveProfile(ProfileDTO profileDTO, ProfileType profileType) {
        ProfileDTO savedProfileDTO = null;
        switch (profileType) {
            case USER:
                User user = this.userRepository.save(this.userMapper.userDTOToUser((UserDTO) profileDTO));
                savedProfileDTO = this.userMapper.userToUserDTO(user);
                profileDTO.getAuthentication().setProfileId(user.getId());
                break;
            case PROJECT:
            case ORGANIZATION:
            default:
        }

        this.authenticationRepository.save(this.authenticationMapper.authenticationDTOToAuthentication(profileDTO.getAuthentication()));

        return savedProfileDTO;
    }

    @Override
    public Optional<ProfileDTO> getProfile(Long id, ProfileType profileType) {
        switch (profileType) {
            case USER:
                UserDTO userDTO = null;
                Optional<User> optionalUser = this.userRepository.findById(id);
                if (optionalUser.isPresent()) {
                    userDTO = this.userMapper.userToUserDTO(optionalUser.get());
                }
                return Optional.ofNullable(userDTO);
            case PROJECT:
            case ORGANIZATION:
            default:
        }
        return Optional.empty();
    }

    @Override
    public Optional<ProfileDTO> getProfile(String authToken) {
        if (StringUtils.isEmpty(authToken)) {
            return Optional.empty();
        }

        Optional<Authentication> optionalAuth = this.authenticationRepository.findByToken(authToken);
        if (optionalAuth.isPresent()) {
            Authentication authentication = optionalAuth.get();
            return Optional.ofNullable(ProfileFactory.getProfile(authentication));
        }

        return Optional.empty();
    }

    @Override
    public void cleanInvalidToken() {
        List<AuthToken> removalAuthTokens = this.authTokenRepository.findAll()
                .stream()
                .filter(token -> token.getModifiedDateTime().plusMinutes(AUTH_TOKEN_TIMEOUT).isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(removalAuthTokens)) {
            log.info("eleonore - Auto clean {} invalid authentication tokens", removalAuthTokens.size());
            this.authTokenRepository.deleteAll(removalAuthTokens);
        }
    }
}
