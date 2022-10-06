
## 🖥 Preiview
![earthUs](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlyIQp%2FbtrNZoERCaJ%2Fxwv1DFpGK3kI4CisP09dLk%2Fimg.png)


## 🔎 group info
  * BE : 강윤주 / 전혜진 / 안재원
  * FE : 심유선 / 김민석 / 박세은A / 오정진 [Front-End GitHub](https://github.com/Team-1-Final-Project/FE-Final-Project) (링크)


## 💡 핵심기능

>제로웨이스트 실천을 위한 데일리미션이 제공되며 유저 수행 인증이 가능합니다.

>커뮤니티 메뉴(모임 및 게시판)를 통하여 유저간 소통과 만남이 가능합니다. 

>뱃지 시스템을 통해 일정 목표를 달성하여 유저에게 성취감을 제공합니다.

>추천샵 정보를 온라인샵 링크와 오프라인샵 위치정보(지도)를 제공하여 제로웨이스트 관련 용품 사용을 독려합니다.

>모임 후기 작성 기능으로 해당 모임 참여 유저를 통한 미참여 유저들의 궁금증을 해소할 수 있습니다. 

>일부 페이지 권한을 전체 개방하여 미가입 유저에게 일부 서비스 이용을 허용하여 미리보기를 제공합니다.


## 🛠 Service Architecture
![architecture](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FZzmin%2FbtrNZGySzkd%2FPkE0YIk8PKu8sZpJZdF4Ok%2Fimg.png)



## 📁 Project Structure
![structure](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FmUMmk%2FbtrNXYHrLY8%2FALfyidilVqXbnVrDePtXLK%2Fimg.png)

 * 가시성과 편의성을 고도하기 위해 폴더 구조를 기능별로 분류 후, 각 기능별마다 스프링 기본 구조로 재분류하였습니다.

![meetingFolder](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FvXiG9%2FbtrNXVKSiWU%2FM9zL0Oi3KG6BHyIepgBB71%2Fimg.png)
 * 상세폴더 구조는 예시로 대표기능의 폴더 구조를 첨부하였습니다.



### [🔗 API 설계](https://www.notion.so/7fa2f4edf3da4a1cba1a25c80a2833a4?v=a29bf04c9e6e469ca21cdf7f228a2079) (링크연결)

## 🗂 ERD
![ERD](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FclCCne%2FbtrNYt72JfM%2FkO1XYUVcjIjei63k4XoQTK%2Fimg.png)



## 🚀 Trouble Shooting
> ### Selenium
 * 도입이유
   + 사용자에게 오프라인 매장 추천 리스트를 보여주는데 직접 db에 데이터를 넣기보다는 크롤링 하기로 함

 * 문제상황
   + Jsoup 라이브러리로 읽어오려 했으나 javascript로 이루어진 동적 데이터라 읽어올 수 없음

 * 해결방안
   + 자동화 테스트 프레임워크인 Selenium을 활용해서 크롤링을 해서 해결함


> ### 카카오 로그인 : 동의내역을 확인하는 API 요청

* 도입이유
  + 사용자가 로그인을 할 때 동의한 내역을 받기 위해서 api를 요청함

* 문제상황
  + 로그인 시 프로필 이미지 수집을 동의하지 않으면 프로필 이미지가 없어 에러가 발생함

* 해결방안
  + 동의 내역을 받아와서 동의하지 않은 사용자일 경우 s3에 업로드된 이미지를 기본 프로필 이미지로 설정되도록 변경함




## 📚 Technique Stacks
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white) <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"> ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white) <img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> ![Microsoft Excel](https://img.shields.io/badge/Microsoft_Excel-217346?style=for-the-badge&logo=microsoft-excel&logoColor=white) <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"> <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"> <img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"> <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"> <img src="https://img.shields.io/badge/Ubuntu-D4911E?style=for-the-badge&logo=Ubuntu&logoColor=white"> <img src="https://img.shields.io/badge/intelliJ IDEA-7A1FA2?style=for-the-badge&logo=intelliJIDEA&logoColor=white">



