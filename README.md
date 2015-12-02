# 통합재난경보발령시스템 #

**통합경보시스템**(KIEAS: Korea Integrated Emergency Alert System)은 국내 다양한(이기종) 경보시스템을 통합 운용하기 위한 시스템이다. **[한국재난정보미디어포럼](http://www.kpew.or.kr/)**에서는 ETRI를 주관기관으로 하여 국민안전처의 "지능/맞춤형 통합경보시스템 연구 개발" 과제를 공동 연구하고 있다. 이 사이트에서는 통합경보시스템 개발에 참조하기 위한 **참조 소프트웨어**를 배포한다.

본 연구결과는 국민안전처의 연구과제에 따른 것이므로, 모든 소유권은 대한민국 정부에 있다.


## 1. 배포문서 ##

#### [ 2015 공통경보프로토콜 기술설명회 ] ####
다음은 [기술설명회 자료](https://drive.google.com/folderview?id=0B_zNG8ZKU_9-M3I2YWlSMk1kbUk&usp=sharing)이다.

 * 재난의 정의와 재난관리: 재난, 재난관리에 관한 개요
 * 재난경보 전달 체계: 재난경보시스템의 개요와 국내외 현황
 * CAP 엘리먼트 소개: 공통경보프로토콜(Common Alerting Protocol) 개요 및 메시지 구조
 * 통합경보시스템 참조 코드 사용 환경 구성: 참조 SW 사용 방법 소개
 * 설치 가이드라인(향후)

#### [ CAP/EDXL 표준 (외부 링크) ] ####

* [CAP v1.2 OASIS 표준문서](http://docs.oasis-open.org/emergency/cap/v1.2/CAP-v1.2-os.pdf)
* [CAP Feeds v1.0 OASIS 표준문서](http://docs.oasis-open.org/emergency-adopt/cap-feeds/v1.0/cap-feeds-v1.0.pdf)
* [CAP Elements v1.0 OASIS 표준문서](http://docs.oasis-open.org/emergency-adopt/cap-elements/v1.0/cn01/cap-elements-v1.0-cn01.pdf)
* [EDXL-DE v2.0 OASIS 표준문서](http://docs.oasis-open.org/emergency/edxl-de/v2.0/cs02/edxl-de-v2.0-cs02.pdf)
* [EDXL-CT v1.0 OASIS 표준문서](http://docs.oasis-open.org/emergency/edxl-ct/v1.0/csd03/edxl-ct-v1.0-csd03.pdf)
* [EDXL-SitRep v1.0 OASIS 표준문서](http://docs.oasis-open.org/emergency/edxl-sitrep/v1.0/cs01/edxl-sitrep-v1.0-cs01.pdf)

#### [ 국내 표준 문서 (외부 링크, TTA 로그인 필요) ] ####

* ["통합경보시스템을 위한 공통경보프로토콜 프로파일" TTAK.OT-06.0055/R1](http://www.tta.or.kr/data/ttas_view.jsp?rn=1&rn1=Y&rn2=&rn3=&nowpage=1&pk_num=TTAK.OT-06.0055%2FR1&standard_no=OT-06.0055&kor_standard=&publish_date=&section_code=&order=publish_date&by=desc&nowSu=1&totalSu=2&acode1=&acode2=&scode1=&scode2=)
* ["지상파 디지털 멀티미디어 방송(DMB) 재난 경보 서비스" TTAK.KO-07.0046/R5](http://www.tta.or.kr/data/ttas_view.jsp?rn=1&rn1=Y&rn2=&rn3=&nowpage=1&pk_num=TTAK.KO-07.0046%2FR5&standard_no=07.0046&kor_standard=&publish_date=&section_code=&order=publish_date&by=desc&nowSu=1&totalSu=6&acode1=&acode2=&scode1=&scode2=)
* ["이기종 경보 시스템 서버와 통합 재난 경보 게이트웨이 연계 프로토콜" TTAK.KO-09.0085/R1](http://www.tta.or.kr/data/ttas_view.jsp?rn=1&rn1=Y&rn2=&rn3=&nowpage=1&pk_num=TTAK.KO-09.0085%2FR1&standard_no=&kor_standard=%C0%CC%B1%E2%C1%BE+%B0%E6%BA%B8&publish_date=&section_code=&order=publish_date&by=desc&nowSu=1&totalSu=2&acode1=&acode2=&scode1=&scode2=)
* ["옥내경보방송시스템" TTAK.KO-06.0363](http://www.tta.or.kr/data/ttas_view.jsp?rn=1&rn1=Y&rn2=&rn3=&nowpage=1&pk_num=TTAK.KO-06.0363&standard_no=&kor_standard=%BF%C1%B3%BB%B0%E6%BA%B8&publish_date=&section_code=&order=publish_date&by=desc&nowSu=1&totalSu=1&acode1=&acode2=&scode1=&scode2=)
* ["경보 표출 방법" TTAK.KO-06.0364/R1](http://www.tta.or.kr/data/ttas_view.jsp?rn=1&rn1=Y&rn2=&rn3=&nowpage=1&pk_num=TTAK.KO-06.0364%2FR1&standard_no=&kor_standard=%B0%E6%BA%B8+%C7%A5%C3%E2&publish_date=&section_code=&order=publish_date&by=desc&nowSu=1&totalSu=2&acode1=&acode2=&scode1=&scode2=)
* ["LTE 망에서 재난문자 서비스 제공을 위한 요구사항 및 메시지 형식" KCS.KO-06.0263/R1](http://www.tta.or.kr/data/ttas_view.jsp?rn=1&rn1=Y&rn2=&rn3=&nowpage=1&pk_num=TTAK.KO-06.0263%2FR1&standard_no=&kor_standard=lte+%B8%C1&publish_date=&section_code=&order=publish_date&by=desc&nowSu=1&totalSu=2&acode1=&acode2=&scode1=&scode2=)