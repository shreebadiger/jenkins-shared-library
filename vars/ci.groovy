def call () {
    node ('workstation') {
        sh 'env'
        if (env.BRANCH_NAME == "main") {
            stage('Code checkout'){}
            stage('Compile'){}
            stage('Build'){}
        }
        else if (env.BRANCH_NAME ==~ "PR.*"){
            stage('Code checkout'){}
            stage('Compile'){}
            stage('Test case'){}
            stage('Integration test'){}
        }
        else if (env.TAG_NAME ==~ ".*"){
            stage('Code checkout'){}
            stage('Compile'){}
            stage('Build'){}
            stage('Release'){}
        }
        else {
            stage('Code checkout'){}
            stage('Compile'){}
            stage('Test case'){}
        }       
    }    
}
        //stage('Code checkout'){}
        //stage('Compile'){}
        //stage('Test case'){}
        //stage('Integration test'){}
        //stage('Build'){}
        //stage('Release'){}