# 요구 사항

- 만들어야 할 것은 문서 전자 결재 시스템입니다.
- 기술 요구 사항
  - spring boot, java 1.8 이상, mysql, vue.js & javascript
- 요구 사항 명세서
  - 완전히 동작하는 웹 어플리케이션을 작성해주세요.
  - 데이터를 영구히 저장하기 위해 dbms를 사용해주세요.
  - 사용자 모델과 회원가입 및 로그인 시스템이 필요합니다.
  - 사용자는 결재받을 문서를 생성할 수 있습니다.
  - 문서는 제목과 분류, 내용을 가집니다.
  - 문서 생성시 결재를 해주었으면 하는 사용자를 지정할 수 있습니다.
  - 결재자는 한명 이상이 될 수 있습니다.
    - 문서를 생성한 본인을 지정할 수도 있습니다.
  - 결재는 순서대로 진행됩니다.
    - 두번째 결재자가 먼저 결재할 수는 없습니다.
  - 모든 결재자가 승인하면 문서가 승인됩니다. 
    - 한명이라도 거절하면 거절됩니다.
  - 문서 승인/거절시 의견을 추가할 수 있습니다.
  - 사용자가 볼 수 있는 문서 목록은 다음과 같습니다.
    - OUTBOX: 내가 생성한 문서 중 결재 진행 중인 문서
    - INBOX: 내가 결재를 해야 할 문서
    - ARCHIVE: 내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서
- 화면 요구 사항
  - 회원가입 및 로그인할 수 있는 화면
  - 결재 문서들을 조회하는 화면
    - 검색 기능은 가능하면 추가해주세요.
  - 결제 상세 조회할 수 있는 화면
    - 최소한 승인자, 결재 상태, 제목, 내용를 조회할 수 있어야 합니다.
  - 결재를 승인, 거절할 수 있는 화면
- 문서산출물
  - 물리 DB 설계서 (ERD)
  - front source
  - server source

---

# Test Scenario

- 가입
  - 유효성 검사
    - 각 입력란 비우고 등록
    - 제한 길이 미만 초과하여 등록
    - 이미 등록된 user의 id 입력 후 등록
    - password와 repassword를 다르게 입력하고 등록
- 접속
  - list에 현재 접속한 user 이름 표시 확인
  - list를 불러와 출력하는지 확인
- 문서 등록
  - 유효성 검사
    - 각 입력란이 비었을 때 등록 실행
  - approver 등록 table의 row를 눌렀을 때, 지워지는지 확인
  - 중복되는 approver이 등록되는지 확인
  - inbox에 들어왔는지 확인
- 결재
  - approve
    - 다음 결재자로 순서가 넘어갔는지 확인
    - 뒷 결재자까지 전부 approve했을 때, 문서 상태가 approve로 바뀌는지 확인
  - reject
    - 앞 결재자가 reject하면 뒷 결재자들의 결재 상태가 cancel로 바뀌는지 확인
    - 문서 상태가 reject로 바뀌는지 확인
  - 결재된 문서가 archive에 들어갔는지 확인
    - archive에는 작성자, 결재자 모두에게 관련된 완료 문서가 나옴
- token이 없을 때
  - logout -> 뒤로가기 -> token이 필요한 요청 보내는 기능 실행
  - logout -> 뒤로가기 : token을 null로 만드는 과정
  - token이 null일 때 요청에 대한 처리 확인
- paging 확인
  - dummy 계정으로 접속
  - inbox, outbox, archive 각각 작동 확인
- 각 category에서 검색
  - 각 category 내의 검색 결과인지 확인
  - paging 동작 확인
- token 만료시 처리 확인
  - 확인을 위해 model.common.JwtManager의 expired_time을 10s로 조정
  - server restart
  - 접속 후 10초 기다리고 요청 처리 후 확인

---

# Front

- Vue CLI 3.11.0
- user token이 window.sessionStorage에 저장됨
  - server에 저장하지 않음

# Back

- Spring Boot 2.5.1
- MVC pattern
- MyBatis
- MySql

---

# Status Code

|code|status|
|-|-|
|100|success|
|200|no token for user|
|300|no data to read|
|310|unsuitable data to write|

- success : 1xx
  - 종류 상관없이 성공 : 100
- fail
  - 인증 : 2xx
    - no token : 200
  - data 통신 : 3xx
    - read : 30x
      - no data : 300
    - write : 31x
      - unsuitable data : 310
        - out of size
        - duplicated data
        - not same data

---

# 20210630 Code Review

### 자신에게 맞는 memo style

- markdown
- todo 정렬 기준
  1. 중요한 것
  2. 다른 것을 하기 위해 선행되어야 하는 것
  3. 빠르게 끝낼 수 있는 것

### Done

- user
  - login
  - regist
- document
  - list
    - search
    - outbox
    - inbox
    - archive
  - regist
    - add approver
  - detail
    - approve & reject

### To Add

- [x] token이 만료되었을 때, login page로 이동 (1h)
  - user.seq를 사용하는 모든 곳에서 검사
  - JwtManager.validatetoken method에서 token이 만료되었는지 확인
  - 만약 만료된 token이 들어오면 exception throw하고 status code를 front에 넘겨줌
  - front는 status code를 가지고 페이지 이동할지 결정
  - [x] token에 아무 값이 없을 때, login page로 이동
- [x] 현재 어떤 종류의 list를 보고 있는지 확인 가능하도록 button에 highlight (30m)
  - ex) outbox를 보고 있으면 outbox button에 highlight
- [x] 앞선 approver이 reject했을 때, 뒤의 남은 approver들의 상태값은 'proceed'에서 'cancel'이 되도록 하기 (1h)
- [x] 현재 사용자가 누구인지 확인 가능한 영역 추가 (30m)
  - ex) nav bar, list page
- [x] document regist에서 추가된 approver list를 선택하면 다시 삭제되는 기능 (30m)
- [x] table들에 header로 column 정보 제공 (10m)
- [x] document list paging (8h)
  - 검색할 때도 paging이 되도록 하기 위해선 getList() method 내에서 구현해야 함
  - /outbox, /inbox, /archive, /search 등 서로 다르던 url을 /list로 통합
    - 검색은 boolean을 사용해 mode on/off하는 식으로 전환
  - controller에서 category에 따라 분기하던 부분을 service 영역으로 옮김
  - controller에서 page에 필요한 만큼의 list를 만들어 view로 보내주기

### To Update

- [x] jwt(jjwt) & session (2h)
  - [x] header로 주고 받기 : header로 주고받고 server에는 사용자 정보를 저장하지 않음
    1. client : sessionstorage에 token 저장하고, 정보를 사용하고 싶을 때 server로 token을 header에 실어 request
    2. server : 받은 token을 해석하여 정보 조회 후, 해당 정보를 body에 실어 client로 response
  - [x] token 유효시간 설정
    - 유효시간 만료되면 login page로 이동
- [x] document detail page를 볼 때, nav에 들어가는 username 영역에 user_array가 출력되는 현상 해결하기 (1h)
  - 하위 component와 상위 component가 모두 lifecycle hook에 대한 처리를 하기 때문에 발생
  - nav component는 link만 연결하도록 하여 해결 (username은 list page에 나옴)
- [x] 통신의 용도에 따라 post, get 구분 및 수정 & parameter 수정 (30m)
  - 현재 token을 header로 전달하지 않아 post, get 통신에서 필요없는 parameter가 들어있음
    - token을 get으로 보내려다 보니 요청 주소가 너무 길어져 post 방식을 사용한 것이 여러개 있음
    - header로 전달하면 문제 해결 가능
- [x] document search : 모든 문서에서의 검색이 아닌 현재 tab(outbox, inbox, archive)에서만 검색 가능해야함 (1h)
  - 큰 list를 가져와서 java source 단에서 검사하면 항상 해당 category의 모든 list를 가져와야 함
  - 따라서, sql query문을 3개 더 만드는 것으로 결정
- [x] document registform에서 approver을 중복해서 추가할 수 없도록 수정 (1h)
  - approver_list.approver.user_seq와 selected user.seq를 비교하는 logic을 넣어 구현
  - [x] server 내의 logic에서도 유효성을 검사하도록 하기
- [x] responsedata 사용 세분화 (2h)
  - 성공 실패를 나타내기 위해 0, 1로만 지정하던 "status" parameter를 각 쓰임에 맞게 조정
  - [x] 명세표 작성
  - [x] 명세표에 따라 status code 수정
- [x] login할 때, id/password 중에 어느 것이 틀렸는지에 대한 정보 전달 (30m)
- [x] all list 지우기 (5m)
  - 사용자는 outbox, inbox, archive만 볼 수 있음
- [x] document regist할 때, 유효성 검사 통과하지 못했는데도 등록이 되는 문제 해결 (30m)

---

# 20210708 Code Review

### To Do

- [ ] 사용자가 접속하고 있다가도 token 시간이 만료되면 로그아웃 되는 현상 해결
  - refresh token 찾아보기
- [ ] detail 들어갔다가 나올 때 쪽수와 검색 모드가 풀리는 현상 해걸
  - local에서 값을 저장할 수 있는 수단 찾기
- [ ] end-point마다 있는 token을 가져오는 logic의 중복을 interceptor로 제거
- [ ] 여러개로 나뉘어져 있는 ApproverMapper을 사용하는 logic 수정
  - updateState(), updateOpinion(), updateFindate() -> updateStateOpinionFindate()
- [ ] status code를 상수 말고 enum으로 변경
  - public final class 에서 final을 사용한 이유는?
- [ ] @Data annotation 지양하는 code로 수정

- [ ] search mode 말고 search word가 있을 때로 구분?
- [ ] mybatis if test

- [ ] "group by" vs "distinct"
- [ ] approve시 approver의 수로 계산 x
- [x] ResponseData Member 변수로 들어간 것 수정
- [ ] DTO를 상속한 목적에 맞는 DTO를 만들어 상황에 맞게 사용
- [x] positive coding
  - ! (x)
  - == false (o)
- [x] if문 앞에 조건절 따로 빼기
- [ ] @Autowired에서 생성자 주입 방식으로 변경
- [ ] switch vs if else 고민
- [ ] approver update할 때, findate 가져올 때는 언제나 공통된 시간을 가져올 수 있도록 수정
  - query로 database에 입력 후 가져오는 방법 등 여러가지 방법이 있음
- [ ] controller에는 값 가공, 함수 호출 등의 logic만 있는 것이 좋음
- [ ] approver update 한번에 하기
  - 현재는 list를 따라 돌면서 하나씩 update
- for-each vs for 고민



