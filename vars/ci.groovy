def AWS_SSM_PARAM(param_name){
    def OUTPUT = sh (script: "aws ssm get-parameter --name ${param_name} --with-decryption --query 'Parameter.Value' --output text",returnStdout: true
).trim()
    return(OUTPUT)
}
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
            stage('Test case'){
            // sh 'npm test'
            }
            stage('Code Quality'){
                print (AWS_SSM_PARAM(param_name:'sonar.token'))
           // env.SONAR_TOKEN = AWS_SSM_PARAM(param_name:'sonar.token')
            //sh 'sonar-scanner -Dsonar.host.url=http://172.31.83.244:9000 -Dsonar.login=${SONAR_TOKEN} -Dsonar.projectKey=${repo_name}'
            }
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
            //sh 'npm test'
        }       
    }    
}