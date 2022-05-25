#!groovy

def mavenVersion = 'maven-3.8.4'
def javaVersion = 'jdk11'
def artefactName = 'shared-utils'
def artifactory_server = 'jfrog-artifactory'
def releaseRepo = 'it2go-releases'
def snapshotRepo = 'it2go-snapshots'
def depsResolverRepo = 'default-maven-virtual'

def sendSuccessMail(){
    mail to: "mbarek@it-2go.de", bcc: "", cc: "", from: "Jenkins@it-2go.de", replyTo: "",
    subject: "Build  ${env.JOB_NAME} done",
    body: "Your Build  ${env.JOB_NAME} #${env.BUILD_NUMBER} is successfully done."
}

def sendErrorMail(error){
    mail to: "mbarek@it-2go.de", bcc: "", cc: "", from: "Jenkins@it-2go.de", replyTo: "",
    subject: "Build  ${env.JOB_NAME} fails",
    body: """
    Your Build  ${env.JOB_NAME} #${env.BUILD_NUMBER} fails.
    ${error}
    For details check the Job console: ${env.BUILD_URL}console"""
}

def runCommand(command){
    if(isUnix()){
        sh command
    } else {
        bat command
    }
}
def pom
node {

    ansiColor('xterm') {
         stage('Checkout') {
            echo "Checkout ${artefactName}..."
            checkout scm
            pom = readMavenPom()
         }

        /*
        stage ('Artifactory configuration') {
            // Obtain an Artifactory server instance, defined in Jenkins --> Manage Jenkins --> Configure System:
            server = Artifactory.server artifactory_server

            rtMaven = Artifactory.newMavenBuild()
            rtMaven.tool = mavenVersion
            rtMaven.deployer releaseRepo: releaseRepo, snapshotRepo: snapshotRepo, server: server
            rtMaven.resolver releaseRepo: depsResolverRepo, snapshotRepo: depsResolverRepo, server: server
            rtMaven.deployer.deployArtifacts = false // Disable artifacts deployment during Maven run

            buildInfo = Artifactory.newBuildInfo()
        }*/

         stage('Build') {
            echo "Build  ${pom.artifactId}-${pom.version}..."
            withMaven(jdk: javaVersion, maven: mavenVersion) {
                try{
                    runCommand('mvn clean package -DskipTests')
                } catch(exception){
                    sendErrorMail("Error occurred while building, error: " + exception.message)
                    warnError(exception.message)
                }
            }
         }

        stage('Test') {
            echo "Test  ${pom.artifactId}-${pom.version}..."
            withMaven(jdk: javaVersion, maven: mavenVersion) {
                try{
                    runCommand('mvn test')
                } catch(exception){
                    sendErrorMail("Error occurred while testing, error: " + exception.message)
                    warnError(exception.message)
                }
            }
        }

        if(BRANCH_NAME.contains("release/")) {
            // Obtain an Artifactory server instance, defined in Jenkins --> Manage Jenkins --> Configure System:
            server = Artifactory.server artifactory_server

            rtMaven = Artifactory.newMavenBuild()
            rtMaven.tool = mavenVersion
            rtMaven.deployer releaseRepo: releaseRepo, snapshotRepo: snapshotRepo, server: server
            rtMaven.resolver releaseRepo: depsResolverRepo, snapshotRepo: depsResolverRepo, server: server
            rtMaven.deployer.deployArtifacts = false // Disable artifacts deployment during Maven run

            buildInfo = Artifactory.newBuildInfo()
            // install to .m2 wird glaube ich nicht gebraucht
            /*
            stage ('Install') {
                rtMaven.run pom: 'pom.xml', goals: 'install', buildInfo: buildInfo
            }*/

            stage ('Deploy') {
                rtMaven.deployer.deployArtifacts buildInfo
            }

            stage ('Publish build info') {
                server.publishBuildInfo buildInfo
            }
        }
/*
        stage('Docker_build') {
            echo "Docker build ${pom.artifactId}-${pom.version} image..."
            withMaven(jdk: javaVersion, maven: mavenVersion) {
                try{
                    runCommand('mvn docker:build')
                } catch(exception){
                    sendErrorMail("Error occurred while building docker image, error: " + exception.message)
                    warnError(exception.message)
                }
            }
        }

        stage('Docker_push') {
            echo "Docker push ${pom.artifactId}-${pom.version} image..."
            withMaven(jdk: javaVersion, maven: mavenVersion) {
                try{
                    runCommand('mvn docker:push')
                } catch(exception){
                    sendErrorMail("Error occurred while pushing docker image, error: " + exception.message)
                    warnError(exception.message)
                }
            }
        }
*/
        stage('Notify'){
            echo "Notify contributors ..."
            sendSuccessMail()
        }

        stage('Cleanup') {
            // Delete workspace when build is done
            cleanWs()
        }
    }
}
