def call () {
    node ('workstation') {
        sh "find . | sed -e '1d' | xargs rm -rf"
        if(env.TAG_NAME ==~ ".*") {
            env.branch_name = "refs/tags/${env.TAG_NAME}"
        } else {
            if(env.BRANCH_NAME ==~ "PR-.*") {
                env.branch_name = "${env.CHANGE_BRANCH}"
            }
            else{
                env.branch_name = "${env.BRANCH_NAME}"
            }
            env.branch_name = env.BRANCH_NAME
        }
        stage('Code checkout'){
           // git branch: 'main', url: 'https://github.com/shreebadiger/expense-backend.git'
           checkout scmGit(
            branches: [[name : "${branch_name}"]],
            userRemoteConfigs: [[url:"https://github.com/shreebadiger/expense-backend.git"]]
           )
           sh 'cat Jenkinsfile'
        }
    
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