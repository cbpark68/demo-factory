## 모던 자바 개발데모 입니다.
- 자바+스프링부트+JPA로 구성된 모던 자바 개발환경입니다.
- 자바+스프링+마이바티스로 구성된 레거시 자바 개발환경은 https://github.com/cbpark68/bookShop02 에서 확인할 수 있습니다.

## 데모용 REST-API 서버 개요
- 생산시설인 팩토리의 사용자와 생산설비의 정보를 REST-API 서비스합니다.
- 기본적인 테스트 케이스외 예외적인 상황을 대비한 다양한 테스트 케이스를 구현했습니다.
- REST-DOC을 이용해서 테스트케이스를 통해 REST-API 규격 문서를 자동으로 생성합니다.
- 컨트롤러에서 DTO를 서비스로 전달하면 서비스는 요청에 맞는 DTO 또는 엔티티를 리턴합니다.
- 엔티티는 서비스와 JPA에서만 정상적으로 이용하고 서비스를 벗어나면 제한된 이용만 가능합니다.
- open-in-view = false 설정을 통해서 서비스단계를 벗어나면 트랜잭션을 반납하도록 해서 커낵션풀을 효율적으로 관리합니다.
- 엔티티는 Setter를 모두 제거하고 별도의 modify메소드를 통해서만 수정하도록 했습니다.
- 엔티티는 생성자,수정자,생성시간,수정시간 등의 감사정보를 상속받아 구현하고 서버에서 자동으로 관리합니다.
- DTO는 기본용,리스트용 외에도 비즈니스 요구에 맞게 추가로 생성했습니다.
- 엔티티와 DTO는 서로가 생성자를 통해서 생성할 수 있습니다. 이로 인해 컨트롤러나 서비스에서 깔끔한 소스를 유지할 수 있습니다.
- 컨트롤러,서비스에서는 핵심적인 로직코드만 남기고 부가적인 코드는 별도 유틸객체로 옮겼습니다.
- 중요한 설정정보는 프로퍼티파일에 저장했습니다.

## 이 프로젝트의 용도
- 제가 REST-API와 테스트케이스를 구현할수 있음을 보여드립니다.
- 제가 REST-DOC를 이용해서 API문서를 자동으로 생성할수 있음을 보여드립니다.
- 제가 JPA의 일대다,다대일관계를 정확하게 이해하고 구현할수 있음을 보여드립니다.
- 제가 엔티티와 DTO의 용도와 한계를 잘 이해하고 구현할수 있음을 보여드립니다.
- 제가 테이블 감사기능을 이해하고 구현할수 있음을 보여드립니다.

## 테스트케이스 실행화면
![테스트케이스](https://github.com/cbpark68/demo-factory/blob/main/src/main/resources/static/images/%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%BC%80%EC%9D%B4%EC%8A%A4.png)

## REST-DOC을 이용한 문서 자동화
![API규격서1](https://github.com/cbpark68/demo-factory/blob/main/src/main/resources/static/images/REST-DOC1.png)

![API규격서2](https://github.com/cbpark68/demo-factory/blob/main/src/main/resources/static/images/REST-DOC2.png)

## Swagger-ui를 이용한 문서 자동화
![Swagger-ui](https://github.com/cbpark68/demo-factory/blob/main/src/main/resources/static/images/Swagger-ui.png)

## REST-DOC VS Swagger-ui
- 이 프로젝트에는 REST-DOC 뿐 아니라 Swagger-ui도 이미 구현되어 있습니다.
- 그러나 문서자동화는 REST-DOC을 이용했습니다.
- Swagger-ui는 컨트롤러에 등록된 모든 API가 조회되고 추가적인 어노테이션으로 소스가 상당히 지저분해집니다.
- 반면 REST-DOC은 API를 선택할수 있고 테스트를 통과해야만 작성되므로 관리가 더 용이합니다.

## ERD
- 팩토리 - factory
- 사용자 - user
- 설비 - facility
![ERD](https://github.com/cbpark68/demo-factory/blob/main/src/main/resources/static/images/ERD.png)

