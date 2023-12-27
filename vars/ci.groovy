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
            userRemoteConfigs: [[url:"https://github.com/shreebadiger/${repo_name}"]]
           )
           sh 'cat Jenkinsfile'
        }
        
        if(app_type == "nodejs"){
        stage('Download dependencies'){
            sh 'npm install'
        }
        }
       
        if (env.JOB_BASE_NAME ==~ "PR.*") {
            sh 'echo PR'
            stage('Test case'){}
            stage('Code Quality'){}
        }
        else if (env.BRANCH_NAME == "main"){
            sh 'echo main' 
            stage('Build'){}
        }
        else if (env.TAG_NAME ==~ ".*"){
            sh 'echo tag'
            stage('Build'){}
            stage('Release'){}
        }
        else {
            sh 'echo branch'
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