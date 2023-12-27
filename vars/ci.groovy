def call () {
    node ('workstation') {
        if(env.TAG_NAME ==~ ".*") {
            env.branchName = env.TAG_NAME
        } else
        {
            env.branchName = env.BRANCH_NAME
        }
        stage('Code checkout'){
           // git branch: 'main', url: 'https://github.com/shreebadiger/expense-backend.git'
           checkout scmGit(
            branches: [[name : "${branchName}"]],
            userRemoteConfigs: [[url:"https://github.com/shreebadiger/expense-backend.git"]]
           )
        }
        sh 'ls'
        stage('Compile'){}
       
        if (env.BRANCH_NAME == "main") {
            stage('Build'){}
        }
        else if (env.BRANCH_NAME ==~ "PR.*"){
            stage('Test case'){}
            stage('Integration test'){}
        }
        else if (env.TAG_NAME ==~ ".*"){
            stage('Build'){}
            stage('Release'){}
        }
        else {
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