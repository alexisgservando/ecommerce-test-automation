pipeline {
  agent any

  tools {
    jdk   'JDK21'
    maven 'Maven 3.9'
  }

  options {
    timestamps()
    ansiColor('xterm')
  }

  environment {
    MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    // If you set this globally you can remove it here:
    // CHROME_BIN = 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe'
    // Optional: cache WebDriverManager downloads to speed up builds
    // WDM_CACHE_PATH = 'C:\\jenkins\\cache\\wdm'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      options { timeout(time: 10, unit: 'MINUTES') }
      steps {
        bat 'mvn -B -U -DskipTests compile'
      }
    }

    stage('Test') {
      options { timeout(time: 20, unit: 'MINUTES') }
      steps {
        bat 'mvn -B -U clean test'
      }
    }

    stage('Report') {
      steps {
        // JUnit-style Surefire XMLs
        junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'

        // Native TestNG results (matches your plugin’s snippet)
        testNG reportFilenamePattern: '**/test-output/testng-results.xml, **/surefire-reports/testng-results.xml'

        // Keep useful outputs
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
