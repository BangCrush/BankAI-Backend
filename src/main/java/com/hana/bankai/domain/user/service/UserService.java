package com.hana.bankai.domain.user.service;

import com.hana.bankai.domain.user.dto.UserRequestDto;
import com.hana.bankai.domain.user.dto.UserResponseDto;
import com.hana.bankai.domain.user.entity.Role;
import com.hana.bankai.domain.user.entity.User;
import com.hana.bankai.domain.user.entity.UserJob;
import com.hana.bankai.domain.user.entity.UserTrsfLimit;
import com.hana.bankai.domain.user.repository.UserJobRepository;
import com.hana.bankai.domain.user.repository.UserRepository;
import com.hana.bankai.domain.user.repository.UserTrsfLimitRepository;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import com.hana.bankai.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.hana.bankai.global.common.response.SuccessCode.*;
import static com.hana.bankai.global.error.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserJobRepository userJobRepository;
    private final UserTrsfLimitRepository trsfLimitRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));
    }

    // 회원가입 여부 확인
    public ApiResponse<UserResponseDto.RegisterDuplicateCheck> registerCheck(UserRequestDto.RegisterCheck request) {
        // return value. 계정이 이미 존재하면 true, 아니면 false 반환
        Boolean value = false;

        if (userRepository.existsByUserInherentNumber(request.getUserInherentNumber())) {
            value = true;
        }

        return ApiResponse.success(USER_REGISTER_CHECK_SUCCESS, new UserResponseDto.RegisterDuplicateCheck(value));
    }

    // 중복 이메일 여부 확인
    public ApiResponse<UserResponseDto.RegisterDuplicateCheck> registerCheckEmail(UserRequestDto.RegisterCheckEmail request) {
        // return value. 중복이면 true, 아니면 false 반환
        Boolean value = false;

        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            value = true;
        }

        return ApiResponse.success(USER_REGISTER_CHECK_EMAIL_SUCCESS, new UserResponseDto.RegisterDuplicateCheck(value));
    }

    // 중복 아이디 여부 확인
    public ApiResponse<UserResponseDto.RegisterDuplicateCheck> registerCheckId(UserRequestDto.RegisterCheckId request) {
        // return value. 중복이면 true, 아니면 false 반환
        Boolean value = false;

        if (userRepository.existsByUserId(request.getUserId())) {
            value = true;
        }

        return ApiResponse.success(USER_REGISTER_CHECK_ID_SUCCESS, new UserResponseDto.RegisterDuplicateCheck(value));
    }

    // 회원가입
    public ApiResponse<Object> register(UserRequestDto.Register register) {
        // RequestDto to Entity
        User user = User.builder()
                .userId(register.getUserId())
                .userPwd(passwordEncoder.encode(register.getUserPwd()))
                .userNameKr(register.getUserNameKr())
                .userInherentNumber(register.getUserInherentNumber())
                .userPhone(register.getUserPhone())
                .userAddr(register.getUserAddr())
                .userAddrDetail(register.getUserAddrDetail())
                .userNameEn(register.getUserNameEn())
                .userEmail(register.getUserEmail())
                .roles(Collections.singletonList(Role.ROLE_USER.name()))
                .build();

        userRepository.save(user);

        // UserJob 생성
        UserJob userJob = UserJob.builder()
                .user(user)
                .build();

        userJobRepository.save(userJob);

        // UserTrsfLimit 생성
        UserTrsfLimit userTrsfLimit = UserTrsfLimit.builder()
                .user(user)
                .build();
      
        trsfLimitRepository.save(userTrsfLimit);

        return ApiResponse.success(USER_REGISTER_SUCCESS);
    }

    // 로그인
    public ApiResponse<UserResponseDto.TokenInfo> login(UserRequestDto.Login login) {
        // 로그인 정보 계정 조회
        if(userRepository.findByUserId(login.getUserId()).orElse(null) == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        // 1. 로그인 아이디, 비밀번호를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
        // authenticationToken 내에는 id, pwd 들어가 있다

        // 2. 실제 검증(사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 함수가 실행될 때 loadUserByUsername 함수 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // authenticationManagerBuilder -> id, pwd 일치 여부 판단
        // authentication -> 권한 정보 저장

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        log.info("RT: " + authentication.getName() + " : " + tokenInfo.getRefreshToken() + " : " + tokenInfo.getRefreshTokenExpirationTime() + " : " + TimeUnit.MILLISECONDS);

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return ApiResponse.success(USER_LOGIN_SUCCESS, tokenInfo);
    }

    // 아이디 조회
    public ApiResponse<UserResponseDto.LoginFindId> loginFindId(UserRequestDto.LoginFindId request) {
        String userId = userRepository.findUserIdByUserNameKrAndUserEmail(request.getUserNameKr(), request.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return ApiResponse.success(USER_FIND_ID_SUCCESS, new UserResponseDto.LoginFindId(userId));
    }

    // 비밀번호 조회
    public ApiResponse<UserResponseDto.LoginFindPwd> loginFindPwd(UserRequestDto.LoginFindPwd request) {
        String userPwd = userRepository.findUserPwdByUserNameKrAndUserIdAndUserEmail(request.getUserNameKr(), request.getUserId(), request.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return ApiResponse.success(USER_FIND_PWD_SUCCESS, new UserResponseDto.LoginFindPwd(userPwd));
    }

    /* token 재발급
    public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if (ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }
    */

    // 로그아웃
    public ApiResponse<Object> logout(UserRequestDto.Logout logout) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            throw new CustomException(USER_LOGOUT_ACCESS_TOKEN_VALIDATION_FAIL);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User ID 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
        // 5. 이후 JwtAuthenticationFilter 에서 redis 에 있는 logout 정보를 가지고 와서 접근을 거부함

        return ApiResponse.success(USER_LOGOUT_SUCCESS);
    }

    // 회원 정보 조회
    public ApiResponse<UserResponseDto.UserInfo> getUserInfo(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_GET_INFO_FAIL));

        return ApiResponse.success(USER_GET_INFO_SUCCESS, UserResponseDto.UserInfo.from(user));
    }

    // 회원 정보 수정
    public ApiResponse<Object> updateUserInfo(String userId, UserRequestDto.UserInfo request) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_UPDATE_INFO_FAIL));

        userRepository.updateUser(
                user.getUserCode(),
                passwordEncoder.encode(request.getUserPwd()),
                request.getUserEmail(),
                request.getUserPhone(),
                request.getUserAddr(),
                request.getUserAddrDetail(),
                request.getUserMainAcc()
        );

        trsfLimitRepository.updateDailyLimit(
                user.getUserCode(),
                request.getUserTrsfLimit()
        );

        return ApiResponse.success(USER_UPDATE_INFO_SUCCESS);
    }

    // 직업 정보 조회
    public ApiResponse<UserResponseDto.UserJobInfo> getUserJobInfo(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_GET_JOB_INFO_FAIL));

        return ApiResponse.success(USER_GET_JOB_INFO_SUCCESS, UserResponseDto.UserJobInfo.from(user.getUserJob()));
    }

    // 직업 정보 수정
    public ApiResponse<Object> updateUserJobInfo(String userId, UserRequestDto.UserJobInfo request) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_UPDATE_JOB_INFO_FAIL));

        userJobRepository.updateUserJob(
                user.getUserCode(),
                request.getJobName(),
                request.getCompanyName(),
                request.getCompanyAddr(),
                request.getCompanyPhone()
        );

        return ApiResponse.success(USER_UPDATE_JOB_INFO_SUCCESS);
    }

}
