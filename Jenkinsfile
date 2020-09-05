node {
    def git_commit = ""
    def author_email = ""
    def customImage = ""
    def docker_image_name = "vilvamani007/sprintboot"

    def mvnHome = tool 'M3'
    env.PATH = "${mvnHome}/bin:${env.PATH}"

    properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '7', numToKeepStr: '10')), disableConcurrentBuilds(), [$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false]])

    try {
        stage("CleanUp WorkSpace") {
            cleanWs()
        }

        stage("Git Checkout") {
            git url: 'https://github.com/vilvamani/springboot.git'
        }

        stage("Read Author") {
            git_commit = sh label: 'get last commit',
                returnStdout: true,
                script: 'git rev-parse --short HEAD~0'
            author_email = sh label: 'get last commit',
                returnStdout: true,
                script: 'git log -1 --pretty=format:"%ae"'
        }

        stage("UnitTest") {
            sh 'mvn clean test -U'
        }

        stage("Maven Build") {
            sh "mvn install -DskipTests"
        }

        stage("SonarQube") {
            withSonarQubeEnv('SonarQube') {
                sh "mvn verify sonar:sonar"
            }
        }

        stage("Snyk Vulnerability Scan") {
            snykSecurity(organisation: '20be9dd9-b33c-4860-96f1-072eecf66b40', failOnIssues: false, projectName: 'spring-boot', snykInstallation: 'Snyk', snykTokenId: 'snyktoken')
            //sh "mvn snyk:test"
            //sh "mvn snyk:monitor"
        }

        stage("OWASP Dependancy Check"){
            dependencyCheck additionalArguments: '', odcInstallation: 'owasp'
            dependencyCheckPublisher pattern: 'dependency-check-report.xml'
        }

        stage("Build Docker Image") {
            sh "curl -O https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip"
            sh "jar -xvf newrelic-java.zip"
            sh "cp newrelic/newrelic.jar ./newrelic.jar"
            sh "rm -rf newrelic newrelic-java.zip"
            customImage = docker.build(docker_image_name)
        }

        stage("Regression Test - Postman Collection") {
            /*docker.image("${docker_image_name}:latest").withRun("-p 8081:8080") {
                sh 'sleep 90'
                sh 'newman run springboot.postman_collection.json --reporters cli,junit --reporter-junit-export "newman/myreport.xml"'

                junit 'newman/myreport.xml'
            }*/
        }

        stage("Docker Push & CleanUp") {
            // This step should not normally be used in your script. Consult the inline help for details.
            withDockerRegistry(credentialsId: 'docker_hub', url: 'https://index.docker.io/v1/') {
                customImage.push("${env.BUILD_NUMBER}")
                customImage.push("${git_commit}")
                customImage.push("latest")
            }

            // Remove dangling Docker images
            sh "docker image prune --all --force"
        }
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        currentBuild.displayName = "#" + (currentBuild.number + ' - ' + currentBuild.result)
        println "::: Catching the exception :::"
        println e
    } finally {
        //The finally block always executes. 
        if (currentBuild.result != 'FAILURE') {
            currentBuild.result = 'SUCCESS'
            currentBuild.displayName = "#" + (currentBuild.number + ' - ' + currentBuild.result)
        }
    }
}
