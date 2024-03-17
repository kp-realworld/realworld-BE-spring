# ecr login
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 427800856788.dkr.ecr.ap-northeast-2.amazonaws.com

# 이미지 빌드
docker build -t realworld-spring-api .

# 이미지 태깅
docker tag realworld-spring-api:latest 427800856788.dkr.ecr.ap-northeast-2.amazonaws.com/realworld-spring-api:latest

# 이미지 푸시
docker push 427800856788.dkr.ecr.ap-northeast-2.amazonaws.com/realworld-spring-api:latest