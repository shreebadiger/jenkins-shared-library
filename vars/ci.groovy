def AWS_SSM_PARAM(param_name){
    def OUTPUT = sh (script: "aws ssm get-parameter --name ${param_name} --with-decryption --query 'Parameter.Value' --output text",returnStdout: true).trim()
    return(OUTPUT)
}
def call () {
    node ('workstation') {
        common.codeCheckout() 
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
            env.SONAR_TOKEN = AWS_SSM_PARAM('sonar.token')
            wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_TOKEN}", var: 'PASSWORD']]]) {
            sh 'sonar-scanner -Dsonar.host.url=http://172.31.83.244:9000 -Dsonar.login=${SONAR_TOKEN} -Dsonar.projectKey=${repo_name} -Dsonar.qualitygate.wait=true -Dsonar.exlusions=node_modules/**'
               }            
            }           
        }
        else if (env.BRANCH_NAME == "main"){
            sh 'echo main' 
            stage('Build'){}
        }
        else if (env.TAG_NAME ==~ ".*"){
            sh 'echo tag'
            stage('Build'){
            sh 'zip -r ${repo_name}-${TAG_NAME}.zip *'
            }
            
            stage('Release'){
               env.ARTIFACTORY_PASSWORD = AWS_SSM_PARAM('artifactory.password')
               wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${ARTIFACTORY_PASSWORD}", var: 'PASSWORD']]]){
               sh 'curl -sSf -u "admin:${ARTIFACTORY_PASSWORD}" -X PUT -T ${repo_name}-${TAG_NAME}.zip http://artifactory.sbadiger93.online:8081/artifactory/${repo_name}/${repo_name}-${TAG_NAME}.zip'
              }
        }
        }
    
        else {
            sh 'echo branch'
            stage('Test case'){
                 //sh 'npm test'
            }
           
        }       
    }    
}