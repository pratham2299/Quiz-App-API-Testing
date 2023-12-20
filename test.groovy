pipeline {
    agent any

    stages {
        stage('Get Source Code') {
            steps {
                git branch: 'restAssured', credentialsId: 'credential-test', url: 'https://gitlab.com/prathamesh2435347/quiz-app-api-testing.git'
            }
        }
        stage('Build Code') {
            steps {
                bat script: 'mvn compile'
            }
        }
        stage('Run Test') {
            steps {
                bat script: 'mvn test'
            }
        }
        stage('Publish Report') {
            steps {
                publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: '', reportFiles: 'target/surefire-reports/index.html', reportName: 'Pipeline Report', reportTitles: '', useWrapperFileDirectly: true])
            }
        }
    }
}
