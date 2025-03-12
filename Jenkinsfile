pipeline {
    agent any

    environment {
        SERVICE_NAME = 'craneWebBackend_v2'
        IMAGE_TAG = "cranebackend_v2:latest"
        LOCAL_PORT = "8900"
        // ✅ 환경 변수 설정
        JWT_SECRET = credentials('JWT_SECRET')
        REDIS_HOST = credentials('REDIS_HOST')
        REDIS_PORT = credentials('REDIS_PORT')
        MYSQL_URL_V2 = credentials('MYSQL_URL_V2')
        MYSQL_USER = credentials('MYSQL_USER')
        MYSQL_PASSWORD = credentials('MYSQL_PASSWORD')

        TZ = "Asia/Seoul"
        SLACK_CHANNEL = "build-deploy"
        SLACK_SUCCESS_COLOR = "#2C953C"
        SLACK_FAIL_COLOR = "#FF3232"
        SLACK_MESSAGE_UNIT = "=================================================="
        SLACK_DURATION_TIME_MESSAGE = ""
        SLACK_MESSAGE_BUILDER = ""
    }

    stages {
        stage('Start'){
            steps{
                script{
                    SLACK_MESSAGE_BUILDER =
                        "${SLACK_MESSAGE_UNIT}" + "\n" +
                        "@here \n" +
                        ":pencil: `${env.JOB_NAME}` 배포 파이프라인 실행 결과 리포트입니다. (<${env.BUILD_URL}|${currentBuild.displayName}>) \n"
                    slackSend (
                        channel: SLACK_CHANNEL,
                        color: SLACK_SUCCESS_COLOR,
                        message: ":information_source: @here `${env.JOB_NAME}` 배포 파이프라인이 시작되었습니다. (<${env.BUILD_URL}|${currentBuild.displayName}>)"
                    )
                }
            }
        }

        stage('Check Environment Variables') {
            steps {
                script {
                    echo "✅ Jenkins 환경 변수 확인..."
                    echo "🔍 REDIS_HOST: $REDIS_HOST"
                    echo "🔍 REDIS_PORT: $REDIS_PORT"
                }
            }
        }

        stage('Check Changes') {
            steps {
                script {
                    def changes = sh(script: "git diff --name-only HEAD^", returnStdout: true).trim()

                    if (!changes) {
                        currentBuild.result = 'NOT_BUILT'
                        error('No changes in CraneWebBackend_v2 directory, skipping build')
                    }
                    echo "✅ CraneWebBackend_v2 변경 사항 감지됨. 빌드를 진행합니다."
                }
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/CraneWebProject/Crane_web_backend_v2'

                script {
                    def gitCommit = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    echo "✅ 현재 빌드하는 커밋: ${gitCommit}"
                }
            }
            post {
                failure {
                    slackSend (
                        channel: SLACK_CHANNEL,
                        color: SLACK_FAIL_COLOR,
                        message: "${SLACK_MESSAGE_BUILDER}" + stageFailSlackMessage("Git Checkout") + "${SLACK_MESSAGE_UNIT}"
                    )
                }
            }
        }

        stage('Build') {
            steps {
                script{
                    long startTime = new Date().getTime()
                    sh '''
                        chmod +x gradlew
                        ./gradlew clean build -x test
                    '''
                    long endTime = new Date().getTime()
                    SLACK_DURATION_TIME_MESSAGE = getStageDurationMessage(startTime, endTime)
                    SLACK_MESSAGE_BUILDER += ":white_check_mark: Build 성공! (${SLACK_DURATION_TIME_MESSAGE}) \n"
                }

            }
            post {
                success {
                    echo "✅ Build 단계 완료"
                }
                failure {
                    error "❌ Build 단계 실패"
                    slackSend (
                        channel: SLACK_CHANNEL,
                        color: SLACK_FAIL_COLOR,
                        message: "${SLACK_MESSAGE_BUILDER}" + stageFailSlackMessage("Build") + "${SLACK_MESSAGE_UNIT}"
                    )
                }
            }
        }

        stage('Docker Build & Run') {
            steps {
                script {
                    long startTime = new Date().getTime()

                    sh '''
                    echo "🔍 현재 REDIS_HOST 값: $REDIS_HOST"
                    echo "🔍 현재 REDIS_PORT 값: $REDIS_PORT"

                    docker build -t $IMAGE_TAG \
                        --build-arg TZ=$TZ \
                        --build-arg JWT_SECRET=$JWT_SECRET \
                        --build-arg REDIS_HOST=$REDIS_HOST \
                        --build-arg REDIS_PORT=$REDIS_PORT \
                        --build-arg MYSQL_URL_V2=$MYSQL_URL_V2 \
                        --build-arg MYSQL_USER=$MYSQL_USER \
                        --build-arg MYSQL_PASSWORD=$MYSQL_PASSWORD \
                        user-service/

                    # 기존 컨테이너 종료 후 삭제
                    docker stop $SERVICE_NAME || true
                    docker rm $SERVICE_NAME || true

                    # 새 컨테이너 실행
                    docker run -d --name $SERVICE_NAME \
                        -e TZ=$TZ \
                        -e JWT_SECRET=$JWT_SECRET \
                        -e REDIS_HOST=$REDIS_HOST \
                        -e REDIS_PORT=$REDIS_PORT \
                        -e MYSQL_URL_V2=$MYSQL_URL_V2 \
                        -e MYSQL_USER=$MYSQL_USER \
                        -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
                        -p $LOCAL_PORT:8900 $IMAGE_TAG
                    '''

                    long endTime = new Date().getTime()
                    SLACK_DURATION_TIME_MESSAGE = getStageDurationMessage(startTime, endTime)
                    SLACK_MESSAGE_BUILDER += ":white_check_mark: Deploy 성공 (${SLACK_DURATION_TIME_MESSAGE}) \n"
                    SLACK_MESSAGE_BUILDER += ":tada: `${env.JOB_NAME}` 배포 파이프라인이 성공적으로 완료되었습니다. :beer:\n" + "${env.SLACK_MESSAGE_UNIT}"
                }
            }
        }
    }

    post {
        success {

            echo """
            ===========================================
            ✅ Pipeline Successfully Completed
            Service: ${SERVICE_NAME}
            Image: ${IMAGE_TAG}
            Port: ${LOCAL_PORT}
            ===========================================
            """

            slackSend (
                channel: SLACK_CHANNEL,
                color: SLACK_SUCCESS_COLOR,
                message: SLACK_MESSAGE_BUILDER
            )
        }
        failure {
            echo """
            ===========================================
            ❌ Pipeline Failed
            Service: ${SERVICE_NAME}
            Stage: ${currentBuild.result}
            ===========================================
            """

             slackSend (
                channel: SLACK_CHANNEL,
                color: SLACK_FAIL_COLOR,
                message: "${SLACK_MESSAGE_BUILDER}" + stageFailSlackMessage("Deploy") + "${SLACK_MESSAGE_UNIT}"
            )
        }
        always {
            cleanWs()
        }
    }
}



// Stage 경과 시간을 계산하여 메시지를 생성하는 함수
def getStageDurationMessage(long startTime, long endTime) {
    long durationMillis = endTime - startTime

    long durationSeconds = (long) (durationMillis / 1000) % 60
    long durationMinutes = (long) (durationMillis / (1000 * 60)) % 60

    def durationMessage = ""
    if (durationMinutes > 0) {
        durationMessage += "${durationMinutes}분 "
    }
    durationMessage += "${durationSeconds}초"

    return durationMessage
}


// 슬랙 실패 메세지를 생성하는 함수
def stageFailSlackMessage(stageName) {
    return ":alert: ${stageName} 단계에서 배포가 실패하였습니다. \n"
}