# 테이블을 활용한 락을 구현합니다.

## 구현 전략

- TDD 를 활용합니다.
- H2 DB 를 사용하여 인프라 편의성을 높입니다.

## 배운 내용

Unchecked exception 이 발생하면 롤백을 마크하기 때문에 해당 트랜젝션을 재사용하는 것은 불가능하다. (from. [응? 이게 왜 롤백되는거지?](https://techblog.woowahan.com/2606/))

트랜잭션을 분리하고자 하는 메서드를 반드시 클래스를 분리해서 적용해야한다. (from. [[TIL] 스프링 트랜잭션 따로 적용 하기 (REQUIRES_NEW), 클래스 분리 필요](https://whitepro.tistory.com/581))
