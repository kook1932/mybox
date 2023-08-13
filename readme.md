# Storage 서비스 구축

### Description
*  readme 문서
* 라이브러리 & 버전, 서버 실행 방법, 소프트웨어 구조 등에 대한 문서입니다.

### Environment
* OpenJDK 11
* Spring Boot 2.7.14
* Spring Data Jpa
* H2 Database(in-memory)
* Swagger 2.9.2

### Usage
* jar 실행 방법(Windows 기준)
    * java 환경변수 등록
    * 명령 프롬프트(cmd)에서 프로젝트 폴더로 이동 : cd 프로젝트 저장 경로
    * 프로젝트 빌드 명령어 입력 : .\gradlew build
    * 프로젝트 실행 명령어 입력 : java -jar .\build\libs\assignment-0.0.1-SNAPSHOT.jar
    * localhost:80 으로 API 서버 사용 가능

### API Document
* API 명세서 : http://localhost/swagger-ui.html

### Architecture
* 공통 모듈 : com.mybox.common
    * AOP, Exception, Util 등 프로젝트 공통으로 사용하는 기능 정의
* 도메인
    
* 테스트
    * DataJpaTest, Mockito, WebMvcTest 등을 활용하여 Unit Test 작성