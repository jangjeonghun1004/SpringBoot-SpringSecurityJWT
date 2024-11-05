# JWT(JSON Web Token)

    JWT(JSON Web Token)은 정보를 JSON 형태로 안전하게 전송하기 위한 토큰입니다.
    JWT는 주로 서버와의 통신에서 권한 인가를 위해 사용합니다.

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

# 스프링 시큐리티 인증 수행 과정 및 JWT 적용

    선행 정보
    스프링 시큐리티(Spring Security) 사용 설정을 한 경우 서블릿 필터(Servlet Filter)를 기반으로 동작한다.
    서블릿 필터(Servlet Filter)는 DispatherServlet 앞에 배치된다.

    1. 사용자 요청
    2. 사용자 요청을 서블릿 필터(Servlet Filter)가 받은 후 SecurityFilterChain으로 작업을 위임한다.
    3. 작업을 위임 받은 SecurityFilterChain은 AuthenticationFilter에게 인증 처리를 요청합니다.
    4. AuthenticationFilter는 UsernamePasswordAuthenticationToken을 통해 인증을 처리합니다.
    5. AuthenticationFilter는 HttpServletRequest에서 username과 password를 추출한 후 토큰을 생성합니다.
    6. 추출된 username, password 토큰을 ProviderManager(AuthenticationManager 구현체)에게 전달합니다.
    7. ProviderManager는 토큰을 AuthenticationProvider에게 전달하고 
    8. AuthenticationProvider는 UserDetailsService에 전달합니다.
    9. UserDetailsService는 전달받은 정보를 통해 데이터베이스에서 일치하는 사용자를 찾아
       UserDetails 객체를 생성합니다.
    10. 생성된 UserDetails 객체는 AuthenticationProvider로 전달되며 인증을 수행하고 성공을 하게되면
        ProviderManager로 권한을 담은 토큰을 전달합니다.
    11. ProviderManager는 검증된 토큰을 AuthenticationFilter로 전달합니다.
    12. AuthenticationFilter는 검증된 토큰을 SecurityContextHolder에 있는 SecurityContext에 저장합니다.

    # JWT(Json Web Token) 적용
    인증 수행 과정의 4번째에 해당하는 UsernamePasswordAuthenticationToken을 통한 인증 처리 전에
    JwtAuthenticationFilter가 먼저 인증을 수행하도록 설정합니다.
    
    JwtAuthenticationFilter를 추가하는 코드
    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

# JWT 회원 가입 수행 과정

    1. 사용자는 회원 가입을 요청합니다.
    2. 시스템은 "SecurityFilterChainConfig.java" -> "SecurityFilterChain()" 메소드 "requestMatchers()"에 의해 접근을 허용합니다.
    3. 시스템은 "UserRepository.java"을 사용해서 회원 가입을 완료합니다.

# JWT 회원 로그인 수행 과정

    1. 사용자는 회원 로그인을 요청합니다.
    2. 시스템은 "SecurityFilterChainConfig.java" -> "SecurityFilterChain()" 메소드 "requestMatchers()"에 의해 접근을 허용합니다.
    3. 시스템은 사용자가 입력한 정보를 사용해서 회원 정보를 확인합니다.
       3.1 회원 정보가 존재하는 경우
           3.1.1 시스템은 JwtTokenProvider.createToke()을 호출합니다.
           3.1.2 시스템은 생성된 Token을 사용자에게 전달합니다.
           3.1.3 사용자는 시스템이 전달한 Token을 사용해서 요청을 합니다.
       3.2 회원 정보가 존재하지 않는 경우
           3.2.1 시스템은 "인증 실패"를 사용자에게 전달합니다.

# JWT Token을 사용한 요청 수행 과정

    1. 사용자는 로그인 후 전송 받은 Token을 헤더 정보에 설정합니다.
    2. 시스템은 "SecurityFilterChainConfig.SecurityFilterChain()" 메소드 "addFilterBefore()"에 의해 설정된
       "JwtAuthenticationFilter.doFilterInternal()" 메소드를 호출합니다.
    3. "JwtAuthenticationFilter.doFilterInternal()"은 사용자 요청당 한번씩 토큰의 유효성을 확인합니다.
       3.1 토큰이 유효한 경우
           "setAuthentication()" 메소드를 사용해서 인증을 처리합니다.
       3.2 토큰이 유효하지 않은 경우
           시스템은 "인증 실패"를 사용자에게 전달합니다.

    인증 성공 후
    SecurityContextHolder에 저장할 Authentication을 생성한다.
    UsernamePasswordAuthenticationToken을 사용해서 Authentication을 생성할 수 있다.

# 핵심 요약

    1. 사용자의 모든 요청은 SecurityFilterChainConfig.filterChain()을 호출합니다.
       1.1 사용자의 요청 권한이 유효한 경우
           1.1.1 시스템은 JwtAuthenticationFilter.doFilterInternal()을 호출합니다.
       1.2 사용자의 요청 권한이 유효하지 않은 경우
           1.2.1 시스템은 "인증 실패"를 사용자에게 전달합니다.

# 추가 내용

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    요청 본문을 처리할 때는 사용되고, 응답 결과를 생성할 때는 해당 필드는 제외되서 응답 본문에 표시되지 않게 됩니다.

    @Builder.Default
    Builder의 기본 값을 설정합니다.

    @Builder
    public class SomeClass {
        @Builder.Default
        private String name = "james";
    }

    @ElementCollection
    기본적으로 지정(hobbies) 필드의 값 타입 컬렉션으로 매핑하여 user_hobbies 테이블 생성한 후 연결합니다.

    @ElementCollection(fetch = FetchType.EAGER)
    기본 엔티티가 조회될 때 @ElementCollection으로 정의된 필드도 즉시 함께 조회됩니다.
    FetchType.LAZY는 FetchType.EAGER의 반대 개념.
    
    @RequiredArgsConstructor
    final 필드나, @NonNull 이 붙은 필드를 기준으로 생성자를 생성한다.

# Json Web Token 사이트

    https://jwt.io

    깃허브 테스트를 위한 첫번째 수정
