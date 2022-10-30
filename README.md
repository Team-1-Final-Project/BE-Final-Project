<div align="center"> 
<h1> Innovation Camp SEOUL [1조] </br> </br> 
<img src="https://user-images.githubusercontent.com/110370262/198892139-df6d5f2a-3f8b-4c58-8618-ef4398d1cebf.png" width="320">
</h1>
</div>

<br></br>

<div align="center"> 
<h3> 🔎 group info </h3> </br>
   ▪︎ Back-End : 강윤주 (Team Leader) / 전혜진 / 안재원 </br> </br>
   ▪︎ Front-End : 심유선 (Deputy Team Leader) / 김민석 / 박세은A / 오정진 </br> 
     <a href="https://github.com/Team-1-Final-Project/FE-Final-Project">Front-End GitHub 바로가기</a>
</div>

<br></br>
<br></br>


## 🖥 Preview
![영상썸네일_최종](https://user-images.githubusercontent.com/110370262/198887577-72c986b2-82ef-4e48-8105-12d2933ca192.png)

![earthUs](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fcd0e46%2FbtrN4wR9V5a%2FT0kXvqqHVq5zUKDURQCO6k%2Fimg.png)


<br></br>
<br></br>

## 📃 서비스 개요
<div align="center"> 
최근 우리는 날이 갈수록 심각해지는 기후변화와 가속화되는 환경오염을 겪고 있습니다. </br> </br>
이러한 이유로 환경 문제에 관심이 있고 제로웨이스트를  실천하고자 하는 분들이라면 누구나 </br>
환경 문제에 대한 노하우나 생각을  공유, 소통하며 자유롭게 모여 함께 실천할 수 있는 공간을 제공함으로서 </br>
일상 속에 자연스레 녹아드는 제로웨이스트를 추구하고자 기획하게 된 서비스입니다.
</div>

<br></br>
<br></br>
<br></br>

## 💡 핵심기능

>제로웨이스트 실천을 위한 데일리미션이 제공되며 유저 수행 인증이 가능합니다.

>커뮤니티 메뉴(모임 및 게시판)를 통하여 유저간 소통과 만남이 가능합니다. 

>뱃지 시스템을 통해 일정 목표를 달성하여 유저에게 성취감을 제공합니다.

>추천샵 정보를 온라인샵 링크와 오프라인샵 위치정보(지도)를 제공하여 제로웨이스트 관련 용품 사용을 독려합니다.

>모임 후기 작성 기능으로 해당 모임 참여 유저를 통한 미참여 유저들의 궁금증을 해소할 수 있습니다. 

>일부 페이지 권한을 전체 개방하여 미가입 유저에게 일부 서비스 이용을 허용하여 미리보기를 제공합니다.

<br></br>
<br></br>
<br></br>

## 🛠 Service Architecture
![서비스 아키텍쳐](https://user-images.githubusercontent.com/110370262/198887695-657fd21a-5d5b-4d09-b538-e2f5d90091a8.png)

<br></br>
<br></br>
<br></br>

## 📁 Project Structure
<br></br>
<div align="center">
<img src="https://user-images.githubusercontent.com/110370262/198891253-555bc7ef-9ee6-458d-9826-173732f3f43b.png">
</div>
<br></br>

 * 가시성과 편의성을 고도하기 위해 폴더 구조를 기능별로 분류 후, 각 기능별마다 스프링 기본 구조로 재분류하였습니다.
 * 상세폴더 구조는 예시로 대표기능의 폴더 구조를 첨부하였습니다.


<br></br>
<br></br>
<br></br>

<hr>
<br></br>
<div align="center"> 
<h3> <a href="https://www.notion.so/7fa2f4edf3da4a1cba1a25c80a2833a4?v=a29bf04c9e6e469ca21cdf7f228a2079">🔗 API 명세서 보기</a></h3>
</div>
<br></br>
<hr>

<br></br>
<br></br>
<br></br>

## 🗂 ERD
![ERD](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FclCCne%2FbtrNYt72JfM%2FkO1XYUVcjIjei63k4XoQTK%2Fimg.png)

<br></br>
<br></br>
<br></br>

## 🚀 Trouble Shooting
> ### Selenium
 * 도입이유
   + 사용자에게 오프라인 매장 추천 리스트를 보여주는데 직접 db에 데이터를 삽입하기보다는 크롤링으로 결정하였습니다.

 * 문제상황
   + Jsoup 라이브러리로 읽어오려 했으나 JavaScript로 이루어진 동적 데이터라 읽어들일 수 없었습니다. 

 * 해결방안
   + 자동화 테스트 프레임워크인 Selenium을 활용해서 크롤링을 해서 해결하였습니다.


> ### 카카오 로그인 : 동의내역을 확인하는 API 요청

* 도입이유
  + 사용자가 로그인을 할 때 동의한 내역을 받기 위해서 API를 요청합니다.

* 문제상황
  + 로그인 시 프로필 이미지 수집을 동의하지 않으면 프로필 이미지가 없어 에러가 발생하였습니다.

* 해결방안
  + 동의 내역을 받아와서 동의하지 않은 사용자일 경우 S3에 업로드하여 지정한 이미지를 기본 프로필 이미지로 설정되도록 수정하였습니다.

<br></br>
<br></br>
<br></br>

## 📚 Back-End Technique Stacks
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-52B0E7?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/OAuth2.0-02303A?style=for-the-badge&logo=OAuth2.0&logoColor=white">
<img src="https://img.shields.io/badge/JWT-A100FF?style=for-the-badge&logo=JWT&logoColor=white">
<img src="https://img.shields.io/badge/kakao developers-FFCD00?style=for-the-badge&logo=kakao developers&logoColor=white">
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=Selenium&logoColor=white">
<img src="https://img.shields.io/badge/SSE-181717?style=for-the-badge&logo=SSE&logoColor=white">
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
<img src="https://img.shields.io/badge/Amazon RDS-003791?style=for-the-badge&logo=Amazon RDS&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-DA1F26?style=for-the-badge&logo=Amazon S3&logoColor=white">
<img src="https://img.shields.io/badge/Ubuntu-D4911E?style=for-the-badge&logo=Ubuntu&logoColor=white">
<img src="https://img.shields.io/badge/HTTPS-00A98F?style=for-the-badge&logo=HTTPS&logoColor=white">
<img src="https://img.shields.io/badge/Code Deploy-43B02A?style=for-the-badge&logo=Code Deploy&logoColor=white">
<img src="https://img.shields.io/badge/intelliJ IDEA-7A1FA2?style=for-the-badge&logo=intelliJIDEA&logoColor=white">

