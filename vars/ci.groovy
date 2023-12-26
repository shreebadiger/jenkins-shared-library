def call () {
    node ('workstation') {
        sh 'env'
        if (env.BRANCH_NAME == "main") {
            stage('Code checkout'){}
            stage('Compile'){}
            stage('Build'){}
        }
        
    }
    
}
        //stage('Code checkout'){}
        //stage('Compile'){}
        //stage('Test case'){}
        //stage('Integration test'){}
        //stage('Build'){}
        //stage('Release'){}