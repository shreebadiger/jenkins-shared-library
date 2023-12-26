def call () {
    node ('workstation') {
        sh 'env'
        stage('Code checkout'){}
        stage('Compile'){}
        stage('Test case'){}
        stage('Integration test'){}
        stage('Build'){}
        stage('Release'){}
    }
    
}