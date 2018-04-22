# 챕터 1) 스프링부트로 웹 서비스 출시하기 - 1. SpringBoot & Gradle & Github 프로젝트 생성하기
Ref: http://jojoldu.tistory.com/250?category=635883

---
# 챕터 2) SpringBoot & JPA로 간단 API 만들기

## 키워드
- ORM
  - JPA
    - JPA Auditing
- Lombok
- JodaTime, LocalDate & LocalDateTime
- JUnit
- In-Memory Database
  - H2
- Intellij - .http

## 본격적인 JPA(Java Persistent API) 사용 시작
### 앞서, ORM이란?
> RDB 테이블을 객체지향적으로 사용하기 위한 기술  
=> 데이터베이스 테이블을 객체지향적 관점으로 다루기 위한 기술

### 그렇다면, JPA란?
> JPA는 여러 ORM 전문가가 참여한 EJB 3.0 스펙 작업에서 기존 EJB ORM이던 Entity Bean을 JPA라고 바꾸고 JavaSE, JavaEE를 위한 영속성(persistence) 관리와 ORM을 위한 표준 기술이다. JPA는 ORM 표준 기술로 Hibernate, OpenJPA, EclipseLink, TopLink Essentials과 같은 구현체가 있고 이에 표준 인터페이스가 바로 JPA이다.
  
*출처: http://blog.woniper.net/255  
  
최근에서야 NPM 패키지인 `Sequelize`를 통해 ORM에 입문한 내게 ORM은 정말 문명의 발달, 기술 발전의 신비함을 느끼게 해주는 기술이었다.  
Java를 주로 다루다가 Node로 넘어간 내겐 Spring Framework는 Express보다 생산성이 현저히 떨어질것이란 고정관념이 박히고 말았는데, 생각보다 생산성이 뛰어나 깜짝놀랐다.  
더불어 전공 프로젝트로 진행한 [SignMe프로젝트](https://github.com/Nooheat/SignMe)에서 JPA를 함께 쓰지 못하고 일일이 쿼리를 작성했던 내가 안타깝다는 생각마저 들었다. ~~삽질을 해봤기 때문에 ORM이란 기술에 더 감사할 수 있는 것같다.~~

### JPA에서 제공하는 어노테이션
- `@Entity`
  - 테이블과 링크될 클래스임을 알려줌
  - 언더스코어 네이밍룰로 이름을 매칭 (AppleStore.java -> apple_store 테이블)
- `@Id`
  - PK 필드임을 나타냄
- `@GeneratedValue`
  - PK값 자동 생성 규칙
  - default: AUTO (auto_increment)
  - ref: http://jsaver.tistory.com/entry/Id%EC%99%80-GeneratedValue-%EC%95%A0%EB%85%B8%ED%85%8C%EC%9D%B4%EC%85%98
- `@Column`
  - 테이블 컬럼, 굳이 선언하지 않아도 됨
  - 그렇다면 왜 사용하느냐? -> 특수한 변경이 필요한 상황
  - 타입을 TEXT로 변경하거나 문자열의 길이 제한을 변경하는 등의 경우에 사용

## JPA Auditing
매번 DB에 insert하기 전, update하기 전에 날짜 정보를 등록/수정해야하는 번거로움을 줄여줌
  
### 어노테이션
- `@MappedSuperClass`
  - JPA Entity 클래스들이 해당 클래스를 상속할경우 필드들도 컬럼으로 자동 추가
- `@EntityListeners(AuditingEntityListener.class)`
  - 해당 클래스에 Auditing 기능 포함
- `@CreatedDate`
  - Entity가 생성되어 저장될 때 시간이 자동저장됨
- `@LastModifiedDate`
  - 조회한 Entity의 값을 변경할 때 시간이 자동저장

### 적용법
`@MappedSuperClass` 어노테이션을 달고있는 클래스를 `extends`  

### 느낌
- 앞서 언급했던 `Sequelize`의 `timestamps` 옵션과 비슷한 느낌
- `mongoose`의 `hook` 기능축소버전? (hook은 더 다양한 작업을 할 수 있기 때문)

## JodaTime -> LocalDate & LocalDateTime
SignMe프로젝트를 진행하면서 날짜(시간)에 대한 API가 굉장히 부실하다는 생각을 받아 JodaTime의 도움을 받아 해결했었는데, Java8에 추가된 LocalDate를 통해 해결이 가능했다는걸 알았으면... 참 좋았겠다.  
아는 만큼 보이고 모르는 만큼 삽질하는구나 ㅋㅋ

### Lombok
`getter`/`setter` 생성같은 반복적인 귀찮은 작업부터 `builder`패턴의 구현까지 많은 작업들을 대신해주는 고마운 녀석  
  
하지만 무분별하게 사용하면 오히려 독이되기 때문에 사용에 신중해야한다.  
[Lombok 주의사항](http://kwonnam.pe.kr/wiki/java/lombok/pitfall)  
  
Intellij 플러그인으로 쉽게 받아볼 수 있다.  
**Enable Annotation Processors를 체크해줘야 정상동작함 (Intellij 기준)**

## JUnit 사용
### JUnit?
> Java의 단위 테스트(유닛 테스트) 프레임워크, assertXXX 형태의 간단한 사용법이 특징

### JUnit 워크플로우
`@BeforeClass` (최초 1회) -> [ `@Before` -> `@Test` -> `@After` ] **n회** -> `@AfterClass`  
  
### 느낌
최근에 접한 npm 패키지 `mocha`의 원조 격이라는 느낌이 들었다.  
어노테이션을 활용한 쉽고 빠른 테스트케이스 작성이 인상적이었다.  

## In-Memory Database "H2" 사용
### In-Memory Database?
> 인메모리 데이터베이스는 디스크에 최적화된 데이터베이스보다 더 빠른데 그 까닭은 디스크 접근이 메모리 접근보다 느리기 때문이며, 이 데이터베이스는 내부 최적화 알고리즘디 더 단순하며 더 적은 CPU 명령을 실행한다. 메모리의 데이터에 접근하면 데이터를 조회할 때 검색 시간이 줄어들며 디스크보다 더 빠르고 더 예측 가능성 성능을 제공한다.

*출처 : https://ko.wikipedia.org/wiki/%EC%9D%B8%EB%A9%94%EB%AA%A8%EB%A6%AC_%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4  
**하지만 RAM의 휘발성 탓에 해당 데이터베이스를 사용하고있는 프로그램이 종료되거나 기기의 전원 공급이 중단되면 데이터가 다 날아가버린다**. 이를 해결할 수 있는 **비휘발성 RAM**이란 것도 있다.

### H2
스프링부트에서 사용하는 **인메모리 데이터베이스**, 쿼리는 여타 SQL과 다름이 없다.

## .http 파일로 포스트맨 대체하기
> **Intellij 2017.3버전**부터 지원하는 **.http**파일을 이용하면 Postman 프로그램을 대체할 수 있다.

### 장점
- 하이라이팅 지원
- 직관적 구조
- 공유에 용이 (Git을 통해 관리가 가능, 파일을 넘겨주어 API호출 스크립트 공유 가능)

자세한 내용 > [IntelliJ의 .http를 사용해 Postman 대체하기](http://jojoldu.tistory.com/266)
## 댓글의 순기능
먼저 거쳐가신 선배님들의 댓글 덕에 장시간의 삽질로 이어질뻔한 오류를 금방 해결했다.  
선배님들 충성충성 ^^7

