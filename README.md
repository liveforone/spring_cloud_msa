## 프로젝트 만들기전
* msa 뿐만아니라 모놀리틱 서비스를 만들더라도 반드시 파일명에는
* 대쉬(-)가 아닌 언더스코어/언더바(_)가 들어가야한다.
* 대쉬는 커맨드라인(터미널)에서 명령할때 주로 사용되기 때문이다.

## 스프링 클라우드 버전 체크
* 스프링 클라우드 release train 을 꼭 살펴보아야한다.
* 현재 스프링부트 버전에는 어떤 클라우드가 호환되는지 반드시 확인한다.
* 버전 문제때문에 gradle이 정상적으로 작동이 안될때가있다.
* 디펜던시에는 존재하는 채로 말이다...
* 따라서 이 경우에는 spring cloud사이트에 접속해 cloud관련 디펜던시들의 정확한 버전정보를 가져와서
* gradle에 넣어주면된다.
* ex : implementation 'org.springframework.cloud:spring-cloud-starter-openfeign:4.0.1'

## 사용기술
#### configuration management
* spring cloud config server
#### location transperency
* eureka (naming server)
#### load balancing
* ribbon (client)
* spring cloud gateway
#### rest client
* feign client
#### monitoring
* zipkin
* netflix api gateway
#### 장애복구
* hystrix

## spring cloud netflix eureka
* role : service discovery
* 말그대로 여러 마이크로 서비스를 가지고 있는 전화번호부 역할이고,
* 여러 서비스를 검색해준다.
* api gateway의 뒤 단계로 게이트 웨이로 받은 정보를 바탕으로 서비스를 찾아 리턴해준다.

## 마이크로 서비스에서 client 설정
* standard인 discovery client를 enable 하도록 한다.
* eureka.client.fetch.registry=true는 유레카 서버로부터 인스턴스 정보를 주기적으로 가져온다는 의미이다.
* 즉 갱신된 정보를 받겠다는 의미이다.
* 유레카 디폴트 존을 설정할때에는 localhost보다는 127.0.0.1:을 쓰는것이 좋다.

## localhost:8761
* 서비스가 up상태이면 작동중, 아니면 정지중인 상태이다.

## 서비스 복사
* 서비스 복사시에 environment에서 vmoption을 변경한다.
* -Dserver.port=9002

## 터미널로 실행
* 유의할점은 테스트를 위해 여러포트로 실행했지만,
* 실제 msa 운영환경에서는 스케일 업을 하지 않는이상 여러포트를 두지 않는다.
* 개인적으로는 jar 실행이 bootrun보다 낫다.(실제 배포와 비슷하기도 하고)
```
[하나만 실행할 경우1] - main
./gradlew.bat clean build
cd build
cd libs
java -jar 이름-0.0.1-SNAPSHOT.jar
ctrl c (^c : 종료)

[하나만 실행할 경우2]
./gradlew.bat clean build
./gradlew bootrun
./gradlew -stop

[여러 포트로 실행할 경우1] - main
./gradlew.bat clean build
cd build
cd libs
java -jar "-Dserver.port=9003" 이름-0.0.1-SNAPSHOT.jar
ctrl c (^c : 종료)

[여러 포트로 실행할 경우2]
./gradlew.bat clean build
./gradlew bootrun --args ' --server.port=포트번호'
./gradlew -stop
```

## 랜덤 포트
* 랜덤 포트 사용시 yml 파일은 다음과 같다.
* 스케일 업시 사용한다.
* 적극적으로 사용하는 것을 권장한다.
* 포트번호를 지정하지 않아도되어서 깔끔하고 귀찮지 않다.
```
server:
  port: 0

spring:
  application:
    name: user-service

eureka:
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
```

## api gateway 기본
* apigateway-service에서 매 요청이 들어 올때마다 service-discovery(eureka)에게 해당 요청의 위치를 물어서 확인한다기 보다는,
* 주기적으로 (기본값이 30초) eureka -> apigateway로 등록/해제 된 서비스들의 정보를 전달해 주면, 
* apigateway-service가 해당 정보를 기억하고 있다가, 자신이 사용해야 하는 시점에 해당 서비스로 요청을 전달하게 됩니다. 
* fetch-registry 정보가 이 부분을 설정하는 부분입니다.

## 필터 
* 요청정보가 들어오면 predicate가 판단하여 pre filter와 post filter를 거쳐 필터링 된다.
* 필터는 yml과 자바코드 두가지 방법으로 작성가능하다.
* custom 필터를 구축할때 ServerRequest를 사용하는 이유는
* 내장 서버가 netty, 즉 비동기 방식의 내장 서버를 사용하고 있어서
* 비동기에 맞지 않는 Servlet대신 Server를 사용하는 것이다.
* 또한 비동기 방식으로 리턴할때에는 Mono라는 타입을 사용한다.
* 커스텀 필터는 각 라우트 별 필터이고
* 글로벌 필터는 전체 서비스의 필터다 라고 생각하면된다.

## 설정 관리
* bootstrap.yml은 application.yml보다 상위 이다.

## 설정 반영
* 액츄에이터를 사용해서 설정을 반영할 때에는
* post방식으로 /url/actuator/refresh로 넣어주면된다.
* 이렇게 호출하면 설정정보가 반영된다.

## git
```
git remote -v : 깃 연결되었나 확인

[초기 설정]
git init
git add .
git commit -m "주석"
git remote add origin 링크
git push --set-upstream origin master

[커밋 및 푸시]
git commit -m ""
git push origin master
```

## RestTemplate - deprecate
* feign client를 사용하는 것이 더 좋다.
* 더이상 rest template을 지원하지 않고, 편리하지 않기 때문이다.
* 사용법은 아래와 같다.
```
[main.java]
@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

[서비스 로직]
String orderUrl = String.format(environment.getProperty("order_service.url"), id);
        ResponseEntity<List<OrderResponse>> orderResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<OrderResponse>>() {
        });
```

## feign client
* feign client는 resttemplate에 비해 편하게 사용 가능하다.
* 만약에 a서비스가 b서비스를 feign client를 통해 호출할때
* a서비스에 필터가 걸려있다면 어떻게 될까?
* 필터는 a서비스에만 걸린다.
* 이미 a서비스 필터에서 걸러졌기때문에 b서비스에서는 그 필터에 대해 고민할 필요가 없다.(ex: authorization)

## feign client 예외처리
* feign client와 같은 외부 리소스를 가져오는 작업을 할때에는 반드시 예외 처리를 해야한다.
* 예를들어 유저정보 반환시 주문정보도 반환 한다고 했을때,
* 주문에서 에러가 발생해 유저정보도, 주문정보도 모두 반환되지 않고 에러창만 뜨게되면 좋지않다.
* 주문정보는 에러 로그를 남기고, 에러없이 가져올 수 있는 유저 정보는 반환하도록 한다면 아주 좋을 것이다.
* 이러한 이유로 외부 리소스를 가져와 처리할 때에는 예외처리를 반드시 해야한다.
* feign client는 기본적으로 에러를 다루는 객체이다.
* 에러 디코더로 처리를 하는 것은 프론트엔드에게 정확한 에러 메세지를 던짐으로써
* 에러에 관해 클라이언트에게 전달하는 것을 프론트단에 완전히 위임하는 것이다.
* 클라이언트에게 상세히 에러를 전달하는 작업은 백엔드가 하지 않아도 되기 때문에 try-catch보다는 에러 디코더로 상세한 에러 정보를 프론트에 전달하는 것이 더 좋다.

## 카프카 파일 설명
#### config
* server.properties 파일은 카프카 실행시
* zookeeper.properties 파일은 주키퍼 실행시 사용한다.
#### bin/window
* server start or stop 배치 파일은 카프카 자체를 실행시
* zookeeper start or stop 배치 파일은 주키퍼 실행시 사용된다.

## 카프카 실행 순서
* 주키퍼를 실행하고 카프카를 실행해야한다.

## 카프카 서버 기동
```
[zookeeper 구동]
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

[kafka 구동]
bin\windows\kafka-server-start.bat config\server.properties

[topic 생성]
bin\windows\kafka-topics.bat --create --topic 토픽이름 --bootstrap-server localhost:9092 --partitions 1

[topic 목록 확인]
bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list

[topic 정보 확인]
bin\windows\kafka-topics.bat --describe --topic 토픽이름 --bootstrap-server localhost:9092

[topic 삭제]
server.properties 에서 
delete.topic.enable = true 로 설정해야함.
bin\windows\kafka-topics.bat --delete --zookeeper zookeeper:2181 --topic 토픽이름
```

## 카프카 프로듀서 post & 컨슈머 pull
```
[프로듀서 post]
bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic 토픽이름

[컨슈머 - pull]
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic 토픽이름 --from-beginning
```

## 카프카 커넥트 실행
```
bin\windows\connect-distributed.bat etc\kafka\connect-distributed.properties
```

## 카프카 멈추기
* 프로듀서와 컨슈머와 커넥트를 멈춘다.
* 카프카를 멈춘다.
* 주키퍼를 멈춘다.
* ctrl + c이다. 

## source connect
* 보내는 역할을 한다. 따라서 이름같은 것들을 잘 지정해주어야한다.
* POST : localhost:8083/connectors
* name : 커넥터 이름
* connector.class : 반드시 JdbcSourceConnector
* connector.url : 포트번호 조심(maria랑 mysql이랑 다름)
* mode: incrementing은 db에 insert가 들어갈때마다 generation value를 의미함.
* topic.prefix에는 source connect의 경우 _뒤에 이름을 지정하지 않아도 테이블이름이 들어간다.
```
{
    "name" : "소스 커넥터 이름(지정)",
    "config" : {
        "connector.class" : "io.confluent.connect.jdbc.JdbcSourceConnector",
        "connection.url":"jdbc:mysql://localhost:DB포트/스키마(DB)이름",
        "connection.user":"root",
        "connection.password":"159624",
        "mode": "incrementing",
        "table.whitelist":"테이블",
        "topic.prefix" : "원하는 이름_",
        "tasks.max" : "1"
    }
}
```

## sink connector
* 받는 역할을 한다.
* 자동으로 topics라는 이름의 스키마를 만들어준다.
* 해당 옵션은 auto.create":"true"
```
{
    "name":"싱크 커넥터 이름(지정)",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:mysql://localhost:포트/스키마이름",
        "connection.user":"root",
        "connection.password":"159624",
        "auto.create":"true",
        "auto.evolve":"true",
        "delete.enabled":"false",
        "tasks.max":"1",
        "topics":"my_topic_orders"
    }
}
```

## kafka로 전달시 유의 사항
* 토픽정보를 컨슈머와 프로듀서 모두 동일하게 작성하는데,
* 문자열로 하드 코딩하는게 좀 그러니,,
* 이름을 topic이라는 enum을 만들어서 getValue()로 값 참조해서 사용하는게 더 좋을것 같다.

## 사용처
* 중간매개체, 즉 주문시 수량 감소 와 같이 서비스 내의 service 로직에서 해결하지 못하는 외부에서 호출해야하는 것을
* feign client로 api로도 처리가 되지만 mq를 사용해서 처리도 된다는 것.
* 중간에 잠깐식들어가는 쿼리들, 즉 메인이 아닌 서비스에 한해서 mq로 처리해주면될것같다.
* 메인이 아니지만 많이 사용되는 것들.(ex:수량 up/dwon)
* DB에는 아무래도 직접 접근하는 것이 카프카를 덜 불편하게 사용하는 것 같다.
* 하나의 일만 하는 도메인은 아무래도 적다보니 여러일을 하는 경우에 다른 도메인으로 할일(json)을 보내줄때에는 mq를 사용해서 보내주기만 하면된다.
* 컨슈머는 받은 토픽으로 json에서 원하는 값을 꺼내서 사용하면된다.
* 데이터 동기화는 일반적으로 잘 사용하지 않으므로 커넥터로 데이터 동기화를 할일은 드물다.
* mq는 메시지 전송용으로만 쓰자.
* feign client로 json을 받아 jpa로 db에 값을 넣을때 주의 해야할점은 
* 트랜잭션이다. 결국 repository를 사용해 작업을 한다면 트랜잭션 어노테이션을 붙여야한다. 반드시!!

## 카프카 여러 메소드 하나의 토픽 사용예제
* 하나의 토픽을 여러 메소드, 즉 여러 컨슈머/프로듀서로 사용하는 방법이다.
* 보낼때 메세지 dto를 만들어서 해당 도메인의 responsedto가 어떤 방식으로 쓰일지
* type이라는 필드에 넣어서 사용하면된다.(enum으로 구성하든지 등등..)
```
 @KafkaListener(id = "myGroupId", topics = "#{'${kafka.topic.member}'.split(',')}")
    public void kafkaListener(MessageDto messageDto) {
        switch (messageDto.getType()) {
            case REGISTER: {
                memberService.register(messageDto);
                break;
            }
            case WITHDRAWAL: {
                memberService.withdrawal(messageDto.getId());
                break;
            }
            default: {
                throw new RuntimeException("wrong type error. type=" + messageDto.getType());
            }
        }
    }
```

## kafka json send - 대량 데이터 조회시 사용
* api로 호출을 하게되면 kafka로 json타입으로 보내준다.
* https://velog.io/@rainbowweb/kafka-%EC%97%B0%EC%8A%B5%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-2-JSON-%EC%A3%BC%EA%B3%A0%EB%B0%9B%EA%B8%B0
* 위의 json send 링크 참조

## 총정리
* feign client를 사용해서 적절하게 api 요청을 보낸다.
* 주로 조회할 때 사용되곤한다.
* save와 delete쿼리는 mq를 사용하지 않기로 했기때문에 이때 feign client를 사용해도 좋다.
* mq는 대량의 데이터를 가져올때 사용한다.
* 대량의 데이터가 아닌이상은 feign으로 가져오나, 대량일 경우 mq로 json parsing해서 가져와 사용해도 된다.
* feign은 동기식, mq는 비동기다. 성능이 필요하면 mq를, 간단한 작업은 feign을 쓴다.
* 토픽 등이 이름이 모두 문자열인데, 문자열로 하드코딩해 사용하지 말고,
* enum 클래스를 만들어서 깔끔하게 처리하자.yml은..지양하자,, 문자열과 별 다를게 없다...
* feign clinet를 사용할때에는 반드시 logger레벨을 full로 설정해 로그를 남기도록 한다.
* env 폴더를 만들어서 거기에 문자열 설정값들을 전부 enum으로 집어넣는다.
* 다른 서비스를 호출해서 값을 가져오는(조회쿼리)경우에는 서킷브레이커를 건다.
* 그 이외에 업데이트쿼리라던지 등은 서킷 브레이크를 걸 필요가 없다.

## 장애 처리
* 장애 처리는 circuit breaker로 하면되는데,
* api gateway에는 서비스가 정상적으로 이용이 안될때, 즉 서버가 죽었을때에 대한 서킷브레이커를 걸어주면되고
* (안걸어주면 500서버 에러뜨면서 게이트웨이 난리난다.)
* 각 서비스에는 각 url별로 서킷브레이커를 걸고 적절한 장애처리를 해주면된다.

## 설계시 주의점
* 반드시 도메인(각 엔티티)를 정확히 설계하여 고정하고
* db에 create 쿼리를 날린다. 이때 ddl은 당연히 none이다.
* 이후에 source와 sink 커넥터를 만들고(문서화 당연) 커넥팅하면된다.

## db connector 설정
* etc\kafka\connect-distributed.properties에 plugin.path=가 
* lib폴더로 잘 설정되어있는지 확인
* share\java\kafka 폴더에 jar파일로 집어넣으면됨

## 서버 번호
* zookeeper : 2181
* kafka : 9092
* connector : 8083

## 스프링 도커 create
1. Dockerfile 만들기
2. 디렉터리 최상단으로 진입
3. 프로젝트 clean build 하기
4. 아래 명령어 실행
* VOLUME /tmp 는 tmp라는 폴더를 만든다는 의미이다.
* container의 데이터 휘발성 때문에 데이터를 container가 아닌 호스트에 저장할 때,
* 또는 container끼리 데이터를 공유할 때 Volume를 사용한다.
* 계정명/프로젝트명 으로 해야 hub에 푸시가 가능해진다.
* 0.0.1은 tag다. 즉 버전을 의미한다. 일반적으로 배포시에는 1.0으로 버전을 수정하고 배포하는 편이다.
* 맨 마지막에 . 은 폴더내 모든 파일
```
[Dockerfile 양식]
FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=./build/libs/user-service-0.0.1.jar
COPY ${JAR_FILE} UserService.jar
ENTRYPOINT ["java","-jar","UserService.jar"]

[도커 command]
docker build --tag yc4852(계정명)/생성할이미지이름:버전 .
docker push yc4852(계정명)/생성한이미지이름:버전
```

## 도커 command
```
[이미지 조회]
docker image ls

[이미지 삭제]
docker rmi 이미지id

[컨테이너 조회]
docker container ls : 컨테이너 목록확인
docker container ls -a : 종료된 컨테이너 포함 목록확인

[멈춰있는 컨테이너 재실행]
컨테이너 조회후 컨테이너 id복사
docker start containerid

[컨테이너 삭제]
docker container rm 컨테이너id : 컨테이너 삭제(이미지 아님)

[도커 프로세스 확인]
docker ps -a

[도커 네트워크 조회]
docker network ls

[네크워크 상세 조회]
docker network inspect 네트워크이름

[도커 브릿지 네트워크 생성]
docker network create --gateway 172.18.0.1(ip주소) --subnet 172.18.0.0/16(서브ip주소) 네트워크이름

[컨테이너 실행]
docker run -d 파일명(계정명이 포함되어있어서 기재안함):태그

[discovery service(eureka) 컨테이너 실행]
# 포트번호가 있는 디스커버리나 게이트웨이는 포트번호를 명시한다.
docker run -d -p 포트번호:포트번호 --network 네트워크명 --name discovery-service 계정명/discovery-service:1.0

[마이크로 서비스 컨테이너 실행 - gateway + other service]
#yml에 다른 url을 가져왔다면 그것을 보고 그대로 적어주면된다.
#물론 같은 네트워크 안에 있을때 ip대신에 이미지명을 적는것이다.
#다른 네트워크일경우 불가능하다
docker run -d -p 포트번호:포트번호 --network 네트워크명 -e "eureka.client.serviceUrl.defaultZone=http://discoveryservice:8761/eureka/" --name 서비스명 계정명/서비스명:버전

[db 실행]
docker run --name 컨테이너명 --network 네트워크명 -e MYSQL_ROOT_PASSWORD=비밀번호 -d -p 13306:3306 mysql:latest

[db 접근]
docker exec -it 컨테이너명 bash
mysql -u root -p

[컨테이너 중지]
docker stop containerid

[로그파일 확인]
docker logs 컨테이너명
```

## 카프카 도커
* 일반적으로 카프카는 3대 이상 기동해야하지만 싱글 브로커를 활용해 한대만 기동 가능하다.
* 전체 디렉터리안에 wurstmeister/kafka-docker을 git clone을 해준다.
* 후에 yml에서 ip와 네트워크를 적절히 수정한다.
* ip를 직접적으로 명시하는 이유는 카프카와 주키퍼는 서비스안에서 ip를 명시하며 사용해야하기에
* 임의로 할당하기 보다는 직접 할당 시킨다.
```
[docker-compose-single-broker]
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
    networks:
      my-network:
        ipv4_address: 172.18.0.100
  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 172.18.0.101
      KAFKA_CREATE_TOPICS: "test:1:1"
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    depends_on:
      - zookeeper
    networks:
      my-network:
        ipv4_address: 172.18.0.101

networks: 
  my-network:
    name: ecommerce-network

[start]
docker-compose -f docker-compose-single-broker.yml up -d

[stop]
docker-compose -f docker-compose-single-broker.yml down -d
```

## db connection in docker
* 도커에서는 docker-compose.yml에서 db 설정을 해주어야
* db가 정상적으로 커넥션된다.
* 모든 작업이 끝나고 도커로 빌드하면서 해볼작업이고,
* 향후 프로젝트에는 굳이 도커로 빌드하진 않겠다.

## DB 설계 및 핸들링
* 일단 기본적으로 msa를 사용하는 목적에 맞게 각 마이크로 서비스 별로 DB를 구축함.
* 다만 조인이 필요한 DB들이 반드시 존재하고, 많은 양의 데이터들이 오가는 조인이 필요한 경우라면 api조회를 사용하면 성능이 급격히 떨어진다.
* 이 경우 두가지를 고민해 볼 수 있다.
* 복제 DB를 놓거나 read-only db를 구축하는 것이다.
* 복제 DB나 read-only DB는 조금 sync가 늦어지더라도 큰 문제가 없기 때문에 걱정할 일이 적다.