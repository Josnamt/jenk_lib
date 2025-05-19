def buildAndTest() {
    echo "Running Maven build and tests..."
    sh 'mvn clean install'
}

def publishTestResults() {
    echo "Publishing JUnit test results..."
    junit '**/target/surefire-reports/*.xml'
}

def publishArtifacts() {
    echo "Publishing artifacts to Artifactory..."

    def server = Artifactory.server(env.ARTIFACTORY_SERVER)
    def uploadSpec = """{
        "files": [{
            "pattern": "target/*.jar",
            "target": "${env.ARTIFACTORY_REPO}/"
        }]
    }"""

    server.upload spec: uploadSpec
}
return this
