def apiImage
pipeline {
    agent any
    environment {
        HOME = '.'
    }
    stages {
        stage("Build Docker Image") {
            steps {
                echo "Build Docker Image"
                script {
                    apiImage = docker.build("${params.DOCKER_IMAGE}:${params.TARGET_ENV}")
                }
            }
        }
        stage("Push to Docker Hub") {
            steps {
                echo "Push to Docker Hub"
                script {
                    docker.withRegistry("https://cd.lifestoreshealthcare.com:5000") {
                        apiImage.push()
                    }
                }
            }
        }
    }
}