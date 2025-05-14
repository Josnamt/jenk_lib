def initializePipeline() {
    if (!params.NEW_RUN) {
        currentBuild.result = 'ABORTED'
        error("Pipeline aborted: NEW_RUN is false")
    }
    echo "Running environment: ${params.RUN}"
}

def buildApp() {
    echo "Building with Gradle"
    sh './gradlew clean build -x test'
}

def runTests() {
    if (params.RUN != 'prod') {
        echo "Running tests for ${params.RUN}"
        sh './gradlew test'
    } else {
        echo "Skipping tests in production"
    }
}

def deployApp() {
    echo "Deploying ${env.APP_NAME} to ${env.DEPLOY_DIR}"
    def jarFile = sh(
        script: "ls build/libs/*.jar | head -n 1",
        returnStdout: true
    ).trim()

    echo "Found artifact: ${jarFile}"

    sh """
        if [ -f "${jarFile}" ]; then
            cp "${jarFile}" "${env.DEPLOY_DIR}/"
            echo "Deployment completed"
        else
            echo "File not found: ${jarFile}"
            exit 1
        fi
    """
}