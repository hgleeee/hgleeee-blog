# hgleeee-blog

## ERD
- https://www.erdcloud.com/d/SQTMLg5nY7YvDRer9
- 위의 링크에서 확인 가능하다.

## 개발 양식
### git branch
- 개인 프로젝트인 관계로 git-flow는 사용하지 않는다.
- 대신 Main, Develop, Feature 3 가지의 branch를 사용한다.
    - Main : 배포 가능한 상태만 담는다.
    - Develop : 다음 버전 출시를 위해 사용하는 개발용 브랜치, 배포 가능한 상태가 되었을 때 Main과 병합
    - Feature : 새로운 기능 개발 or 버그 수정 등의 상황에서 develop branch에서 분기하고 개발이 완료되면 develop branch와 병합

### git branch naming
- 이슈 단위의 개발을 진행한다.
- Main과 Develop은 그대로의 이름을 사용하고, Feature는 feature/{#issue번호}-{name}으로 branch 이름을 짓는다.


### git commit
- 이슈를 끝내는 커밋에는 close {#issue번호}로 마무리 짓는다.<br>깃허브에서 이슈를 닫을 필요없이 자동으로 닫힌다.
- 다음과 같은 commit type을 commit 제목에 붙이도록 한다.

```yaml
feat: 기능 추가
fix: 버그 수정
docs: 문서 수정
refactor: 코드 리팩토링
test: 누락된 테스트 코드 추가
```