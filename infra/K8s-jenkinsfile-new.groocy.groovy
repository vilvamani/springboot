#!groovy

properties([
    disableConcurrentBuilds(),
    buildDiscarder(logRotator(daysToKeepStr: '7', numToKeepStr: '10'))
])

jenkins_common_branch = "develop"
jenkins_common_repo_url = "https://github.com/vilvamani/jenkins_common_library.git"
jenkins_common_checkout_dir = "jenkins_library"
jenkins_common_file = "jenkins_common_library.groovy"

colorBlue = '#0000FF'
colorGreen = '#00FF00'
colorRed = '#FF0000'

params = [
    branch_checkout_dir: 'service',
    branch: 'master',
    repo_url: 'https://github.com/vilvamani/springboot.git',
    skip_unit_test: false,
    skip_integration_test: true,
    skip_sonar: false,
    skip_artifactory: false,
    skip_docker_push: false,
    skip_kubernetes_deployment: false,
    dockerRepoName: 'vilvamani007',
    dockerImageName: 'sprintboot',
    kubeDeploymentFile: './infra/k8s-deployment.yaml',
    kubeServiceFile: './infra/k8s-service.yaml'
]

node('jenkins-slave') {
    //def mvnHome = tool 'M3'
    //env.PATH = "${mvnHome}/bin:${env.PATH}"

    step([$class: 'WsCleanup'])
    jenkinsLibrary = loadJenkinsCommonLibrary()
    jenkinsLibrary.defaultConfigs(params)
    timestamps {
        try {
            jenkinsLibrary.mavenSpingBootBuild(params)
        } catch (Exception err) {
            currentBuild.result = 'FAILURE'

            jenkinsLibrary.sendToSlack(colorRed, "FAILURE", 'sprintboot', 'infra-development', params.branch)

            throw err
        } finally {
            jenkinsLibrary.sendToSlack(colorBlue, "SUCCESS", 'sprintboot', 'infra-development', params.branch)
        }
    }
}

def loadJenkinsCommonLibrary() {
    dir(jenkins_common_checkout_dir) {
        git(url: jenkins_common_repo_url, branch: jenkins_common_branch)
            def jenkinsLibrary = load "${jenkins_common_file}"
            return jenkinsLibrary
    }
}
