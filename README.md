# Toy for test on spring!

해당 레포지토리는 테스트 코드 추가를 위한 토이 프로젝트 입니다.
코드가 얼마나 정상 동작하는지, 프로덕션에서 잘 동작하는지를 검증하지는 말아주세요.
스프링에 테스트를 넣는 과정을 보여드리기 위해 만들어진 레포지토리입니다.
당연히 완벽하지 않습니다.

## 실행하기

### 00. 바로 시작

h2를 이용하여 `auto create table`을 하고 있기 때문에 바로 실행이 가능합니다.

### 01. 이메일 인증

> 단 이 프로젝트는 사용자가 가입할 때 이메일 인증을 하기위해 메일을 발송하는 코드가 있습니다.

이메일이 제대로 발송되는지 확인해보고 싶으신 분들은 [해당 document 파일](./document/connect-mail-sender.md)을 따라해주세요.
관련된 자료는 라이브러리나 Gmail 정책에 따라 UI와 방법이 달라질 수 있습니다.
최신화 된 정보를 제공하지 않으니, 가급적 문서를 참조해주시고, contribution 해주시면 감사하겠습니다.

## 관리 도구로 바로가기

- [h2-console](http://localhost:8080/h2-console)
- [Openapi-doc](http://localhost:8080/swagger-ui.html)

## 정리 내용 추가
[기존 방식의 문제점과 해결.md](document%2F%EA%B8%B0%EC%A1%B4%20%EB%B0%A9%EC%8B%9D%EC%9D%98%20%EB%AC%B8%EC%A0%9C%EC%A0%90%EA%B3%BC%20%ED%95%B4%EA%B2%B0.md)
  
[테스트와 의존성, Testability.md](document%2F%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%99%80%20%EC%9D%98%EC%A1%B4%EC%84%B1%2C%20Testability.md)