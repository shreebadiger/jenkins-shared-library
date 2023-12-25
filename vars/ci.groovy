def call () {

pipeline {
    agent {
        node { label'workstation'}
    }
    stages{
        stage('compiling') {
            steps{
              echo 'compiling'
          }
        }

         stage('Test'){
            steps{
              echo 'Test'
          }
         }

         stage('Build'){
            steps{
              echo 'Build'
          }
         }

         stage('Release App'){
            steps{
              echo 'Release'
          }
         }
    }      
}
}