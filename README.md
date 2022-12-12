# SNOBS

👋 SNOBS의 동작 상세 문서입니다.

- Github

  [https://github.com/kswamen/snobs](https://github.com/kswamen/snobs)


## SNOBS란?

사용자의 독서내역을 바탕으로 동작하는 랜덤 채팅 웹 어플리케이션 토이 프로젝트입니다.

![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled.png)

## 기술 스택

Spring Boot, Hibernate, Spring Security, OAuth2, WebSocket, React

NGINX, AWS EC2, RDS, ElastiCache, S3

## 프로젝트 아키텍처

![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%201.png)

## ERD

![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%202.png)

## 1. Authentication / Authorization

### 1-1. Login Process

카카오, 구글의 OAuth2 소셜 로그인 또는 사용자의 이메일과 패스워드로 로그인할 수 있습니다.

처음 SNOBS에 가입한 유저가 소셜 로그인을 진행할 경우, 다음과 같은 프로세스로 이루어집니다.

![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%203.png)

클라이언트는 서버로부터 발급받은 accessToken과 refreshToken을 쿠키에 저장합니다.

이 때 쿠키에는 Secure와 Http Only 속성을 적용시킵니다.

발급받은 토큰은 다음과 같은 정보를 포함하고 있습니다.

![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%204.png)

토큰은 시작 및 만료 일자, 이메일로 구성된 단순 정보를 포함합니다.

### 1-2. 예외 처리

JWT 토큰 검증은 커스텀 인터셉터와 어노테이션을 사용하고, 검증된 사용자 정보를 Spring Security가 제공하는 SecurityContext에 저장합니다.

인증/인가 과정에서 발생하는 대표적인 예외 상황에는 **토큰 만료, 토큰 위/변조, 권한 없음** 등이 있습니다.

토큰 검증에 필터를 사용할 경우, **WAS 단에 속한 필터의 특성상 위와 같은 예외 상황들을 Spring 단에서 잡아 처리해 줄 수 없다는 문제점**이 발생합니다. 때문에 기존 필터 체인 방식을 제거하고 인터셉터를 대신 사용하되, 하나의 트랜잭션 내부에서 인증된 사용자 정보에 자유롭게 접근할 수 있도록 SecurityContext를 그대로 사용하는 방식을 채택했습니다.

![Filter Chain을 거치며 SecurityContext 생성, Interceptor를 거치며 유저 인증 정보 저장](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%205.png)

Filter Chain을 거치며 SecurityContext 생성, Interceptor를 거치며 유저 인증 정보 저장

![토큰 미포함과 같은 인증/인가 예외 상황 또한 Spring 단에서 통합 처리 가능하게 되었다.](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%206.png)

토큰 미포함과 같은 인증/인가 예외 상황 또한 Spring 단에서 통합 처리 가능하게 되었다.

## 2. Services

스놉스에서는 서버의 응답 메세지로 다음과 같은 구조를 사용하고 있습니다.

![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%207.png)

status는 Http 상태 코드와 일치하며, message는 오류 상세 정보, code는 스놉스 내부에서 사용하는 오류 코드, payload는 요청 성공 시 응답 내용이 들어가 있으며 응답 실패 시에는 **null** 값입니다.

서비스에 접근할 때, 적절한 권한을 보유하고 있지 않다면 아래와 같은 결과 화면이 나타납니다.

![접근 권한이 없는 유저](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%208.png)

접근 권한이 없는 유저

스놉스는 다음과 같은 기능을 지원합니다(**화살표를 클릭하여 펼치기**).

- **프로필 확인**

  ![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%209.png)

  프로필 정보의 확인 및 수정이 가능합니다.

  수정을 원할 경우 사용자 이메일 인증을 다시 거쳐야 합니다.

- **독후감 등록 및 수정**

  스놉스에서는 도서 검색을 위해 **Kakao 책 검색 API**를 활용하고 있습니다.

  ![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2010.png)

  검색 결과를 클릭하면 책을 선택하며, 이후 해당 책에 대한 독후감을 작성할 수 있습니다.

  메뉴에서 본인이 작성한 독후감 목록을 확인할 수 있습니다.

  ![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2011.png)

- **그 날의 독후감, 리액션 전송**
  1. **그 날의 독후감**

  스놉스는 매일 다른 유저가 작성한 독후감들을 3편 추천해 줍니다.

  ![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2012.png)

  마음에 드는 독후감을 클릭하면, 작성자의 프로필과 해당 작품에 대해서 작성자가 기입한 질문을 함께 확인할 수 있습니다.

  ![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2013.png)

  1. **리액션 전송 및 확인, 채팅방 생성**

  작성자의 질문에 응답하면 리액션이 전송되고, 상대방은 메뉴에서 내가 보낸 리액션을 확인할 수 있습니다.

  ![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2014.png)

  답변이 마음에 든다면 상대방은 새롭게 채팅방을 새롭게 생성할 수 있습니다.

  만약 해당 유저와의 채팅방이 이미 존재한다면, 채팅방을 새롭게 생성하는 것은 불가능합니다.

  ![리액션 세부 화면](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2015.png)

  리액션 세부 화면

  ![두 유저 간 채팅방에 이미 존재할 경우](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2016.png)

  두 유저 간 채팅방에 이미 존재할 경우

- **1:1 채팅**

  채팅방 목록의 모습은 아래와 같습니다.

  ![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2017.png)

  기존 대화 내역이 있다면, 채팅방에 입장할 때 이전 대화 내역을 먼저 불러옵니다. 이후 채팅이 발생할 때마다 채팅 내역을 서버에 전송해 업데이트합니다.

  기본적으로 웹소켓을 사용해 동작하며, 채팅방에 입장할 때 소켓을 연결하고, 다른 메뉴로 이동하는 등 채팅방에서 나갈 때 소켓 연결을 종료합니다.

  ![Untitled](SNOBS%202b3509031d6748feb1232decfe277aa6/Untitled%2018.png)