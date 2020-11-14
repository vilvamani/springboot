node('jenkins-slave') {

    def git_commit = ""
    def author_email = ""
    def customImage = ""
    def docker_image_name = "vilvamani007/sprintboot"
    def service = "spring-boot"

    def mvnHome = tool 'M3'
    env.PATH = "${mvnHome}/bin:${env.PATH}"
    
    try {
        stage("CleanUp WorkSpace") {
            cleanWs()
        }

        dir(service) {
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
            
            stage("Publish Report"){
                sh 'find . -type f -name "*.exec"'
                
                junit(
                    allowEmptyResults: true,
                    testResults: '**/target/surefire-reports/*.xml,' +
                            '**/target/failsafe-reports/*.xml'
                )
                jacoco()
            }

            stage("Maven Build") {
                sh "mvn install -DskipTests"
            }

            stage("SonarQube") {
                withSonarQubeEnv('SonarQube') {
                    sh "mvn verify sonar:sonar"
                }
            }


            dir("infra"){
                stage("Build Docker Image") {
                    sh "curl -O https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip"
                    sh "jar -xvf newrelic-java.zip"
                    sh "cp newrelic/newrelic.jar ./newrelic.jar"
                    sh "rm -rf newrelic newrelic-java.zip"
                    customImage = docker.build(docker_image_name)
                }
            }
            
            stage("Docker Push & CleanUp") {
                // This step should not normally be used in your script. Consult the inline help for details.
                withDockerRegistry(credentialsId: 'docker_hub', url: 'https://index.docker.io/v1/') {
                    customImage.push("${env.BUILD_NUMBER}")
                    customImage.push("${git_commit}")
                    customImage.push("latest")
                }

                // Remove dangling Docker images
                //sh "docker image prune --all --force"
            }
            //currentBuild.result = "FAILURE"
            
            stage("Push to artifactory") {
                configFileProvider([configFile(fileId: '8b36a983-2cd4-4843-956f-f2f5f72efff4', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS clean deploy"
                }
            }
        }
    } catch (Exception e) {
        currentBuild.result = "FAILURE"
    }
}