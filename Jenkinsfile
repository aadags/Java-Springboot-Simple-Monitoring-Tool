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
                    docker.withRegistry("https://eu.gcr.io/apt-quarter-347815") {
                        apiImage.push()
                    }
                }
            }
        }
    }
}