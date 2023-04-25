
# 유저관리 프로젝트
------------------
### 개요
> 1. JPA연습과 redis 및 OCI 구축을 위한 기초적인 프로젝트가 필요했다.
> 2. 앞으로 만들게 될 프로젝트들은 회원에 대한 기능이 필수적으로 있을 것 같아서 뼈대를 만든다는 생각으로 구현하게됐다.


### 구현 내용
> 1. 스프링 시큐리티를 이용하여 유저와 관리자의 접근권한을 차별화하였다
> 2. 회원이 이메일 인증을 진행할 시 redis 세션을 이용하여 인증정보에 대한 검증을 진행했다.
> 3. 공통 에러 처리를 통해 제공되지 않는 페이지와 접근불가 페이지에 대한 처리를 하였다.
> 4. 실제 운영되는 서버 구축을 위해 OCI를 활용하였다.
> 5. JPA를 활용하여 서비스 로직에 좀 더 집중할 수 있는 환경을 만들었다.

### 개발 환경
- OS : Windows 10
- WAS : Spring Boot 내장 톰캣
- IDE : VSCode, IntelliJ
- Build Tool : gradle
- JAVA : 1.8
- FrameWork
  - Spring Boot : 3.x
  - BootStrap
- Library
  - jQuery
  - Thymeleaf
- RDBMS, DBMS
  - SQL 
    - OracleCloud 10
  - NoSql
    - Redis 3.0.504
- ORM : JPA
- CI/CD : GitHub, Jenkins

### 회원기능
---
1. 회원가입,로그인 기능
2. 이메일 인증 기능
3. 아이디/비밀번호 찾기 기능
4. 마이페이지, 본인정보 수정 기능
---
### 관리자기능
1. 회원 상태 변경 기능

