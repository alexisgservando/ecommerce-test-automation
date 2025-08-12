pipeline {
  agent any

  tools {
    jdk 'JDK21'
    maven 'Maven 3.9'
  }

  options {
    timestamps()
    ansiColor('xterm')
  }

  environment {
    MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    // optional: cache WebDriverManager downloads to speed up builds
    // WDM_CACHE_PATH = 'C:\\jenkins\\cache\\wdm'
    // CHROME_BIN is already set globally; if not, uncomment next line
    // CHROME_BIN = 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build') {
      steps { bat 'mvn -B -U -DskipTests compile' }
    }

    stage('Test') {
      steps { bat 'mvn -B -U clean test' }
    }

    stage('Report') {
      steps {
        // Surefire XMLs (works for TestNG when using maven-surefire-plugin)
        junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'

        // Native TestNG report if your build produces testng-results.xml
        publishTestNG testResultsPattern: '**/test-output/testng-results.xml, **/surefire-reports/testng-results.xml',
                      escapeTestDescp: true, escapeExceptionMsg: true

        archiveArtifacts artifacts: 'target/surefire-reports/**, test-output/**, screenshots/**',
                         allowEmptyArchive: true
      }
    }
  }

  post {
    success { echo 'Build succeeded.' }
    failure { echo 'Build failed. Check console, JUnit, and TestNG reports.' }
    always  { echo 'Pipeline finished.' }
  }
}
