#!groovy

def mavenVersion = 'maven-3.8.4'
def javaVersion = 'jdk11'
def artefactName = 'shared-utils'

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


         stage('Build') {
            echo "Build  ${pom.artifactId}-${pom-version}..."
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
            echo "Test  ${pom.artifactId}-${pom-version}..."
            withMaven(jdk: javaVersion, maven: mavenVersion) {
                try{
                    runCommand('mvn test')
                } catch(exception){
                    sendErrorMail("Error occurred while testing, error: " + exception.message)
                    warnError(exception.message)
                }
            }
        }
/*
        stage('Docker_build') {
            echo "Docker build ${pom.artifactId}-${pom-version} image..."
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
            echo "Docker push ${pom.artifactId}-${pom-version} image..."
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
